package services;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;

import data.dataKeeper.GlobalDataKeeper;
import data.dataSorters.PldRowSorter;
import gui.configurations.Configuration;
import gui.tableElements.commons.JvTable;
import gui.tableElements.tableConstructors.TableConstructionIDU;
import gui.tableElements.tableConstructors.TableConstructionWithClusters;
import gui.treeElements.TreeConstructionGeneral;
import gui.treeElements.TreeConstructionPhasesWithClusters;
import phaseAnalyzer.commons.Phase;
import phaseAnalyzer.engine.PhaseAnalyzerMainEngine;
import tableClustering.clusterExtractor.commons.Cluster;
import tableClustering.clusterExtractor.engine.TableClusteringMainEngine;

public class GeneralDatakeeper {

	private PPLFile pplFile;
	private TableConstructionIDU contructedTable;
	private GlobalDataKeeper globalDataKeeper;
	protected DataService service;
	
	
	
	public void setGlobalData(Configuration configuration){
		try{
			globalDataKeeper=new GlobalDataKeeper(pplFile.getDatasetTxt(), pplFile.getTransitionsFile());
			globalDataKeeper.setData();
			System.out.println(globalDataKeeper.getAllPPLTables().size());
			
	        System.out.println(pplFile.getFile().toString());

		}
		catch(Exception ex){
			
		}
	}
	
	public void importDataFromFile(File file)
	{
		this.pplFile =readFile(file);
		this.globalDataKeeper= service.setGlobalData(pplFile.getDatasetTxt(), pplFile.getTransitionsFile());
		
	}
	
	public PPLFile readFile(File file)
	{
		service = new DataService();
		return service.readFile(file);
		
	}
	
	public void createTableConstructionIDU()
	{
		TableConstructionIDU constructedTable = service.createTableConstructionIDU(getGlobalDataKeeper().getAllPPLSchemas(),
																				   getGlobalDataKeeper().getAllPPLTransitions());
		
		setTableConstructionIDU(constructedTable);
		
	}
	
	public String[][] sortRows(String[][] finalRows)
	{
		PldRowSorter sorter=new PldRowSorter();
		return sorter.sortRows(finalRows,globalDataKeeper.getAllPPLTables());
	}
	
	public void createPhaseAnalyserEngine(Configuration configuration)
	{
		PhaseAnalyzerMainEngine mainPhaseEngine= service.createPhaseAnalyserEngine(configuration,globalDataKeeper,pplFile);
		service.connectTransitionsWithPhases(mainPhaseEngine, globalDataKeeper.getAllPPLTransitions());
		globalDataKeeper.setPhaseCollectors(mainPhaseEngine.getPhaseCollectors());
	}
	
	
	public void createTableClusteringMainEngine(int numberOfClusters)
	{
		TableClusteringMainEngine mainClusterEngine = service.createTableClusteringMainEngine(globalDataKeeper, numberOfClusters);
		globalDataKeeper.setClusterCollectors(mainClusterEngine.getClusterCollectors());
		
	}
	
	public JvTable makeGeneralTableIDU(Configuration configuration) 
	{
		ArrayList<Phase> initialPhases = new ArrayList<>();
		if(globalDataKeeper.getPhaseCollectors() != null  && 
				globalDataKeeper.getPhaseCollectors().size() >0 && 
				globalDataKeeper.getPhaseCollectors().get(0) != null)
		{
			initialPhases = globalDataKeeper.getPhaseCollectors().get(0).getPhases();
		}
		return service.makeGeneralTableIDU(configuration, initialPhases);
	}
	
	public TableConstructionWithClusters constructTableWithClusters() 
	{
		TableConstructionWithClusters table=new TableConstructionWithClusters(globalDataKeeper);
		
		table.constructColumns();
		table.constructRows();
		
		return table;
	}
	
	public JvTable makeGeneralTablePhases(Configuration configuration)
	{
		JvTable table = service.makeGeneralTablePhases(configuration);
		return table;
	}
	
	
	
	public PPLFile getPPLFile()
	{
		return pplFile;
	}
	
	public TableConstructionIDU getTableConstructionIDU(){
		return contructedTable;
	}

	public PPLFile getPplFile() {
		return pplFile;
	}

	public void setPplFile(PPLFile pplFile) {
		this.pplFile = pplFile;
	}

	public GlobalDataKeeper getGlobalDataKeeper() {
		return globalDataKeeper;
	}

	public void setGlobalDataKeeper(GlobalDataKeeper globalDataKeeper) {
		this.globalDataKeeper = globalDataKeeper;
	}
	
	public void setTableConstructionIDU(TableConstructionIDU contructedTable)
	{
		this.contructedTable = contructedTable;
	}

}
