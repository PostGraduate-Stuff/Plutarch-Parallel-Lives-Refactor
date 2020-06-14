package gui.tableElements.tableConstructors;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import data.dataKeeper.GlobalDataKeeper;
import data.dataPPL.pplSQLSchema.PPLSchema;
import data.dataPPL.pplSQLSchema.PPLTable;
import data.dataPPL.pplTransition.AtomicChange;
import data.dataPPL.pplTransition.PPLTransition;
import data.dataPPL.pplTransition.TableChange;

public class TableConstructionIDU extends TableConstruction {

	private TreeMap<String,PPLSchema> allPPLSchemas=new TreeMap<String,PPLSchema>();
	private ArrayList<PPLTable>	tables=new ArrayList<PPLTable>();
	private TreeMap<Integer,PPLTransition> allPPLTransitions = new TreeMap<Integer,PPLTransition>();
	
	private int columnsNumber=0;
	private Integer[][] schemaColumnId=null;
	private int maxDeletions=1;
	private int maxInsersions=1;
	private int maxUpdates=1;
	private int maxTotalChangesForOneTransition=1;
	
	private Integer segmentSize[]=new Integer[4];
	
	private boolean reborn = false;
	private int deletedAllTable;
	
	public TableConstructionIDU(TreeMap<String,PPLSchema> allPPLSchemas, TreeMap<Integer,PPLTransition> allPPLTransitions){
		
		this.allPPLSchemas = allPPLSchemas;
		this.allPPLTransitions = allPPLTransitions;
		
		
	}
	
	public void constructColumns(){
		setSchemaColumnId();
		ArrayList<String> columnsList=setColumnsList();
		this.constructedColumns = new String[columnsList.size()];  
		columnsNumber=columnsList.size();
		for(int j=0; j<columnsList.size(); j++ )
		{
			this.constructedColumns[j]=columnsList.get(j);
		}	
	}
	
	private void setSchemaColumnId(){
		schemaColumnId=new Integer[allPPLTransitions.size()][2];
		
		schemaColumnId[0][0]=0;
		schemaColumnId[0][1]=1;
		
		for(int i=1;i<allPPLTransitions.size();i++){
			schemaColumnId[i][0]=i;
			schemaColumnId[i][1]=schemaColumnId[i-1][1]+1;
		}
	}
	
	private ArrayList<String> setColumnsList(){
		ArrayList<String> columnsList=new ArrayList<String>();
		columnsList.add("Table name");
		for (Map.Entry<Integer,PPLTransition> pplTransition : allPPLTransitions.entrySet()) 
		{
			String label=Integer.toString(pplTransition.getKey());
			columnsList.add(label);
		}
		return columnsList;
	}
	
	public void constructRows(){
		
		ArrayList<String[]> allRows= createRowsFromTables();

		String[][] rows =  getRows(allRows);
		
		calculateSegmentSize();
		
		this.constructedRows = rows;
	}
	
	public String[][] getRows(ArrayList<String[]> allRows){
		String[][] tmpRows = new String[allRows.size()][columnsNumber];
		
		for(int z=0; z<allRows.size(); z++){
			
			String[] tmpOneRow=allRows.get(z);
			for(int j=0; j<tmpOneRow.length; j++ )
			{
				tmpRows[z][j]=tmpOneRow[j];	
			}
			
		}
		return tmpRows;
	}
	
