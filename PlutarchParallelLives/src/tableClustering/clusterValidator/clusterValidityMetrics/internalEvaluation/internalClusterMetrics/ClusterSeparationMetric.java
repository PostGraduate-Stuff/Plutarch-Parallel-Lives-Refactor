package tableClustering.clusterValidator.clusterValidityMetrics.internalEvaluation.internalClusterMetrics;

import tableClustering.clusterValidator.clusterValidityMetrics.Interface.TotalMetrics;
import tableClustering.clusterValidator.commons.Centroid;

public class ClusterSeparationMetric implements TotalMetrics {

	private Double clusterSeparation = null;
	private Centroid overallCentroid = null;
	private Centroid clusterCentroid = null;

	
	public ClusterSeparationMetric(Centroid clusterCentroid,Centroid overallCentroid){
		this.clusterCentroid=clusterCentroid;
		this.overallCentroid=overallCentroid;
	}
	
	
	@Override
	public void compute() {
				
		clusterSeparation = new Double(0);
		
		Double distanceBirtVersionID = null;
		Double distanceDeathVersionID = null;
		Double distanceTotalChanges = null;

		distanceBirtVersionID= new Double(Math.pow((double)(clusterCentroid.getBirtVersionID() - overallCentroid.getBirtVersionID()),2.0));
		distanceDeathVersionID=new Double(Math.pow((double)(clusterCentroid.getDeathVersionID() - overallCentroid.getDeathVersionID()),2.0));
		distanceTotalChanges=new Double(Math.pow((double)(clusterCentroid.getTotalChanges()-overallCentroid.getTotalChanges()),2.0));

		clusterSeparation=Math.sqrt(distanceBirtVersionID+distanceDeathVersionID+distanceTotalChanges);
	}
	
	@Override
	public Double getResult() {
		return clusterSeparation;
	}

	

}
