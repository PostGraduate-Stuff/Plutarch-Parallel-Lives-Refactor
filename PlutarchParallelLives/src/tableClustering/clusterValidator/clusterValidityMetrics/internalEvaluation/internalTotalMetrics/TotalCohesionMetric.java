package tableClustering.clusterValidator.clusterValidityMetrics.internalEvaluation.internalTotalMetrics;

import java.util.ArrayList;
import java.util.Iterator;

import tableClustering.clusterValidator.clusterValidityMetrics.Interface.TotalMetrics;
import tableClustering.clusterValidator.commons.ClusterInfoKeeper;

public class TotalCohesionMetric implements TotalMetrics {

	private ArrayList<ClusterInfoKeeper> clusterInfoKeepers = new ArrayList<ClusterInfoKeeper>();
	private Double totalCohesion=null;
	
	public TotalCohesionMetric(ArrayList<ClusterInfoKeeper> clusterInfoKeepers) {
		this.clusterInfoKeepers=clusterInfoKeepers;
	}
	
	@Override
	public void compute(){
		Iterator<ClusterInfoKeeper> iteratorClusterInfoKeeper = clusterInfoKeepers.iterator();
		totalCohesion = new Double(0);
 		while(iteratorClusterInfoKeeper.hasNext()){
			ClusterInfoKeeper currentClusterInfoKeeper = iteratorClusterInfoKeeper.next();
			totalCohesion= totalCohesion + currentClusterInfoKeeper.getClusterCohesion();
		}
		System.err.println("Total Cohesion:"+totalCohesion);
	}
	
	@Override
	public Double getResult() {
		return this.totalCohesion;
	}

}
