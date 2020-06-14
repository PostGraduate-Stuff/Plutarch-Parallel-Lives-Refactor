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

public class TableConstructionAllSquaresIncluded extends TableConstruction {
	private static TreeMap<String,PPLSchema> allPPLSchemas=new TreeMap<String,PPLSchema>();
	private ArrayList<PPLTable>	tables=new ArrayList<PPLTable>();
	private TreeMap<Integer,PPLTransition> allPPLTransitions = new TreeMap<Integer,PPLTransition>();

	private int columnsNumber=0;
	private Integer[][] schemaColumnId=null;
	private int maxDeletions=1;
	private int maxInsersions=1;
	private int maxUpdates=1;
	private Integer segmentSize[]=new Integer[3];
	private String[] states = {"I","U","D"};
	
	public TableConstructionAllSquaresIncluded(GlobalDataKeeper globalDataKeeper){
		allPPLSchemas=globalDataKeeper.getAllPPLSchemas();
		allPPLTransitions=globalDataKeeper.getAllPPLTransitions();
	}
	
	public void constructColumns(){
		setSchemaColumnId();
		ArrayList<String> columnsList= setColumnsList();
		columnsNumber=columnsList.size();
		String[] tmpcolumns=new String[columnsList.size()];
		for(int j=0; j<columnsList.size(); j++ ){
			tmpcolumns[j]=columnsList.get(j);
		}
		constructedColumns = tmpcolumns;
	}
	
	private void setSchemaColumnId(){
		schemaColumnId=new Integer[allPPLSchemas.size()][2];
		
		schemaColumnId[0][0]=0;
		schemaColumnId[0][1]=1;
		
		for(int i=1;i<allPPLSchemas.size();i++){
			schemaColumnId[i][0]=i;
			schemaColumnId[i][1]=schemaColumnId[i-1][1]+4;
		}
	}
	
	private ArrayList<String> setColumnsList(){
		ArrayList<String> columnsList=new ArrayList<String>();
		columnsList.add("Table name");
		int counter=0;
		for (Map.Entry<String,PPLSchema> pplSchema : allPPLSchemas.entrySet()) {
			String label="v"+pplSchema.getValue().getName().replaceAll(".sql", "");
			columnsList.add(label);
			if(counter<allPPLSchemas.size()-1){
				for(int j=0; j<3; j++){
					columnsList.add(states[j]);
				}
			}
			counter++;
		}
		return columnsList;
	}
	
	public void constructRows(){
		ArrayList<String[]> allRows=new ArrayList<String[]>();
	    ArrayList<String>allTables=new ArrayList<String>();
		int counter=0;
		
		for (Map.Entry<String,PPLSchema> pplSchema : allPPLSchemas.entrySet()) {
			PPLSchema oneSchema=pplSchema.getValue();
			for(int j=0; j<oneSchema.getTables().size(); j++){
				boolean found=false;
				PPLTable oneTable=oneSchema.getTableAt(j);
				String tmpTableName=oneTable.getName();
				for(int k=0; k<allTables.size(); k++){
					if(tmpTableName.equals(allTables.get(k))){
						found=true;
						break;
					}
				}
				if(!found){
					allTables.add(tmpTableName);
					tables.add(oneTable);
					String[] tmpOneRow=constructOneRow(oneTable,counter);
					allRows.add(tmpOneRow);
					oneTable=new PPLTable();
					tmpOneRow=new String[columnsNumber];
				}
			}
			counter++;
		}
		calculateSegmentSize();
		constructedRows = getFinalizedConstructedRows(allRows);
	}
	
	private void calculateSegmentSize(){
		float maxI=(float) maxInsersions/4;
		segmentSize[0]=(int) Math.rint(maxI);
		
		float maxU=(float) maxUpdates/4;
		segmentSize[1]=(int) Math.rint(maxU);
		
		float maxD=(float) maxDeletions/4;
		segmentSize[2]=(int) Math.rint(maxD);
	}
	
	private String[][] getFinalizedConstructedRows(ArrayList<String[]> allRows){
		String[][] rows=new String[allRows.size()][columnsNumber];
		for(int i=0; i<allRows.size(); i++){
			String[] tmpOneRow=allRows.get(i);
			for(int j=0; j<tmpOneRow.length; j++ ){
				rows[i][j]=tmpOneRow[j];
			}
		}
		return rows;
	}
	
	private String[] constructOneRow(PPLTable oneTable,int schemaVersion){
		
		String[] oneRow=new String[columnsNumber];
		int deletedAllTable=0;
		int pointerCell=0;
		int updatesNumber=0;
		int deletionsNumber=0;
		int insertionsNumber=0;
		oneRow[pointerCell]=oneTable.getName();
		
		if(schemaVersion==0){
			pointerCell++;
			oneRow[pointerCell]="---------";
			pointerCell++;
			
		}
		else{
			
			for(int i=0; i<schemaColumnId.length; i++){
				
				if(schemaVersion==schemaColumnId[i][0]){
					pointerCell=schemaColumnId[i][1]-3;
					break;
				}
				
			}
			
		}
		
		
		int initialization=0;
		if(schemaVersion>0){
			initialization=schemaVersion-1;
		}
		
		Integer[] mapKeys = new Integer[allPPLTransitions.size()];
		int index = 0;
		for (Integer key : allPPLTransitions.keySet()) {
		    mapKeys[index++] = key;
		}
		
		Integer position=null;

		for(int i=initialization; i<allPPLTransitions.size(); i++){
			
			position=mapKeys[i];
			
			PPLTransition  currentTransition=allPPLTransitions.get(position);
			
			String versionNameString=currentTransition.getNewVersionName();
			
			ArrayList<TableChange> tableChanges=currentTransition.getTableChanges();
			
			updatesNumber=0;
			deletionsNumber=0;
			insertionsNumber=0;
			
			if(tableChanges!=null){
				for(int j=0; j<tableChanges.size(); j++){
					
					TableChange tableChange=tableChanges.get(j);
					
					if(tableChange.getAffectedTableName().equals(oneTable.getName())){
						
						
						
						ArrayList<AtomicChange> atomicChanges = tableChange.getTableAtomicChangeForOneTransition();
						
						for(int k=0; k<atomicChanges.size(); k++){
							
							
							if (atomicChanges.get(k).getType().contains("Addition")){
								
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
								 
								 
								 
								 int attributesNumber=getNumberOfAttributesOfNextSchema(versionNameString, oneTable.getName());
								 
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
			
			
			oneRow[pointerCell]=Integer.toString(insertionsNumber);
			pointerCell++;
			oneRow[pointerCell]=Integer.toString(updatesNumber);
			pointerCell++;
			oneRow[pointerCell]=Integer.toString(deletionsNumber);
			pointerCell++;
			if(deletedAllTable==1){
				break;
			}
			oneRow[pointerCell]="------";
			pointerCell++;
			
			insertionsNumber=0;
			updatesNumber=0;
			deletionsNumber=0;
			
			
		}
		
		for(int i=0; i<oneRow.length; i++){
			if(oneRow[i]==null){
				oneRow[i]="";
			}
		}
	
		return oneRow;
		
	}
	
	public Integer[] getSegmentSize(){
		return segmentSize;
	}
	
	private int getNumberOfAttributesOfNextSchema(String schema,String table){
		int number = 0;
		PPLSchema pplSchema=allPPLSchemas.get(schema);	
		
		for(int i=0;i<pplSchema.getTables().size();i++){
			if(pplSchema.getTableAt(i).getName().equals(table)){
				number=pplSchema.getTableAt(i).getAttributes().size();
				return number;
			}
		}
		return number;
	}
}