	public void calculateSegmentSize(){
		float maxI=(float) maxInsersions/4;
		segmentSize[0]=(int) Math.rint(maxI);
		
		float maxU=(float) maxUpdates/4;
		segmentSize[1]=(int) Math.rint(maxU);
		
		float maxD=(float) maxDeletions/4;
		segmentSize[2]=(int) Math.rint(maxD);
		
		float maxTot=(float) maxTotalChangesForOneTransition/4;
		segmentSize[3]=(int) Math.rint(maxTot);
	}
	
	
	public ArrayList<String[]> createRowsFromTables()
	{
		ArrayList<String[]> allRows = new ArrayList<String[]>();
		ArrayList<String> allTables=new ArrayList<String>();
		int i=0;
		
		for (Map.Entry<String,PPLSchema> pplSchemas : allPPLSchemas.entrySet()) {
			
			PPLSchema oneSchema=pplSchemas.getValue();
			for(int j=0; j<oneSchema.getTables().size(); j++){
				
				PPLTable oneTable=oneSchema.getTableAt(j);
				String tmpTableName=oneTable.getName();
				
				if(tableExists(allTables, tmpTableName)){
					continue;
				}
				allTables.add(tmpTableName);
				tables.add(oneTable);
				String[] tmpOneRow=constructOneRow(oneTable,i-1);
				allRows.add(tmpOneRow);
				oneTable=new PPLTable();
				tmpOneRow=new String[columnsNumber];
			}
			
			i++;
		}
		return allRows;
	
	}
	
	public boolean tableExists(ArrayList<String> allTables,String tmpTableName)
	{
		for(int k=0; k<allTables.size(); k++){

			if(tmpTableName.equals(allTables.get(k))){
				return true;
			}	
		}
		return false;
	}
	
	public int getPointCell(int schemaVersion){
		int toReturn = 0;
		if(schemaVersion==-1){
			toReturn++;
			return toReturn;
		}
		for(int i=0; i<schemaColumnId.length; i++){
			if(schemaVersion==schemaColumnId[i][0]){
				toReturn=schemaColumnId[i][1];
				return toReturn;
			}
		}
		return toReturn;
	}
	
