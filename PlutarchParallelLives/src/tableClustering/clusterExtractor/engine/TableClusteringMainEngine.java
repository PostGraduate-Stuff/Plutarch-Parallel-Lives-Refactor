package tableClustering.clusterExtractor.engine;

import java.util.ArrayList;

import tableClustering.clusterExtractor.analysis.AgglomerativeClusterExtractor;
import tableClustering.clusterExtractor.analysis.ClusterExtractor;
import tableClustering.clusterExtractor.commons.ClusterCollector;
import data.dataKeeper.GlobalDataKeeper;

public class TableClusteringMainEngine {
	
	private GlobalDataKeeper dataKeeper;
	private Double birthWeight;
	private Double deathWeight;
	private Double changeWeight;
	private ArrayList<ClusterCollector> clusterCollectors;
	private ClusterExtractor clusterExtractor = new AgglomerativeClusterExtractor();
	private ArrayList<ClusterCollector> allClusterCollectors = new ArrayList<ClusterCollector>();

	public TableClusteringMainEngine(GlobalDataKeeper dataKeeper,Double birthWeight, Double deathWeight,
			Double changeWeight){
		
		this.dataKeeper=dataKeeper;
		this.birthWeight=birthWeight;
		this.deathWeight=deathWeight;
		this.changeWeight=changeWeight;


	}
	
	public void extractClusters(int numClusters){
		
		clusterCollectors = new ArrayList<ClusterCollector>();
		ClusterCollector clusterCollector = new ClusterCollector();
		clusterCollector = clusterExtractor.extractAtMostKClusters(dataKeeper, numClusters, birthWeight, deathWeight, changeWeight);
		clusterCollector.sortClustersByBirthDeath();
		clusterCollectors.add(clusterCollector);
		
		allClusterCollectors.add(clusterCollector);

	}
	
	public void print(){
		
		String toPrint="";
		
		for(int i=0; i<allClusterCollectors.size(); i++){
			ClusterCollector clusterCollector = allClusterCollectors.get(i);
			toPrint = toPrint+clusterCollector.toString();	
		}
		
		System.out.println(toPrint);
	}
	
	public ArrayList<ClusterCollector> getClusterCollectors(){
		return allClusterCollectors;
	}
	
}
