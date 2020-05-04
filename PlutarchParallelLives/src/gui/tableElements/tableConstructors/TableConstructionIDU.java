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

public class TableConstructionIDU implements PldConstruction {

	private TreeMap<String,PPLSchema> allPPLSchemas=new TreeMap<String,PPLSchema>();
	private ArrayList<PPLTable>	tables=new ArrayList<PPLTable>();
	private TreeMap<Integer,PPLTransition> allPPLTransitions = new TreeMap<Integer,PPLTransition>();
	
	private String[] constructedColumns;
	private String[][] constructedRows;

	private int columnsNumber=0;
	private Integer[][] schemaColumnId=null;
	private int maxDeletions=1;
	private int maxInsersions=1;
	private int maxUpdates=1;
	private int maxTotalChangesForOneTr=1;
	
	private Integer segmentSize[]=new Integer[4];
	
	private boolean reborn = false;
	private int deletedAllTable;
	
	public TableConstructionIDU(TreeMap<String,PPLSchema> allPPLSchemas, TreeMap<Integer,PPLTransition> allPPLTransitions){
		
		this.allPPLSchemas = allPPLSchemas;
		this.allPPLTransitions = allPPLTransitions;
		
		
	}
	
	public String[] getConstructedColumns()
	{
		return constructedColumns;
	}
	
	public String[][] getConstructedRows()
	{
		return constructedRows;
	}
	
	public void constructColumns(){
		
		ArrayList<String> columnsList=new ArrayList<String>();
		
		schemaColumnId=new Integer[allPPLTransitions.size()][2];
		schemaColumnId[0][1]=1;
		schemaColumnId[0][0]=0;
		for(int i=1;i<allPPLTransitions.size();i++){
			schemaColumnId[i][0]=i;
			schemaColumnId[i][1]=schemaColumnId[i-1][1]+1;
		}
		
		columnsList.add("Table name");
		
		for (Map.Entry<Integer,PPLTransition> pplTransition : allPPLTransitions.entrySet()) 
		{
			String label=Integer.toString(pplTransition.getKey());
			columnsList.add(label);
		}
		this.constructedColumns = new String[columnsList.size()];  
		columnsNumber=columnsList.size();
		
		for(int j=0; j<columnsList.size(); j++ )
		{
			this.constructedColumns[j]=columnsList.get(j);
		}	
		
	}
	

	
	
	public void constructRows(){
		
		ArrayList<String[]> allRows= createRowsFromTables();

		String[][] tmpRows =  getRows(allRows);
		
		calculateSegmentSize();
		
		this.constructedRows = tmpRows;
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
		
		float maxTot=(float) maxTotalChangesForOneTr/4;
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
		System.out.println("HEREERER");
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
		int updn=0;
		int deln=0;
		int insn=0;
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
		int pos2 = 0;
		for (Integer key : allPPLTransitions.keySet()) {
		    mapKeys[pos2++] = key;
		}
		
		Integer pos3=null;

		for(int i=initialization; i<allPPLTransitions.size(); i++){
			
			pos3=mapKeys[i];
			
			PPLTransition  tmpTL=allPPLTransitions.get(pos3);
			
			String sc=tmpTL.getNewVersionName();
			
			
			ArrayList<TableChange> tmpTR=tmpTL.getTableChanges();
			
			updn=0;
			deln=0;
			insn=0;
			
			if(tmpTR!=null){
				totalChangesForOneTransition=-1;
				
				for(int j=0; j<tmpTR.size(); j++){
					
					TableChange tableChange=tmpTR.get(j);
					if(tableChange.getAffectedTableName().equals(oneTable.getName())){
						if(deletedAllTable==1){
							reborn=true;
						}
						deletedAllTable=0;
						
						ArrayList<AtomicChange> atChs = tableChange.getTableAtChForOneTransition();

						for(int k=0; k<atChs.size(); k++){
							
							
							if (atChs.get(k).getType().contains("Addition")){
								deletedAllTable=0;
								insn++;
								
								if(insn>maxInsersions){
									maxInsersions=insn;
								}
								
							}
							else if(atChs.get(k).getType().contains("Deletion")){

								deln++;
								
								 if(deln>maxDeletions){
										maxDeletions=deln;
										
								 }
								 								 
								 int num=getNumOfAttributesOfNextSchema(sc, oneTable.getName());
								 if(num==0){
									 
									 deletedAllTable=1;
									 
								 }
								 
							}
							else{

								updn++;
								
								if(updn>maxUpdates){
									maxUpdates=updn;
								}
																
							}
							
						}
					}
					 
					 
				}
				
				
			}
			if(pointerCell>=columnsNumber){
				
				break;
			}
			totalChangesForOneTransition=insn+updn+deln;
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
			
			if (totalChangesForOneTransition>maxTotalChangesForOneTr) {
				maxTotalChangesForOneTr=totalChangesForOneTransition;
			}
			
			insn=0;
			updn=0;
			deln=0;
			
			
		}
		
		for(int i=0; i<oneRow.length; i++){
			if(oneRow[i]==null){
				oneRow[i]="";
			}
		}
		String lala="";
		for (int i = 0; i < oneRow.length; i++) {
			lala=lala+oneRow[i]+",";
		}	
	
		return oneRow;
		
	}
	
