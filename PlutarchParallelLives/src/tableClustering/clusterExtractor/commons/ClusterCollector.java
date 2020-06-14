package tableClustering.clusterExtractor.commons;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ClusterCollector {

	private ArrayList<Cluster> clusters;
	
	public ClusterCollector(){
		clusters=new ArrayList<Cluster>();
	}
	
	public void sortClustersByBirth(){
		 Collections.sort(clusters, new Comparator<Cluster>() {
		        @Override
		        public int compare(final Cluster firstCluster, final Cluster secondCluster) {
		            return Integer.compare(firstCluster.getBirth(),secondCluster.getBirth());
		        }
		       } );
	}
	
	public void sortClustersByDeath(){
		 Collections.sort(clusters, new Comparator<Cluster>() {
		        @Override
		        public int compare(final Cluster firstCluster, final Cluster secondCluster) {
		            return Integer.compare(firstCluster.getDeath(),secondCluster.getDeath());
		        }
		       } );
	}
	
	public void sortClustersByChanges(){
		 Collections.sort(clusters, new Comparator<Cluster>() {
		        @Override
		        public int compare(final Cluster firstCluster, final Cluster secondCluster) {
		            return Integer.compare(firstCluster.getTotalChanges(),secondCluster.getTotalChanges());
		        }
		 });
	}
	
	public void sortClustersByBirthDeath(){
		 Collections.sort(clusters, new Comparator<Cluster>() {
		        @Override
		        public int compare(final Cluster firstCluster, final Cluster secondCluster) {
		        	if (firstCluster.getBirth()<secondCluster.getBirth()) {
						return -1;
					}
		        	if(firstCluster.getBirth()>secondCluster.getBirth()){
		        		return 1;
		        	}
		        	return Integer.compare(firstCluster.getDeath(),secondCluster.getDeath());
		        }
	       } );
	}
	
	
	public void sortClustersByBirthDeathChanges(){
		 Collections.sort(clusters, new Comparator<Cluster>() {
		        @Override
		        public int compare(final Cluster firstCluster, final Cluster secondCluster) {
		        	if (firstCluster.getBirth()<secondCluster.getBirth()) {
						return -1;
					}
		        	if(firstCluster.getBirth()>secondCluster.getBirth()){
		        		return 1;
		        	}
		        	if (firstCluster.getDeath()<secondCluster.getDeath()) {
						return -1;
					}
		        	if(firstCluster.getDeath()>secondCluster.getDeath()){
		        		return 1;
		        	}
		            return Integer.compare(firstCluster.getTotalChanges(),secondCluster.getTotalChanges());
		        }
          } );
	}
	
	public void addCluster(Cluster c){
		this.clusters.add(c);
	}
	
	public ArrayList<Cluster> getClusters(){
		return clusters;
	}
	
	public void setClusters(ArrayList<Cluster> clusters) {
		this.clusters = clusters;
	}
	
	public String toString(){
		String toReturn="";
		for(int i=0; i<this.clusters.size();i++){
			toReturn=toReturn+this.clusters.get(i).toString();
		}
		return toReturn;
	}
}
