package tableClustering.clusterValidator.clusterValidityMetrics.externalEvaluation.externalClusterMetrics;

import java.util.ArrayList;

import tableClustering.clusterExtractor.commons.Cluster;
import tableClustering.clusterValidator.clusterValidityMetrics.Interface.TotalMetrics;
import tableClustering.clusterValidator.commons.ClassOfObjects;

public class ClusterPrecisionMetric implements TotalMetrics {
	
	private Cluster currentCluster = new Cluster();
	private ClassOfObjects classOfObjects=null;
	private Double precision = new Double(0);

	
	public ClusterPrecisionMetric(Cluster currentCluster,ClassOfObjects classOfObjects) {
		this.currentCluster = currentCluster;
		this.classOfObjects = classOfObjects;
	}
	
	@Override
	public void compute() {

		Double tableSize = new Double(0);
		Double itemsInTable = new Double(0);
		
		tableSize = (double)currentCluster.getTables().size();
		itemsInTable=0.0;
		
		ArrayList<String> tablesToCompare = currentCluster.getNamesOfTables();
		ArrayList<String> objects = classOfObjects.getObjects();
		
		for(int j=0; j<objects.size(); j++){
			if(tablesToCompare.contains(objects.get(j))){
				itemsInTable++;
			}
		}
		
		precision=itemsInTable/tableSize;
		
	}

	@Override
	public Double getResult() {
		return precision;
	}

}
