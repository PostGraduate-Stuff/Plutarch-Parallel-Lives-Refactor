package gui.widgets;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;

import data.dataKeeper.GlobalDataKeeper;
import data.dataKeeper.PPLFile;
import gui.configurations.Configuration;
import gui.mainEngine.Gui;
import gui.tableElements.tableConstructors.TableConstructionIDU;
import gui.tableElements.tableConstructors.TableConstructionWithClusters;
import gui.treeElements.TreeConstructionPhasesWithClusters;
import phaseAnalyzer.engine.PhaseAnalyzerMainEngine;
import services.DataService;
import services.PhasesService;
import services.TableService;
import tableClustering.clusterExtractor.commons.Cluster;
import tableClustering.clusterExtractor.engine.TableClusteringMainEngine;

public class DataGenerator
{

	private GlobalDataKeeper globalDataKeeper;
	private PPLFile pplFile;
	private Configuration configuration;
	private TableConstructionIDU constructedIDUTable;
	private TableConstructionWithClusters clusterTable;
	
	public DataGenerator(Configuration configuration) {
		this.configuration = configuration;
	}


	public void initiateLoadState()
	{
		configuration.setTimeWeight((float)0.5); 
		configuration.setChangeWeight((float)0.5);
		configuration.setPreProcessingTime(false);
		configuration.setPreProcessingChange(false);
		
		
	}
	public void makeGeneralTableIDU()
	{
		GeneralTableIDUWidget widget = new GeneralTableIDUWidget(configuration, globalDataKeeper);
		widget.makeGeneralTableIDU();
		widget.constructGeneralTableIDU();
	}
	
	
	public void importData(File file)
	{
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
	
	
	
	
	public void setIDUPanel()
	{
		configuration.setFinalColumnsZoomArea(constructedIDUTable.getConstructedColumns());
		configuration.setFinalRowsZoomArea(constructedIDUTable.getConstructedRows());
		configuration.getTabbedPane().setSelectedIndex(0);
		configuration.setSegmentSizeZoomArea(constructedIDUTable.getSegmentSize());
	}
	
	
	
	
	
	
	public void fillTable() 
	{
		if(globalDataKeeper.getPhaseCollectors().size()==0)
		{
			return;
		}
		constructTableWithClusters();
		
		configuration.getTabbedPane().setSelectedIndex(0);
		configuration.getUniformlyDistributedButton().setVisible(true);
		configuration.getNotUniformlyDistributedButton().setVisible(true);
		
		makeGeneralTablePhases();
		
		fillClustersTree(globalDataKeeper.getClusterCollectors().get(0).getClusters());
	}
	
	public void constructTableWithClusters() 
	{
		clusterTable = constructClustersTable();
		
		final String[] columns= clusterTable.getConstructedColumns();
		final String[][] rows= clusterTable.getConstructedRows();
		
		configuration.setSegmentSize(clusterTable.getSegmentSize());
		configuration.setFinalColumns(columns);
		configuration.setFinalRows(rows);
		configuration.setSelectedRows(new ArrayList<Integer>());
	}
	
	public TableConstructionWithClusters constructClustersTable() 
	{
		TableConstructionWithClusters table=new TableConstructionWithClusters(globalDataKeeper);
		
		table.constructColumns();
		table.constructRows();
		
		return table;
	}
	
	public void makeGeneralTablePhases(){
		GeneralTablePhasesWidget widget = new GeneralTablePhasesWidget(configuration, globalDataKeeper) ;
		widget.initializeGeneralTablePhases();
	}
	
	
	public void printPPLData() 
	{
		System.out.println("Schemas:"+globalDataKeeper.getAllPPLSchemas().size());
		System.out.println("Transitions:"+globalDataKeeper.getAllPPLTransitions().size());
		System.out.println("Tables:"+globalDataKeeper.getAllPPLTables().size());
	}
	
	public Configuration getConfiguration() 
	{
		return configuration;
	}

	
	public void fillTree(){
		
		TreeConstructionWidget treeWidget = new TreeConstructionWidget(configuration, globalDataKeeper.getAllPPLSchemas());
		treeWidget.fillTree();
	}

	public void setGlobalData(){
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
		DataService service = new DataService();
		pplFile = service.readFile(file);
		globalDataKeeper= service.setGlobalData(pplFile.getDatasetTxt(), pplFile.getTransitionsFile());
		
	}


	public void createPhaseAnalyserEngine()
	{
		PhasesService myService = new PhasesService();
		PhaseAnalyzerMainEngine mainPhaseEngine= myService.createPhaseAnalyserEngine(configuration,globalDataKeeper,pplFile);
		myService.connectTransitionsWithPhases(mainPhaseEngine, globalDataKeeper.getAllPPLTransitions());
		globalDataKeeper.setPhaseCollectors(mainPhaseEngine.getPhaseCollectors());
	}
	
	
	public void createTableClusteringMainEngine(int numberOfClusters)
	{
		TableService service = new TableService();
		TableClusteringMainEngine mainClusterEngine = service.createTableClusteringMainEngine(globalDataKeeper, numberOfClusters);
		globalDataKeeper.setClusterCollectors(mainClusterEngine.getClusterCollectors());
		
	}
	
	public void fillClustersTree(ArrayList<Cluster> clusters){
		
		 TreeConstructionPhasesWithClusters treePhaseWithClusters = new TreeConstructionPhasesWithClusters(clusters);
		 configuration.setTablesTree(treePhaseWithClusters.constructTree());
		 
		 configuration.getTablesTree().addTreeSelectionListener(new TreeSelectionListener () {
			    public void valueChanged(TreeSelectionEvent ae) { 
			    	TreePath selection = ae.getPath();
			    	configuration.getSelectedFromTree().add(selection.getLastPathComponent().toString());
			    	System.out.println(selection.getLastPathComponent().toString()+" is selected");
			    	
			    }
		 });
		 
		 configuration.getTablesTree().addMouseListener(new MouseAdapter() {
				@Override
				   public void mouseReleased(MouseEvent e) {
					
						if(SwingUtilities.isRightMouseButton(e)){
							System.out.println("Right Click Tree");
							
							final JPopupMenu popupMenu = new JPopupMenu();
					        JMenuItem showDetailsItem = new JMenuItem("Show This into the Table");
					        showDetailsItem.addActionListener(new ActionListener() {

					            @Override
					            public void actionPerformed(ActionEvent e) {
					          
					            	configuration.getLifeTimeTable().repaint();
					            	
					            }
					        });
					        popupMenu.add(showDetailsItem);
					        popupMenu.show(configuration.getTablesTree(), e.getX(),e.getY());
							        	
						}
					
				   }
			});
		 
		 configuration.getTreeScrollPane().setViewportView(configuration.getTablesTree());
		 
		 
		 configuration.getTreeScrollPane().setBounds(5, 5, 250, 170);
		 configuration.getTreeScrollPane().setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		 configuration.getTreeScrollPane().setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		 configuration.getTablesTreePanel().add(configuration.getTreeScrollPane());

		 configuration.getTreeLabel().setText("Clusters Tree");

		 configuration.getSideMenu().revalidate();
		 configuration.getSideMenu().repaint();
		 
		
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


	
	
}
