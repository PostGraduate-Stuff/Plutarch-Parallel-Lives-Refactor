package tableClustering.clusterValidator.clusterValidityMetrics.internalEvaluation.internalClusterMetrics;

import java.util.Map;
import java.util.TreeMap;

import tableClustering.clusterExtractor.commons.Cluster;
import tableClustering.clusterValidator.clusterValidityMetrics.Interface.TotalMetrics;
import tableClustering.clusterValidator.commons.Centroid;
import tableClustering.clusterValidator.commons.ClusterInfoKeeper;
import data.dataPPL.pplSQLSchema.PPLTable;

public class ClusterCohesionMetric implements TotalMetrics {
	
	ClusterInfoKeeper clusterInfoKeeper = null;
	private Double sumClusterCohesion = null;
	
	public ClusterCohesionMetric(ClusterInfoKeeper clusterInfoKeeper){
		this.clusterInfoKeeper=clusterInfoKeeper;
	}
	
	@Override
	public void compute() {
		Cluster currentCluster = clusterInfoKeeper.getCluster();
		TreeMap<String, PPLTable> currentClusterTables = currentCluster.getTables();
		Centroid clusterCentroid = clusterInfoKeeper.getCentroid();
		
		sumClusterCohesion = new Double(0);
		
		for(Map.Entry<String,PPLTable> pplTable:currentClusterTables.entrySet()){
			sumClusterCohesion = sumClusterCohesion+computeDistanceFromDataPointToCentroid(pplTable.getValue(),clusterCentroid);
		}
		
	}
	
	private Double computeDistanceFromDataPointToCentroid(PPLTable tableToComputeDistance, Centroid centroidOfCluster){
		
		Double distance = null;
		
		Double distanceBirthVersionID = null;
		Double distanceDeathVersionID = null;
		Double distanceTotalChanges = null;

		distanceBirthVersionID=Math.pow((double)(tableToComputeDistance.getBirthVersionID()-centroidOfCluster.getBirtVersionID()),2.0);
		distanceDeathVersionID=Math.pow((double)(tableToComputeDistance.getDeathVersionID()-centroidOfCluster.getDeathVersionID()),2.0);
		distanceTotalChanges=Math.pow((double)(tableToComputeDistance.getTotalChanges()-centroidOfCluster.getTotalChanges()),2.0);

		distance=Math.sqrt(distanceBirthVersionID+distanceDeathVersionID+distanceTotalChanges);
		
		return distance;
		
	}
	
	@Override
	public Double getResult(){
		return sumClusterCohesion;
	}

}
