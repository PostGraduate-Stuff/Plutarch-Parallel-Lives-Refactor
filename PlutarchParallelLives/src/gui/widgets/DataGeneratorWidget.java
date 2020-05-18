package gui.widgets;

import java.io.File;
import java.util.ArrayList;
import data.dataKeeper.GlobalDataKeeper;
import data.dataKeeper.PPLFile;
import gui.configurations.Configuration;
import gui.tableElements.tableConstructors.TableConstructionIDU;
import gui.tableElements.tableConstructors.TableConstructionWithClusters;
import phaseAnalyzer.engine.PhaseAnalyzerMainEngine;
import services.DataService;
import services.PhasesService;
import services.TableService;
import tableClustering.clusterExtractor.commons.Cluster;
import tableClustering.clusterExtractor.engine.TableClusteringMainEngine;

public class DataGeneratorWidget{
	private GlobalDataKeeper globalDataKeeper;
	private PPLFile pplFile;
	private Configuration configuration;
	private TableConstructionIDU constructedIDUTable;
	private TableConstructionWithClusters clusterTable;
	
	public DataGeneratorWidget(Configuration configuration) {
		this.configuration = configuration;
	}

	public void importData(File file){
		initiateLoadState();
		importDataFromFile(file);
		TableService service = new TableService();
		constructedIDUTable = service.createTableConstructionIDU(globalDataKeeper.getAllPPLSchemas(), globalDataKeeper.getAllPPLTransitions());
		
		setIDUPanel();
			
		makeGeneralTableIDU();
		
		createPhaseAnalyserEngine();
		
		configuration.setNumberOfClusters(14);
		createTableClusteringMainEngine(configuration.getNumberOfClusters());
		
		fillTable();
		printPPLData();
		fillTree();
	}
	
	public void initiateLoadState(){
		configuration.setTimeWeight((float)0.5); 
		configuration.setChangeWeight((float)0.5);
		configuration.setPreProcessingTime(false);
		configuration.setPreProcessingChange(false);
	}
	
	public void setIDUPanel(){
		configuration.setFinalColumnsZoomArea(constructedIDUTable.getConstructedColumns());
		configuration.setFinalRowsZoomArea(constructedIDUTable.getConstructedRows());
		configuration.getTabbedPane().setSelectedIndex(0);
		configuration.setSegmentSizeZoomArea(constructedIDUTable.getSegmentSize());
	}
	
	public void makeGeneralTableIDU(){
		GeneralTableIDUWidget widget = new GeneralTableIDUWidget(configuration, globalDataKeeper);
		widget.makeGeneralTableIDU();
		widget.constructGeneralTableIDU();
	}
	
	public void fillTable(){
		if(globalDataKeeper.getPhaseCollectors().size() == 0){
			return;
		}
		constructTableWithClusters();
		
		configuration.getTabbedPane().setSelectedIndex(0);
		configuration.getUniformlyDistributedButton().setVisible(true);
		configuration.getNotUniformlyDistributedButton().setVisible(true);
		
		makeGeneralTablePhases();
		fillClustersTree( globalDataKeeper.getClusterCollectors().get(0).getClusters());
	}
	
	public void constructTableWithClusters(){
		clusterTable = constructClustersTable();
		
		final String[] columns= clusterTable.getConstructedColumns();
		final String[][] rows= clusterTable.getConstructedRows();
		
		configuration.setSegmentSize(clusterTable.getSegmentSize());
		configuration.setFinalColumns(columns);
		configuration.setFinalRows(rows);
		configuration.setSelectedRows(new ArrayList<Integer>());
	}
	
	public TableConstructionWithClusters constructClustersTable(){
		TableConstructionWithClusters table=new TableConstructionWithClusters(globalDataKeeper);
		table.constructColumns();
		table.constructRows();
		return table;
	}
	
	public void makeGeneralTablePhases(){
		GeneralTablePhasesWidget widget = new GeneralTablePhasesWidget(configuration, globalDataKeeper) ;
		widget.initializeGeneralTablePhases();
	}
	
	public void printPPLData(){
		System.out.println("Schemas:"+globalDataKeeper.getAllPPLSchemas().size());
		System.out.println("Transitions:"+globalDataKeeper.getAllPPLTransitions().size());
		System.out.println("Tables:"+globalDataKeeper.getAllPPLTables().size());
	}
	
	public void fillTree(){
		TreeConstructionWidget treeWidget = new TreeConstructionWidget(configuration, globalDataKeeper.getAllPPLSchemas());
		treeWidget.fillTree();
	}

	public void setGlobalData(){
		globalDataKeeper=new GlobalDataKeeper(pplFile.getDatasetTxt(), pplFile.getTransitionsFile());
		globalDataKeeper.setData();
		System.out.println(globalDataKeeper.getAllPPLTables().size());
        System.out.println(pplFile.getFile().toString());
	}
	
	public void importDataFromFile(File file){
		readFile(file);
		DataService service = new DataService();
		globalDataKeeper= service.initiateGlobalData(pplFile.getDatasetTxt(), pplFile.getTransitionsFile());
	}

	
	public void readFile(File file){
		DataService service = new DataService();
		pplFile = service.readFile(file);
	}
	
	public void createPhaseAnalyserEngine(){
		PhasesService myService = new PhasesService();
		PhaseAnalyzerMainEngine mainPhaseEngine= myService.createPhaseAnalyserEngine(configuration,globalDataKeeper,pplFile);
		myService.connectTransitionsWithPhases(mainPhaseEngine, globalDataKeeper.getAllPPLTransitions());
		globalDataKeeper.setPhaseCollectors(mainPhaseEngine.getPhaseCollectors());
	}
	
	public void createTableClusteringMainEngine(int numberOfClusters){
		TableService service = new TableService();
		TableClusteringMainEngine mainClusterEngine = service.createTableClusteringMainEngine(globalDataKeeper, numberOfClusters);
		globalDataKeeper.setClusterCollectors(mainClusterEngine.getClusterCollectors());
	}
	
	public void fillClustersTree(ArrayList<Cluster> clusters){
		TreeConstructionPhasesWithClustersWidget clustersTree = new TreeConstructionPhasesWithClustersWidget(configuration,clusters);
		clustersTree.fillClustersTree();
	}
	
	public Configuration getConfiguration(){
		return configuration;
	}

	public GlobalDataKeeper getGlobalDataKeeper() {
		return globalDataKeeper;
	}

	public PPLFile getPplFile() {
		return pplFile;
	}

	public TableConstructionIDU getConstructedIDUTable() {
		return constructedIDUTable;
	}

	public TableConstructionWithClusters getClusterTable() {
		return clusterTable;
	}

	public void setPplFile(PPLFile pplFile) {
		this.pplFile = pplFile;
	}
	
	public void setConstructedIDUTable(TableConstructionIDU constructedIDUTable) {
		this.constructedIDUTable = constructedIDUTable;
	}
}
