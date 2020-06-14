package tableClustering.clusterValidator.commons;

public class Centroid {

	private double birtVersionID;
	private double deathVersionID;
	private double totalChanges;
	
	public Centroid(double x,double y, double z){
		this.birtVersionID = x;
		this.deathVersionID = y;
		this.totalChanges = z;
	}
	
	public double getBirtVersionID(){
		return birtVersionID;
	}
	
	public double getDeathVersionID(){
		return deathVersionID;
	}
	
	public double getTotalChanges(){
		return totalChanges;
	}
	
}
