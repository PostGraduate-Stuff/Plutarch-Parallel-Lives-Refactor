package tableClustering.clusterExtractor.analysis;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import tableClustering.clusterExtractor.commons.Cluster;
import tableClustering.clusterExtractor.commons.ClusterCollector;
import data.dataKeeper.GlobalDataKeeper;
import data.dataPPL.pplSQLSchema.PPLTable;


public class AgglomerativeClusterExtractor implements ClusterExtractor{

	@Override
	public ClusterCollector extractAtMostKClusters(GlobalDataKeeper dataKeeper,
			int numClusters, Double birthWeight, Double deathWeight, Double changeWeight) {
		
		ClusterCollector initSolution = new ClusterCollector();
		this.init(dataKeeper, initSolution);
		
		ClusterCollector currentSolution = new ClusterCollector();
		currentSolution = this.newClusterCollector(initSolution, birthWeight, deathWeight, changeWeight,dataKeeper.getAllPPLSchemas().size()-1);
		while (currentSolution.getClusters().size() > numClusters){
			currentSolution = this.newClusterCollector(currentSolution, birthWeight, deathWeight, changeWeight,dataKeeper.getAllPPLSchemas().size()-1);
		}
		return currentSolution;
		
	}
	
	public ClusterCollector newClusterCollector(ClusterCollector prevCollector,Double birthWeight, Double deathWeight ,Double changeWeight,int dbDuration){
		
		ClusterCollector newCollector = new ClusterCollector();
		ArrayList<Cluster> oldClusters = prevCollector.getClusters();

		int oldSize = oldClusters.size();
		if (oldSize == 0){
			
			System.exit(-10);
		}

		double distances[][] = calculateDistance(oldClusters, birthWeight, deathWeight , changeWeight, dbDuration);

	    
		int posI=-1; 
	    int posJ=-1;
	    //double minDist = getminDistance(distances, oldSize, posI, posJ);
	    double minDist = Double.MAX_VALUE;
	    for(int i=0; i<oldSize; i++){
	    	 for(int j=0; j<oldSize; j++){
	    		if(i!=j){
			    	if(distances[i][j]<minDist){
			    		posI = i;
			    		posJ = j;
			    		minDist = distances[i][j];
	
			    	}
	    		}
	    	 }
	    }
		Cluster toMerge = oldClusters.get(posI);
		Cluster newCluster = toMerge.mergeWithNextCluster(oldClusters.get(posJ));
		
		ArrayList<Cluster> newClusters = createNewCulusters(posI, posJ, oldClusters);
		newClusters.add(newCluster);
		
		newCollector.setClusters(newClusters);

		return newCollector;
	}
	
	
	private ArrayList<Cluster> createNewCulusters(int posI, int posJ, ArrayList<Cluster> oldClusters) {
		ArrayList<Cluster> newClusters = new ArrayList<Cluster>();
		for(int i=0; i < oldClusters.size(); i++){
			if(i!=posI && i!=posJ){
				Cluster c = oldClusters.get(i);
				newClusters.add(c);
			}
		}
		return newClusters;
	}

	private double getminDistance(double[][] distances, int oldSize, int posI, int posJ) {
	
		double minDist = Double.MAX_VALUE;
	    for(int i=0; i<oldSize; i++){
	    	 for(int j=0; j<oldSize; j++){
	    		if(i!=j){
			    	if(distances[i][j]<minDist){
			    		posI = i;
			    		posJ = j;
			    		minDist = distances[i][j];
	
			    	}
	    		}
	    	 }
	    }
	    return minDist;
	}

	private double[][] calculateDistance(ArrayList<Cluster> oldClusters, Double birthWeight, Double deathWeight, Double changeWeight, int dbDuration) {
		double[][] distances = new double[oldClusters.size()][oldClusters.size()];
		
	    for(int i=0; i<oldClusters.size(); i++){
	    	Cluster currentCluster=oldClusters.get(i);
		    for(int j=0; j<oldClusters.size(); j++){
	    		Cluster clusterToCompare=oldClusters.get(j);
	    		distances[i][j] = currentCluster.calculateDistance(clusterToCompare,birthWeight,deathWeight,changeWeight,dbDuration);
	    	}
	    }
	    return distances;
	}

	public ClusterCollector init(GlobalDataKeeper dataKeeper, ClusterCollector clusterCollector){
		
		TreeMap<String, PPLTable> tables=dataKeeper.getAllPPLTables();

		
		for (Map.Entry<String,PPLTable> pplTable : tables.entrySet()) {
			Cluster c = new Cluster(pplTable.getValue().getBirthVersionID(),pplTable.getValue().getDeath(),pplTable.getValue().getDeathVersionID(),pplTable.getValue().getDeath(),pplTable.getValue().getTotalChanges());
			c.addTable(pplTable.getValue());
			clusterCollector.addCluster(c);
			
		}
		return clusterCollector;
	}

}
