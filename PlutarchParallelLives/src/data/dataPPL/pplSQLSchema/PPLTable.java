package data.dataPPL.pplSQLSchema;

import gr.uoi.cs.daintiness.hecate.sql.Table;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import data.dataPPL.pplTransition.AtomicChange;
import data.dataPPL.pplTransition.TableChange;

public class PPLTable {

	private int age;
	private int totalChanges;
	private int currentChanges;
	private HashMap<String,Integer> coChanges;
	private HashMap<String,Integer> sequenceCoChanges;
	private HashMap<String,Integer> windowCoChanges;
	private ArrayList<Integer> changesForChart = new ArrayList<Integer>();
	private TableChange tableChanges;
	private Table hecTable;
	private TreeMap<String, PPLAttribute> attributes;
	private String name="";
	private String birth=null;
	private int birthVersionID;
	private String death=null;
	private int deathVersionID;
	private boolean active=false;

	
	public PPLTable(String tmpName,Table tmpHecTable){
		hecTable=tmpHecTable;
		name=tmpName;
		this.attributes = new TreeMap<String, PPLAttribute>();
	}
	
	public PPLTable(){
	}
	
	public void setBirth(String birth){
		this.birth=birth;
	}
	
	public void setBirthVersionID(int birthID){
		birthVersionID=birthID;
	}
	
	public void setDeath(String death){
		this.death=death;
	}
	
	public void setDeathVersionID(int deathID){
		deathVersionID=deathID;
	}
	
	public void setActive(){
		this.active=!this.active;
	}
	
	public boolean getActive(){
		return this.active;
	}
	
	public String getBirth(){
		return this.birth;
	}
	
	public int getBirthVersionID(){
		return birthVersionID;
	}
	
	public int getDeathVersionID(){
		return deathVersionID;
	}
	
	public String getDeath(){
		return this.death;
	}
	
	public void addAttribute(PPLAttribute attribute) {
		this.attributes.put(attribute.getName(), attribute);
	}
	
	public TreeMap<String, PPLAttribute> getAttributes() {
		return this.attributes;
	}
	
	public String getName(){
		return name;
	}
	
	public int getTotalChanges(){
		return totalChanges;
	}
	
	public TableChange getTableChanges(){
		return tableChanges;
	}

	public void setTableChanges(TableChange tempTableChanges){
		tableChanges=tempTableChanges;
	}
	
	public void setTotalChanges(){
		TreeMap<Integer,ArrayList<AtomicChange>> totalchanges=tableChanges.getTableAtomicChanges();
		for(Map.Entry<Integer, ArrayList<AtomicChange>> change:totalchanges.entrySet()){
			totalChanges=totalChanges+change.getValue().size();
		}
	}
	
	public int getTotalChangesForOnePhase(int startPosition,int LastPosition){
		
		int counter=0;
		for(int i=startPosition;i<=LastPosition;i++){
			if(tableChanges.getTableAtomicChangeForOneTransition(i)!=null){
				counter=counter+tableChanges.getTableAtomicChangeForOneTransition(i).size();
			}
		}
		return(counter);
	}
	
	public int getNumberOfAdditionsForOneTransition(Integer transition){
		return tableChanges.getNumberOfActionForOneTransition(transition, "Addition");
	}
	
	public int getNumberOfDeletionsForOneTransition(Integer transition){
		return tableChanges.getNumberOfActionForOneTransition(transition, "Deletion");
	}
	
	public int getNumberOfUpdatesForOneTransition(Integer transition){
		return tableChanges.getNumberOfActionForOneTransition(transition, "Change");
	}
}
