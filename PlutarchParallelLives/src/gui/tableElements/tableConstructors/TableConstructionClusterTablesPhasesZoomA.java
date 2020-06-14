package gui.tableElements.tableConstructors;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import phaseAnalyzer.commons.Phase;
import data.dataKeeper.GlobalDataKeeper;
import data.dataPPL.pplSQLSchema.PPLSchema;
import data.dataPPL.pplSQLSchema.PPLTable;
import data.dataPPL.pplTransition.AtomicChange;
import data.dataPPL.pplTransition.PPLTransition;
import data.dataPPL.pplTransition.TableChange;

public class TableConstructionClusterTablesPhasesZoomA extends TableConstruction {
	
	private TreeMap<String,PPLSchema> allPPLSchemas=new TreeMap<String,PPLSchema>();
	private ArrayList<PPLTable>	tables=new ArrayList<PPLTable>();
	private ArrayList<Phase> phases = new ArrayList<Phase>();

	private int columnsNumber=0;
	private Integer[][] schemaColumnId=null;
	private int maxDeletions=1;
	private int maxInsersions=1;
	private int maxUpdates=1;
	private int maxTotalChangesForOneTransition=1;
	private Integer[] segmentSize=new Integer[4];
	private ArrayList<String> tablesOfCluster=new ArrayList<String>();
	
	public TableConstructionClusterTablesPhasesZoomA(TreeMap<String,PPLSchema> allPPLSchemas, ArrayList<Phase> phases, ArrayList<String> tablesOfCluster){
		this.allPPLSchemas=allPPLSchemas;
		this.phases=phases;
		this.tablesOfCluster=tablesOfCluster;
	}
	
	public void constructColumns(){
		setSchemaColumnId();
		ArrayList<String> columnsList=setColumnsList();
		columnsNumber=columnsList.size();
		String[] tmpcolumns=new String[columnsList.size()];
		for(int j=0; j<columnsList.size(); j++ ){
			tmpcolumns[j]=columnsList.get(j);
		}
		constructedColumns = tmpcolumns;
	}
	
	private void setSchemaColumnId(){
		schemaColumnId=new Integer[phases.size()][2];
		
		schemaColumnId[0][0]=0;
		schemaColumnId[0][1]=1;
		
		for(int i=1;i<phases.size();i++){
			schemaColumnId[i][0]=i;
			schemaColumnId[i][1]=schemaColumnId[i-1][1]+1;
		}
	}
	
	private ArrayList<String> setColumnsList(){
		ArrayList<String> columnsList=new ArrayList<String>();
		columnsList.add("Table name");
		for(int i=0;i<phases.size(); i++){
			String label="Phase "+i;
			columnsList.add(label);
		}
		return columnsList;
	}
	
	public void constructRows(){
		ArrayList<String[]> allRows=new ArrayList<String[]>();
	    ArrayList<String>	allTables=new ArrayList<String>();
		int i=0;
		for (Map.Entry<String,PPLSchema> pplSchema : allPPLSchemas.entrySet()) {
			PPLSchema oneSchema=pplSchema.getValue();
			for(int j=0; j<oneSchema.getTables().size(); j++){
				boolean found = false;
				PPLTable oneTable=oneSchema.getTableAt(j);
				String tmpTableName=oneTable.getName();
				for(int k=0; k<allTables.size(); k++){
					if(tmpTableName.equals(allTables.get(k))){
						found=true;
						break;
					}
				}
				
				if(!found && tablesOfCluster.contains(tmpTableName)){
					allTables.add(tmpTableName);
					tables.add(oneTable);
					String[] tmpOneRow=constructOneRow(oneTable,i,oneSchema.getName());
					allRows.add(tmpOneRow);
					oneTable=new PPLTable();
					tmpOneRow=new String[columnsNumber];
				}
			}
			i++;
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
		
		float maxT=(float) maxTotalChangesForOneTransition/4;
		segmentSize[3]=(int) Math.rint(maxT);
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
	
	private String[] constructOneRow(PPLTable oneTable,int schemaVersion,String schemaName){
		
		String[] oneRow=new String[columnsNumber];
		int deletedAllTable=0;
		int pointerCell=0;
		int updatesNumber=0;
		int deletionsNumber=0;
		int insertionsNumber=0;
		int totalChangesForOnePhase=0;
		boolean reborn = true;

		oneRow[pointerCell]=oneTable.getName();
		if(schemaVersion==0){
			pointerCell++;
			
		}
		else{
			for(int p=0; p<phases.size(); p++){
				
				TreeMap<Integer,PPLTransition> phasePPLTransitions=phases.get(p).getPhasePPLTransitions();
				for(Map.Entry<Integer, PPLTransition> transitionElement:phasePPLTransitions.entrySet()){
					
					if(transitionElement.getValue().getNewVersionName().equals(schemaName)){	
						pointerCell=p+1;
						break;
					}
				}
				
			}
			
		}
		
		int initialization=0;
		if(pointerCell>0){
			initialization=pointerCell-1;
		}
		
		for(int p=initialization; p<phases.size(); p++){
			TreeMap<Integer,PPLTransition> phasePPLTransitions=phases.get(p).getPhasePPLTransitions();

			if (totalChangesForOnePhase>maxTotalChangesForOneTransition) {
				maxTotalChangesForOneTransition=totalChangesForOnePhase;
			}
			totalChangesForOnePhase=0;
			
			for(Map.Entry<Integer, PPLTransition> tempTransition:phasePPLTransitions.entrySet()){

				String newVersionName=tempTransition.getValue().getNewVersionName();
				
				ArrayList<TableChange> tempTableChange=tempTransition.getValue().getTableChanges();
				
				
				
				if(tempTableChange!=null){
					
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
									 
									 boolean existsLater=getNumberOfAttributesOfNextSchema(newVersionName, oneTable.getName());
									 
									 if(!existsLater){
										 
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
				
			}
			
			if(pointerCell>=columnsNumber){
				
				break;
			}
			totalChangesForOnePhase=insertionsNumber+updatesNumber+deletionsNumber;
			if(totalChangesForOnePhase>=0 && reborn){

				oneRow[pointerCell]=Integer.toString(totalChangesForOnePhase);
				
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
	public Integer[] getSegmentSize(){
		return segmentSize;
	}
	
	private boolean getNumberOfAttributesOfNextSchema(String schema,String table){
		PPLSchema schemaPpl=allPPLSchemas.get(schema);
		return schemaPpl.getTables().containsKey(table);
		
	}

}
