package tableClustering.clusterValidator.clusterValidityMetrics.internalEvaluation.internalTotalMetrics;

import java.util.ArrayList;
import java.util.Iterator;

import tableClustering.clusterValidator.clusterValidityMetrics.Interface.TotalMetrics;
import tableClustering.clusterValidator.commons.ClusterInfoKeeper;

public class TotalSeparationMetric implements TotalMetrics {

	private ArrayList<ClusterInfoKeeper> clusterInfoKeepers = new ArrayList<ClusterInfoKeeper>();
	private Double totalSeparation=null;
	
	public TotalSeparationMetric(ArrayList<ClusterInfoKeeper> clusterInfoKeepers) {
		this.clusterInfoKeepers=clusterInfoKeepers;
	}
	
	@Override
	public void compute(){
		Iterator<ClusterInfoKeeper> iteratorClusterInfoKeeper = clusterInfoKeepers.iterator();
		totalSeparation = new Double(0);
 		while(iteratorClusterInfoKeeper.hasNext()){
			ClusterInfoKeeper currentClusterInfoKeeper = iteratorClusterInfoKeeper.next();
			totalSeparation= totalSeparation + currentClusterInfoKeeper.getClusterSeparation();
		}
		System.err.println("Total Separation"+totalSeparation);
	}
	
	@Override
	public Double getResult() {
		return totalSeparation;
	}
}
