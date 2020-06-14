package tableClustering.clusterExtractor.commons;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import data.dataPPL.pplSQLSchema.PPLTable;

public class Cluster {

	private int birth;
	private String birthVersion;
	private int death;
	private String deathVersion;
	private int totalChanges=0;
	private TreeMap<String,PPLTable> tables=null;
	
	public Cluster(){
		tables=new TreeMap<String, PPLTable>();
	}
	
	public Cluster(int birth,String birthVersion, int death,String deathVersion, int totalChanges){
		this.birth=birth;
		this.birthVersion=birthVersion;
		this.death=death;
		this.deathVersion=deathVersion;
		this.totalChanges=totalChanges;
		tables=new TreeMap<String, PPLTable>();
	}
	
	public double calculateDistance(Cluster anotherCluster,Double birthWeight, Double deathWeight ,Double changeWeight,int dbDuration){
		double normalizedChangeDistance= Math.abs((this.totalChanges - anotherCluster.totalChanges)/((double)(this.totalChanges + anotherCluster.totalChanges)));
		double normalizedBirthDistance = Math.abs((this.birth-anotherCluster.birth)/(double)dbDuration);
		double normalizedDeathDistance = Math.abs((this.death-anotherCluster.death)/(double)dbDuration);
		double normalizedTotalDistance = changeWeight * normalizedChangeDistance + birthWeight * normalizedBirthDistance + deathWeight * normalizedDeathDistance;
		return normalizedTotalDistance;
	}
	
	public Cluster mergeWithNextCluster(Cluster nextCluster){
		Cluster newCluster = new Cluster();
		newCluster.birth = getMinimum(this.birth,nextCluster.birth);
		newCluster.birthVersion = getMinimumDescription(this.birth, this.birthVersion, nextCluster.birth, nextCluster.birthVersion);
		newCluster.death = getMaximum(this.death,nextCluster.death);
		newCluster.deathVersion = getMaximumDescription(this.death,this.deathVersion, nextCluster.death, nextCluster.deathVersion);
		newCluster.totalChanges = this.totalChanges + nextCluster.totalChanges;
		for (Map.Entry<String,PPLTable> tab : tables.entrySet()) {
			newCluster.addTable(tab.getValue());
		}
		for (Map.Entry<String,PPLTable> tabNext : nextCluster.getTables().entrySet()) {
			newCluster.addTable(tabNext.getValue());
		}
		return newCluster;
	}
	
	private int getMinimum(int firstComparable, int secondComparable)
	{
		if(firstComparable<=secondComparable){
			return firstComparable;
		}
		return secondComparable;
	}
	
	private String getMinimumDescription(int firstComparable, String firstComparableString,
									  int secondComparable, String secondComparableString)
	{
		if(firstComparable<=secondComparable){
			return firstComparableString;
		}
		return secondComparableString;
	}
	
	private int getMaximum(int firstComparable, int secondComparable)
	{
		if(firstComparable>=secondComparable){
			return firstComparable;
		}
		return secondComparable;
	}
	
	private String getMaximumDescription(int firstComparable, String firstComparableString,
									  int secondComparable, String secondComparableString)
	{
		if(firstComparable>=secondComparable){
			return firstComparableString;
		}
		return secondComparableString;
	}
	
	public TreeMap<String,PPLTable> getTables(){
		return tables;
	}
	
	public ArrayList<String> getNamesOfTables(){
		ArrayList<String> tablesNames = new ArrayList<String>();
		for(Map.Entry<String, PPLTable> table:tables.entrySet()){
			tablesNames.add(table.getKey());
		}
		return tablesNames;
	}
	
	public void addTable(PPLTable table){
		this.tables.put(table.getName(), table);
	}
	
	public int getBirth(){
		return this.birth;
	}
	
	public int getDeath(){
		return this.death;
	}
	
	public String getBirthSqlFile(){
		return this.birthVersion;
	}
	
	public String getDeathSqlFile(){
		return this.deathVersion;
	}
	
	public int getTotalChanges(){
		return totalChanges;
	}
	
	public String toString(){
		String toReturn="Cluster";
		toReturn=toReturn+"\t"+this.birth+"\t"+this.death+"\t"+this.totalChanges+"\n";
		
		for(Map.Entry<String, PPLTable> tableset: this.tables.entrySet()){
			toReturn=toReturn+tableset.getKey()+"\t"+tableset.getValue().getBirthVersionID()+"\t"+tableset.getValue().getDeathVersionID()+"\t"+tableset.getValue().getTotalChanges()+"\n";
		}
		return toReturn;
	}
}
