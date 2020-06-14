package tableClustering.clusterValidator.clusterValidityMetrics.externalEvaluation.externalClusterMetrics;

import java.util.ArrayList;

import tableClustering.clusterExtractor.commons.Cluster;
import tableClustering.clusterValidator.clusterValidityMetrics.Interface.TotalMetrics;
import tableClustering.clusterValidator.commons.ClassOfObjects;

public class ClusterEntropyMetric implements TotalMetrics {

	private ArrayList<ClassOfObjects> classesOfObjects = new ArrayList<ClassOfObjects>();
	private Double clusterEntropy = new Double(0);
	private ArrayList<Cluster> clusters  = new ArrayList<Cluster>();
	private int classIndexOfThisCluster;
	
	public ClusterEntropyMetric(ArrayList<ClassOfObjects> classesOfObjects, 
								ArrayList<Cluster> clusters,int classIndexOfThisCluster) {
		this.classesOfObjects = classesOfObjects;
		this.clusters = clusters;
		this.classIndexOfThisCluster = classIndexOfThisCluster;
	}
	
	@Override
	public void compute() {
		
		Double tablesize = new Double(0);
		Double itemsInTable = new Double(0);
		Double ratio = new Double(0);
		Double entropy= new Double(0);
		for(int i=0 ; i<this.clusters.size(); i++){
			tablesize = (double)this.clusters.get(i).getTables().size();
			itemsInTable=0.0;
			ratio=0.0;
			ArrayList<String> tablesOfCompared = this.clusters.get(i).getNamesOfTables();
			ClassOfObjects classOfThisCluster = classesOfObjects.get(classIndexOfThisCluster);
			ArrayList<String> objects = classOfThisCluster.getObjects();
			for(int j=0; j<objects.size(); j++){
				if(tablesOfCompared.contains(objects.get(j))){
					itemsInTable++;
				}
			}
			ratio=itemsInTable/tablesize;
			if(tablesize!=0 && itemsInTable!=0){
				entropy = entropy + (ratio * (Math.log(ratio)/Math.log(2.0d)));
			}
		}
		clusterEntropy = -entropy;
	}

	@Override
	public Double getResult() {
		return clusterEntropy;
	}

	
}
