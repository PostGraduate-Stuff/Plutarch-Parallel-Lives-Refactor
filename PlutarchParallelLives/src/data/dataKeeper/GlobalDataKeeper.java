package data.dataKeeper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.TreeMap;

import phaseAnalyzer.commons.PhaseCollector;
import tableClustering.clusterExtractor.commons.ClusterCollector;
import data.dataPPL.pplSQLSchema.PPLSchema;
import data.dataPPL.pplSQLSchema.PPLTable;
import data.dataPPL.pplTransition.AtomicChange;
import data.dataPPL.pplTransition.PPLTransition;
import data.dataPPL.pplTransition.TableChange;
import data.dataProccessing.Worker;

public class GlobalDataKeeper {

	private TreeMap<String,PPLSchema> allPPLSchemas  = new TreeMap<String,PPLSchema>();
	private TreeMap<String,PPLTable> allTables = new  TreeMap<String,PPLTable>();
	private ArrayList<AtomicChange> atomicChanges = new ArrayList<AtomicChange>();
	private TreeMap<String,TableChange> tableChanges = new TreeMap<String,TableChange>();
	private TreeMap<String,TableChange> tableChangesForTwo = new TreeMap<String,TableChange>();
	private TreeMap<Integer,PPLTransition> allPPLTransitions = new TreeMap<Integer,PPLTransition>();
	private ArrayList<PhaseCollector> phaseCollectors = new ArrayList<PhaseCollector>();
	private ArrayList<ClusterCollector> clusterCollectors = new ArrayList<ClusterCollector>();

	private String 	projectDataFolder=null;
	private String filename=null;
	private String transitionsFile="";
	

	public GlobalDataKeeper(String fileName,String transitionsFile){
		this.filename = fileName;
		this.transitionsFile = transitionsFile;
	}
	
	public GlobalDataKeeper(){
	}
	
	public void setData(){
		
		Worker worker = new Worker(filename,transitionsFile);
		try {
			worker.work();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		setAllPPLSchemas(worker.getAllPPLSchemas());
		setAllPPLTables(worker.getAllPPLTables());
		setAllPPLTransitions(worker.getAllPPLTransitions());
		setAllTableChanges(worker.getAllTableChanges());
		setAtomicChanges(worker.getAtomicChanges());
		setDataFolder(worker.getDataFolder());
		
	}
	
	public void printPPLData(){
		System.out.println("Schemas:"+allPPLSchemas.size());
		System.out.println("Transitions:"+allPPLTransitions.size());
		System.out.println("Tables:"+allTables.size());
	}
	
	public void setPhaseCollectors(ArrayList<PhaseCollector> phaseCollectors){
		this.phaseCollectors=phaseCollectors;
	}
	
	public void setClusterCollectors(ArrayList<ClusterCollector> clusterCollectors){
		this.clusterCollectors=clusterCollectors;
	}
	
	private void setAllPPLSchemas(TreeMap<String,PPLSchema> tmpAllPPLSchemas){
		allPPLSchemas=tmpAllPPLSchemas;	
	}
	
	private void setAllPPLTables(TreeMap<String,PPLTable> tmpAllTables){
		 allTables=tmpAllTables;
	}
	
	private void setAtomicChanges(ArrayList<AtomicChange> tmpAtomicChanges){
		 atomicChanges=tmpAtomicChanges;	
	}
	
	private void setAllTableChanges(TreeMap<String,TableChange> tmpTableChanges){
		 tableChanges=tmpTableChanges;
	}
	
	
	private void setAllPPLTransitions(TreeMap<Integer,PPLTransition> tmpAllPPLTransitions){
		 allPPLTransitions=tmpAllPPLTransitions;
	}
	
	private void setDataFolder(String tmpProjectDataFolder){
		 projectDataFolder=tmpProjectDataFolder;
	}
	
	public TreeMap<String,PPLSchema> getAllPPLSchemas(){		
		return allPPLSchemas;
	}
	
	public TreeMap<String,PPLTable> getAllPPLTables(){
		return allTables;
	}
	
	public ArrayList<AtomicChange> getAtomicChanges(){
		return atomicChanges;
	}
	
	public TreeMap<String,TableChange> getAllTableChanges(){
		return tableChanges;
	}
	
	public TreeMap<String,TableChange> getTmpTableChanges(){
		return tableChangesForTwo;
	}
	
	public TreeMap<Integer,PPLTransition> getAllPPLTransitions(){
		return allPPLTransitions;
	}
	
	public String getDataFolder(){
		return projectDataFolder;
	}
	
	public ArrayList<PhaseCollector> getPhaseCollectors(){
		return this.phaseCollectors;
	}
	
	public ArrayList<ClusterCollector> getClusterCollectors(){
		return this.clusterCollectors;
	}
}
