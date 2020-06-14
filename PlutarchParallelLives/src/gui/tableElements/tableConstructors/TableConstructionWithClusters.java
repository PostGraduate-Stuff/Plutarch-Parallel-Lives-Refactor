package gui.tableElements.tableConstructors;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import phaseAnalyzer.commons.Phase;
import tableClustering.clusterExtractor.commons.Cluster;
import data.dataKeeper.GlobalDataKeeper;
import data.dataPPL.pplSQLSchema.PPLSchema;
import data.dataPPL.pplTransition.AtomicChange;
import data.dataPPL.pplTransition.PPLTransition;
import data.dataPPL.pplTransition.TableChange;

public class TableConstructionWithClusters extends TableConstruction{
	
	private ArrayList<Phase> phases = new ArrayList<Phase>();
	private ArrayList<Cluster> clusters = new ArrayList<Cluster>();


	private int columnsNumber=0;
	private Integer[][] schemaColumnId=null;
	private int maxDeletions=1;
	private int maxInsersions=1;
	private int maxUpdates=1;
	private int maxTotalChangesForOneTransition=1;
	private Integer[] segmentSize=new Integer[4];
	
	
	
	public TableConstructionWithClusters(GlobalDataKeeper globalDataKeeper){
		
		globalDataKeeper.getAllPPLSchemas();
		phases=globalDataKeeper.getPhaseCollectors().get(0).getPhases();
		clusters=globalDataKeeper.getClusterCollectors().get(0).getClusters();
		
		
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
			
		for(int j=0; j<clusters.size(); j++){
			String[] tmpOneRow=constructOneRow(clusters.get(j),j);
			allRows.add(tmpOneRow);
			tmpOneRow=new String[columnsNumber];
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
	
private String[] constructOneRow(Cluster incomingCluster,int clusterNumber){
		
		String[] oneRow=new String[columnsNumber];
		int deletedAllTable=0;
		int pointerCell=0;
		int updatesNumber=0;
		int deletionsNumber=0;
		int insertionsNumber=0;
		int totalChangesForOnePhase=0;
		oneRow[pointerCell]="Cluster "+clusterNumber;
		int deadCell=0;
		
		for(int p=0; p<phases.size(); p++){
			if(phases.get(p).getPhasePPLTransitions().containsKey(incomingCluster.getBirth())){
				pointerCell=p+1;
				break;
			}
		}

		for(int p=0; p<phases.size(); p++){
			if(phases.get(p).getPhasePPLTransitions().containsKey(incomingCluster.getDeath()-1)){
				deadCell=p+1;
				break;
			}
		}
		
		int initialization=0;
		if(pointerCell>0){
			initialization=pointerCell-1;
		}
		
		for(int p=initialization; p<phases.size(); p++){
			if(p<deadCell){
				TreeMap<Integer,PPLTransition> phasePPLTransitions=phases.get(p).getPhasePPLTransitions();
	
				if (totalChangesForOnePhase>maxTotalChangesForOneTransition) {
					maxTotalChangesForOneTransition=totalChangesForOnePhase;
				}
				totalChangesForOnePhase=0;
				
				
				for(Map.Entry<Integer, PPLTransition> tempTransition:phasePPLTransitions.entrySet()){
					
					ArrayList<TableChange> tempTableChanges=tempTransition.getValue().getTableChanges();
					
					if(tempTableChanges!=null){
						
						for(int j=0; j<tempTableChanges.size(); j++){
							
							TableChange tableChange=tempTableChanges.get(j);
							
							if(incomingCluster.getTables().containsKey(tableChange.getAffectedTableName())){
								
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
					
					if(deletedAllTable==1){
						break;
					}
					
				}
				
				if(pointerCell>=columnsNumber){
	
					break;
				}
				
				totalChangesForOnePhase=insertionsNumber+updatesNumber+deletionsNumber;
	
				oneRow[pointerCell]=Integer.toString(totalChangesForOnePhase);
				
				pointerCell++;
				
				if(deletedAllTable==1){
					break;
				}
				
				insertionsNumber=0;
				updatesNumber=0;
				deletionsNumber=0;
	
			}
			else{
				break;
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
	
}