	private String[] constructOneRow(PPLTable oneTable,int schemaVersion){
		
		String[] oneRow=new String[columnsNumber];
		int deletedAllTable=0;
		int pointerCell=0;
		int updatesNumber=0;
		int deletionsNumber=0;
		int insertionsNumber=0;
		int totalChangesForOneTransition=0;
		boolean reborn = true;
		oneRow[pointerCell]=oneTable.getName();
		
		if(schemaVersion==-1){
			pointerCell++;
			
			
		}
		else{
			
			for(int i=0; i<schemaColumnId.length; i++){
				
				if(schemaVersion == schemaColumnId[i][0]){
					pointerCell=schemaColumnId[i][1];
					break;
				}
				
			}
			
		}
		
		
		int initialization=0;
		if(schemaVersion>0){
			initialization=schemaVersion;
		}
		
		Integer[] mapKeys = new Integer[allPPLTransitions.size()];
		int index = 0;
		for (Integer key : allPPLTransitions.keySet()) {
		    mapKeys[index++] = key;
		}
		
		Integer position=null;

		for(int i=initialization; i<allPPLTransitions.size(); i++){
			
			position=mapKeys[i];
			
			PPLTransition  tempTransition=allPPLTransitions.get(position);
			
			String newVersionName=tempTransition.getNewVersionName();
			
			
			ArrayList<TableChange> tempTableChange=tempTransition.getTableChanges();
			
			updatesNumber=0;
			deletionsNumber=0;
			insertionsNumber=0;
			
			if(tempTableChange!=null){
				totalChangesForOneTransition=-1;
				
				for(int j=0; j<tempTableChange.size(); j++){
					
					TableChange tableChange=tempTableChange.get(j);
					if(tableChange.getAffectedTableName().equals(oneTable.getName())){
						if(deletedAllTable==1){
							reborn=true;
						}
						deletedAllTable=0;
						
						ArrayList<AtomicChange> atomicChanges = tableChange.getTableAtomicChangeForOneTransition();

						for(int k=0; k<atomicChanges.size(); k++){
							
							
							if (atomicChanges.get(k).getType().contains("Addition")){
								deletedAllTable=0;
								insertionsNumber++;
								
								if(insertionsNumber>maxInsersions){
									maxInsersions=insertionsNumber;
								}
								
							}
							else if(atomicChanges.get(k).getType().contains("Deletion")){

								deletionsNumber++;
								
								 if(deletionsNumber>maxDeletions){
										maxDeletions=deletionsNumber;
										
								 }
								 								 
								 int attributesNumber=getNumberOfAttributesOfNextSchema(newVersionName, oneTable.getName());
								 if(attributesNumber==0){
									 
									 deletedAllTable=1;
									 
								 }
								 
							}
							else{

								updatesNumber++;
								
								if(updatesNumber>maxUpdates){
									maxUpdates=updatesNumber;
								}
																
							}
							
						}
					}
					 
					 
				}
				
				
			}
			if(pointerCell>=columnsNumber){
				
				break;
			}
			totalChangesForOneTransition=insertionsNumber+updatesNumber+deletionsNumber;
			if(totalChangesForOneTransition>=0 && reborn){

				oneRow[pointerCell]=Integer.toString(totalChangesForOneTransition);
				
			}
			
			pointerCell++;
			if(deletedAllTable==1){
				if(pointerCell>=columnsNumber){
					break;
				}
				if(!reborn){
					oneRow[pointerCell]="";
					pointerCell++;
				}
				reborn=false;
				
				
			}
			
			if (totalChangesForOneTransition>maxTotalChangesForOneTransition) {
				maxTotalChangesForOneTransition=totalChangesForOneTransition;
			}
			
			insertionsNumber=0;
			updatesNumber=0;
			deletionsNumber=0;
			
			
		}
		
		for(int i=0; i<oneRow.length; i++){
			if(oneRow[i]==null){
				oneRow[i]="";
			}
		}
		String helper="";
		for (int i = 0; i < oneRow.length; i++) {
			helper=helper+oneRow[i]+",";
		}	
	
		return oneRow;
		
	}
	public int calculateTotalChanges(PPLTransition tempTransition, PPLTable oneTable){
		String newVersionName=tempTransition.getNewVersionName();
		ArrayList<TableChange> tableChanges = tempTransition.getTableChanges();//tmpTR
		int updates=0;
		int deletions=0;
		int inserts=0;
		
		for(int j=0; j<tableChanges.size(); j++){
			
			TableChange tableChange=tableChanges.get(j);
			if(!tableChange.getAffectedTableName().equals(oneTable.getName())){
				continue;
			}
			if(deletedAllTable==1){
				reborn=true;
			}
			deletedAllTable=0;
			ArrayList<AtomicChange> atomicChanges = tableChange.getTableAtomicChangeForOneTransition();
			for(int k=0; k<atomicChanges.size(); k++){
				if (atomicChanges.get(k).getType().contains("Addition")){
					deletedAllTable=0;
					inserts++;
					if(inserts>maxInsersions){
						maxInsersions=inserts;
					}
				}
				else if(atomicChanges.get(k).getType().contains("Deletion")){
					deletions++;
					 if(deletions>maxDeletions){
							maxDeletions=deletions;
					 }
					 int numberOfattributes = getNumberOfAttributesOfNextSchema(newVersionName, oneTable.getName());
					 if(numberOfattributes == 0){
						 deletedAllTable=1;
					 }
				}
				else{
					updates++;
					if(updates>maxUpdates){
						maxUpdates=updates;
					}
				}
			}
		}
		return (inserts+updates+deletions);
	}
	
	public Integer[] getSegmentSize(){
		return segmentSize;
	}
	
	private int getNumberOfAttributesOfNextSchema(String schema,String table){
		int number = 0;
		PPLSchema schemaPPL=allPPLSchemas.get(schema);
		
		for(int i=0;i<schemaPPL.getTables().size();i++){
			if(schemaPPL.getTableAt(i).getName().equals(table)){
				number=schemaPPL.getTableAt(i).getAttributes().size();
				return number;
			}
		}
		return number;
	}

}
