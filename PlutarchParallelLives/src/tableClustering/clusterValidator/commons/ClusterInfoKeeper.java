package tableClustering.clusterValidator.commons;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import data.dataPPL.pplSQLSchema.PPLTable;
import tableClustering.clusterExtractor.commons.Cluster;
import tableClustering.clusterValidator.clusterValidityMetrics.Interface.TotalMetrics;
import tableClustering.clusterValidator.clusterValidityMetrics.externalEvaluation.externalClusterMetrics.ClusterEntropyMetric;
import tableClustering.clusterValidator.clusterValidityMetrics.externalEvaluation.externalClusterMetrics.ClusterFMeasureMetric;
import tableClustering.clusterValidator.clusterValidityMetrics.externalEvaluation.externalClusterMetrics.ClusterPrecisionMetric;
import tableClustering.clusterValidator.clusterValidityMetrics.externalEvaluation.externalClusterMetrics.ClusterRecallMetric;
import tableClustering.clusterValidator.clusterValidityMetrics.internalEvaluation.internalClusterMetrics.ClusterCohesionMetric;
import tableClustering.clusterValidator.clusterValidityMetrics.internalEvaluation.internalClusterMetrics.ClusterSeparationMetric;


public class ClusterInfoKeeper {
	
	private Cluster cluster = new Cluster();
	private Centroid clusterCentroid=null;
	private Centroid overallCentroid=null;
	private Double clusterCohesion = null;
	private Double clusterSeparation = null;
	private Double clusterEntropy = null;
	private ArrayList<Double> precisions = new ArrayList<Double>();
	private ArrayList<Double> recalls = new ArrayList<Double>();
	private ArrayList<Double> fMeasures = new ArrayList<Double>();


	
	public ClusterInfoKeeper(Cluster cluster,Centroid overallCentroid){
		this.cluster=cluster;
		this.overallCentroid=overallCentroid;
		initialize();
	}
	
	
	private void initialize(){
		
		initializeCentroid();
		computeClusterCohesion();
		computeClusterSeparation();		
		
	}
	
	private void initializeCentroid(){
		
		TreeMap<String, PPLTable> tables = this.cluster.getTables();
		double birtVersionID = 0;
		double deathVersionID = 0;
		double totalChanges = 0;
		for(Map.Entry<String,PPLTable> pplTab:tables.entrySet()){
			birtVersionID = birtVersionID + pplTab.getValue().getBirthVersionID();
			deathVersionID = deathVersionID + pplTab.getValue().getDeathVersionID();
			totalChanges = totalChanges + pplTab.getValue().getTotalChanges();
		}
		
		birtVersionID = birtVersionID/tables.size();
		deathVersionID = deathVersionID/tables.size();
		totalChanges = totalChanges/tables.size();
		
		this.clusterCentroid=new Centroid(birtVersionID, deathVersionID, totalChanges);
				
	}
	
	private void computeClusterCohesion(){
		
		TotalMetrics cohesionMetricCalculator = new ClusterCohesionMetric(this);
		cohesionMetricCalculator.compute();
		clusterCohesion=cohesionMetricCalculator.getResult();
		
	}
	
	private void computeClusterSeparation(){
		
		TotalMetrics separationMetricCalculator = new ClusterSeparationMetric(clusterCentroid,overallCentroid);
		separationMetricCalculator.compute();
		clusterSeparation=(double)this.cluster.getTables().size()*separationMetricCalculator.getResult();
		
	}
	
	public void computeClusterEntropy(ArrayList<ClassOfObjects> classesOfObjects,ArrayList<Cluster> clusters,int classIndex){
		
		TotalMetrics entropyMetricCalculator = new ClusterEntropyMetric(classesOfObjects,clusters,classIndex);
		entropyMetricCalculator.compute();
		clusterEntropy = entropyMetricCalculator.getResult();
		
	}
	
	public void computeClusterPrecision(ArrayList<ClassOfObjects> classesOfObjects){
		
		TotalMetrics precisionMetricCalculator;
		for(int i=0; i<classesOfObjects.size(); i++){
			precisionMetricCalculator = new ClusterPrecisionMetric(this.cluster,classesOfObjects.get(i));
			precisionMetricCalculator.compute();
			precisions.add(precisionMetricCalculator.getResult());
		}
		
	}
	
	public void computeClusterRecall(ArrayList<ClassOfObjects> classesOfObjects){
		
		TotalMetrics recallMetricCalculator;
		for(int i=0; i<classesOfObjects.size(); i++){
			recallMetricCalculator = new ClusterRecallMetric(this.cluster,classesOfObjects.get(i));
			recallMetricCalculator.compute();
			recalls.add(recallMetricCalculator.getResult());
		}
				
	}
	
	public void computeClusterFMeasure(){
		
		TotalMetrics fMeasureMetricCalculator;
		for(int i=0; i<precisions.size(); i++){
			fMeasureMetricCalculator = new ClusterFMeasureMetric(precisions.get(i),recalls.get(i));
			fMeasureMetricCalculator.compute();
			fMeasures.add(fMeasureMetricCalculator.getResult());
		}
				
	}
	
	public Cluster getCluster(){
		return this.cluster;
	}
	
	public Centroid getCentroid(){
		return this.clusterCentroid;
	}
	
	public Double getClusterCohesion() {
		return clusterCohesion;
	}
	
	public Double getClusterSeparation() {
		return clusterSeparation;
	}
	
	public Double getClusterEntropy() {
		return clusterEntropy;
	}

	public ArrayList<Double> getPrecisions(){
		return precisions;
	}
	
	public ArrayList<Double> getRecalls(){
		return recalls;
	}
	
	public ArrayList<Double> getFmeasures(){
		return fMeasures;
	}
	
}
