package gui.ui;

import java.io.File;
import java.util.ArrayList;
import data.dataKeeper.GlobalDataKeeper;
import data.dataKeeper.PPLFile;
import gui.configurations.DataConfiguration;
import gui.configurations.DataTablesConfiguration;
import gui.configurations.GuiConfiguration;
import gui.tableElements.tableConstructors.TableConstructionIDU;
import gui.tableElements.tableConstructors.TableConstructionWithClusters;
import phaseAnalyzer.engine.PhaseAnalyzerMainEngine;
import services.DataService;
import services.PhasesService;
import services.TableService;
import tableClustering.clusterExtractor.commons.Cluster;
import tableClustering.clusterExtractor.engine.TableClusteringMainEngine;

public class DataGeneratorUI{
	private GlobalDataKeeper globalDataKeeper;
	private PPLFile pplFile;
	private GuiConfiguration guiConfiguration;
	private DataConfiguration dataConfiguration;
	private DataTablesConfiguration dataTableConfiguration;
	private TableConstructionIDU constructedIDUTable;
	private TableConstructionWithClusters clusterTable;
	
	public DataGeneratorUI(GuiConfiguration guiConfiguration,
								DataConfiguration dataConfiguration,
								DataTablesConfiguration dataTableConfiguration) 
	{
		this.guiConfiguration = guiConfiguration;
		this.dataConfiguration = dataConfiguration;
		this.dataTableConfiguration = dataTableConfiguration;
	}

	public void importData(File file){
		initiateLoadState();
		importDataFromFile(file);
		TableService service = new TableService();
		constructedIDUTable = service.createTableConstructionIDU(globalDataKeeper.getAllPPLSchemas(), globalDataKeeper.getAllPPLTransitions());
		
		setIDUPanel();
			
		makeGeneralTableIDU();
		
		createPhaseAnalyserEngine();
		
		dataConfiguration.setNumberOfClusters(14);
		createTableClusteringMainEngine(dataConfiguration.getNumberOfClusters());
		
		fillTable();
		globalDataKeeper.printPPLData();
		fillTree();
	}
	
	public void initiateLoadState(){
		dataConfiguration.setTimeWeight((float)0.5); 
		dataConfiguration.setChangeWeight((float)0.5);
		dataConfiguration.setPreProcessingTime(false);
		dataConfiguration.setPreProcessingChange(false);
	}
	
	public void setIDUPanel(){
		dataConfiguration.setFinalColumnsZoomArea(constructedIDUTable.getConstructedColumns());
		dataConfiguration.setFinalRowsZoomArea(constructedIDUTable.getConstructedRows());
		guiConfiguration.getTabbedPane().setSelectedIndex(0);
		dataConfiguration.setSegmentSizeZoomArea(constructedIDUTable.getSegmentSize());
	}
	
	public void makeGeneralTableIDU(){
		GeneralTableIDUUI widget = new GeneralTableIDUUI (guiConfiguration,dataConfiguration,dataTableConfiguration, globalDataKeeper);
		widget.makeGeneralTableIDU();
		widget.constructGeneralTableIDU();
	}
	
	public void fillTable(){
		if(globalDataKeeper.getPhaseCollectors().size() == 0){
			return;
		}
		constructTableWithClusters();
		
		guiConfiguration.getTabbedPane().setSelectedIndex(0);
		guiConfiguration.getUniformlyDistributedButton().setVisible(true);
		guiConfiguration.getNotUniformlyDistributedButton().setVisible(true);
		
		makeGeneralTablePhases();
		fillClustersTree( globalDataKeeper.getClusterCollectors().get(0).getClusters());
	}
	
	public void constructTableWithClusters(){
		clusterTable = constructClustersTable();
		
		final String[] columns= clusterTable.getConstructedColumns();
		final String[][] rows= clusterTable.getConstructedRows();
		
		dataConfiguration.setSegmentSize(clusterTable.getSegmentSize());
		dataConfiguration.setFinalColumns(columns);
		dataConfiguration.setFinalRows(rows);
		guiConfiguration.setSelectedRows(new ArrayList<Integer>());
	}
	
	public TableConstructionWithClusters constructClustersTable(){
		TableConstructionWithClusters table=new TableConstructionWithClusters(globalDataKeeper);
		table.constructColumns();
		table.constructRows();
		return table;
	}
	
	public void makeGeneralTablePhases(){
		GeneralTablePhasesUI widget = new GeneralTablePhasesUI(guiConfiguration,dataConfiguration,dataTableConfiguration, globalDataKeeper) ;
		widget.initializeGeneralTablePhases();
	}
	
	public void fillTree(){
		TreeConstructionUI treeWidget = new TreeConstructionUI(guiConfiguration,dataTableConfiguration, globalDataKeeper.getAllPPLSchemas());
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
		PhaseAnalyzerMainEngine mainPhaseEngine= myService.createPhaseAnalyserEngine(dataConfiguration,globalDataKeeper,pplFile);
		myService.connectTransitionsWithPhases(mainPhaseEngine, globalDataKeeper.getAllPPLTransitions());
		globalDataKeeper.setPhaseCollectors(mainPhaseEngine.getPhaseCollectors());
	}
	
	public void createTableClusteringMainEngine(int numberOfClusters){
		TableService service = new TableService();
		TableClusteringMainEngine mainClusterEngine = service.createTableClusteringMainEngine(globalDataKeeper, numberOfClusters);
		globalDataKeeper.setClusterCollectors(mainClusterEngine.getClusterCollectors());
	}
	
	public void fillClustersTree(ArrayList<Cluster> clusters){
		TreeConstructionPhasesWithClustersUI clustersTree = new TreeConstructionPhasesWithClustersUI(guiConfiguration, dataTableConfiguration ,clusters);
		clustersTree.fillClustersTree();
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