	/*
	private String[] constructOneRow(PPLTable oneTable,int schemaVersion){
		
		String[] oneRow=new String[columnsNumber];
		deletedAllTable=0;
		int pointerCell=0;
		
		int totalChangesForOneTransition=0;
		//boolean reborn = true;
		oneRow[pointerCell]=oneTable.getName();
		
		
		pointerCell = getPointCell(schemaVersion);
		
		int initialization=0;
		if(schemaVersion>0){
			initialization=schemaVersion;
		}
		
		Integer[] mapKeys = new Integer[allPPLTransitions.size()];
		int iterator = 0;
		for (Integer key : allPPLTransitions.keySet()) {
		    mapKeys[iterator++] = key;
		}
		
		Integer keyOfPPLTransition = null;

		for(int i=initialization; i<allPPLTransitions.size(); i++){
			
			keyOfPPLTransition = mapKeys[i];
			
			PPLTransition  tempTransition=allPPLTransitions.get(keyOfPPLTransition);
			
			
			if(tempTransition.getTableChanges()!=null){
				totalChangesForOneTransition = calculateTotalChanges(tempTransition, oneTable);
			}
			
			if(pointerCell>=columnsNumber){
				break;
			}
			
			//reborn
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
			
			if (totalChangesForOneTransition>maxTotalChangesForOneTr) {
				maxTotalChangesForOneTr=totalChangesForOneTransition;
			}
			
			
			
		}
		
		for(int i=0; i<oneRow.length; i++){
			if(oneRow[i]==null){
				oneRow[i]="";
			}
		}
//		String lala="";
//		for (int i = 0; i < oneRow.length; i++) {
//			lala=lala+oneRow[i]+",";
//		}	
	
		return oneRow;
		
	}
	*/
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
			
			ArrayList<AtomicChange> atomicChanges = tableChange.getTableAtChForOneTransition();

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
					 								 
					 int numberOfattributes = getNumOfAttributesOfNextSchema(newVersionName, oneTable.getName());
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
		//apo thn test alliws 0
		return (inserts+updates+deletions);
	}
	
	
	
	public Integer[] getSegmentSize(){
		return segmentSize;
	}
	
	private int getNumOfAttributesOfNextSchema(String schema,String table){
		int num = 0;
		PPLSchema sc=allPPLSchemas.get(schema);
		
		for(int i=0;i<sc.getTables().size();i++){
			if(sc.getTableAt(i).getName().equals(table)){
				num=sc.getTableAt(i).getAttrs().size();
				return num;
			}
		}
		return num;
	}

}
