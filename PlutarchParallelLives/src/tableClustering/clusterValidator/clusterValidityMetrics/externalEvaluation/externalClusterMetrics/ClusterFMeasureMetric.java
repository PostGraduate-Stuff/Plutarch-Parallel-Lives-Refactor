package tableClustering.clusterValidator.clusterValidityMetrics.externalEvaluation.externalClusterMetrics;

import tableClustering.clusterValidator.clusterValidityMetrics.Interface.TotalMetrics;

public class ClusterFMeasureMetric implements TotalMetrics {

	private Double precision = new Double(0);
	private Double recall = new Double(0);
	private Double measureResult = new Double(0);

	public ClusterFMeasureMetric(Double precision, Double recall) {
		this.precision = precision;
		this.recall = recall;
	}
	
	@Override
	public void compute() {
		Double numerator = new Double(2*precision*recall);
		Double denominator = new Double(precision+recall);
		if(numerator!=0 && denominator!=0)
			measureResult = numerator/denominator;
	}

	@Override
	public Double getResult() {
		return measureResult;
	}

}
