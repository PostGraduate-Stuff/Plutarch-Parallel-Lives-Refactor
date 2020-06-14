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

public class TableConstructionZoomArea extends TableConstruction {

	private static TreeMap<String,PPLSchema> allPPLSchemas=new TreeMap<String,PPLSchema>();
	private static TreeMap<String,PPLSchema> selectedPPLSchemas=new TreeMap<String,PPLSchema>();
	private ArrayList<PPLTable>	tables=new ArrayList<PPLTable>();
	private TreeMap<String,PPLTable> selectedTables = new TreeMap<String,PPLTable>();
	private ArrayList<String> sSelectedTables = new ArrayList<String>();
	private TreeMap<Integer,PPLTransition> pplTransitions = new TreeMap<Integer,PPLTransition>();
	private GlobalDataKeeper globalDataKeeper = new GlobalDataKeeper();
	private int selectedColumn;

	private int columnsNumber=0;
	private Integer[][] schemaColumnId=null;
	private int maxDeletions=1;
	private int maxInsersions=1;
	private int maxUpdates=1;
	private int maxTotalChangesForOneTransition=1;
	
	private Integer segmentSize[]=new Integer[4];
	
	public TableConstructionZoomArea(GlobalDataKeeper globalDataKeeper,ArrayList<String> sSelectedTables,int selectedColumn){
		this.globalDataKeeper=globalDataKeeper;
		allPPLSchemas=globalDataKeeper.getAllPPLSchemas();
		this.sSelectedTables=sSelectedTables;
		this.selectedColumn=selectedColumn;
		fillSelectedPPLTransitions();
		fillSelectedPPLSchemas();
		fillSelectedTables();
	}
	
	
	
	private void fillSelectedPPLTransitions() {
		
		if(selectedColumn==0){
			pplTransitions=globalDataKeeper.getAllPPLTransitions();
		}
		else{
			pplTransitions=globalDataKeeper.getPhaseCollectors().get(0).getPhases().get(selectedColumn-1).getPhasePPLTransitions();

		}
		
	}
	
	private void fillSelectedPPLSchemas(){
		
		for (Map.Entry<Integer,PPLTransition> pplTr : pplTransitions.entrySet()) {
			
			selectedPPLSchemas.put(pplTr.getValue().getOldVersionName(), allPPLSchemas.get(pplTr.getValue().getOldVersionName()));
			
		}
		
	}
	
	private void fillSelectedTables(){
		
		for(int i=0; i<sSelectedTables.size(); i++){
			selectedTables.put(sSelectedTables.get(i),this.globalDataKeeper.getAllPPLTables().get(sSelectedTables.get(i)) );
		}
		
	}
	
	public void constructColumns(){
		
		setSchemaColumnId();
		ArrayList<String> columnsList=setColumnsList();
		columnsNumber=columnsList.size();
		String[] tmpcolumns=new String[columnsList.size()];
		for(int j=0; j<columnsList.size(); j++ ){
			tmpcolumns[j]=columnsList.get(j);
		}
		this.constructedColumns = tmpcolumns;
	}
	
	private void setSchemaColumnId(){
		schemaColumnId=new Integer[pplTransitions.size()][2];
		
		schemaColumnId[0][0]=0;
		schemaColumnId[0][1]=1;
		
		for(int i=1;i<pplTransitions.size();i++){
			schemaColumnId[i][0]=i;
			schemaColumnId[i][1]=schemaColumnId[i-1][1]+1;
		}
	}
	
	private ArrayList<String> setColumnsList(){
		ArrayList<String> columnsList=new ArrayList<String>();
		columnsList.add("Table name");
		for (Map.Entry<Integer,PPLTransition> pplTr : pplTransitions.entrySet()) {
				String label=Integer.toString(pplTr.getKey());
				columnsList.add(label);
		}
		return columnsList;
	}
	
	public void constructRows(){
		
		ArrayList<String[]> allRows=new ArrayList<String[]>();
	    ArrayList<String>	allTables=new ArrayList<String>();
		for(Map.Entry<String, PPLTable> oneTable:selectedTables.entrySet()){
			boolean found = false;
			String tmpTableName=oneTable.getKey();
			for(int k=0; k<allTables.size(); k++){
				if(tmpTableName.equals(allTables.get(k))){
					found=true;
					break;
				}
			}
			if(!found){
				allTables.add(tmpTableName);
				tables.add(oneTable.getValue());
				String[] tmpOneRow=constructOneRow(oneTable.getValue());
				allRows.add(tmpOneRow);
				tmpOneRow=new String[columnsNumber];
			}
		}
		calculateSegmentSize();
		this.constructedRows = getFinalizedConstructedRows(allRows);
	}
	
	public void calculateSegmentSize()
	{
		float maxI=(float) maxInsersions/4;
		segmentSize[0]=(int) Math.rint(maxI);
		
		float maxU=(float) maxUpdates/4;
		segmentSize[1]=(int) Math.rint(maxU);
		
		float maxD=(float) maxDeletions/4;
		segmentSize[2]=(int) Math.rint(maxD);
		
		float maxTot=(float) maxTotalChangesForOneTransition/4;
		segmentSize[3]=(int) Math.rint(maxTot);
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
	
	private String[] constructOneRow(PPLTable oneTable){
		
		String[] oneRow=new String[columnsNumber];
		int deletedAllTable=0;
		int pointerCell=0;
		int updatesNumber=0;
		int deletionsNumber=0;
		int insertionsNumber=0;
		int totalChangesForOneTransition=-1;
		boolean reborn = true;
		oneRow[pointerCell]=oneTable.getName();
		boolean exists=false;
		for (Map.Entry<Integer,PPLTransition> currentPPLTransition : pplTransitions.entrySet()) {
						
			pointerCell++;
			
			PPLSchema oldSchema = allPPLSchemas.get(currentPPLTransition.getValue().getOldVersionName());
			PPLSchema newSchema = allPPLSchemas.get(currentPPLTransition.getValue().getNewVersionName());

			if(oldSchema.getTables().containsKey(oneTable.getName())|| newSchema.getTables().containsKey(oneTable.getName())){
				exists=true;
				break;
			}
			
		}		
		
		
		int initialization=pointerCell-1;
		
		
		Integer[] mapKeys = new Integer[pplTransitions.size()];
		int index = 0;
		for (Integer key : pplTransitions.keySet()) {
		    mapKeys[index++] = key;
		}
		
		Integer position=null;
		if(exists){
			for(int i=initialization; i<pplTransitions.size(); i++){
				
				position=mapKeys[i];
				
				PPLTransition  tempTransition=pplTransitions.get(position);
				
				String newSchemaName=tempTransition.getNewVersionName();
				
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
									 
									 int numberofAttributes=getNumOfAttributesOfNextSchema(newSchemaName, oneTable.getName());
									 
									 if(numberofAttributes==0){
										 
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
	public Integer[] getSegmentSize(){

		return segmentSize;
	}
	
	private int getNumOfAttributesOfNextSchema(String schema,String table){
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
