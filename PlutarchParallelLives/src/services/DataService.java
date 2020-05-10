package services;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.TreeMap;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.tree.TreePath;

import data.dataKeeper.GlobalDataKeeper;
import data.dataPPL.pplSQLSchema.PPLSchema;
import data.dataPPL.pplSQLSchema.PPLTable;
import data.dataPPL.pplTransition.PPLTransition;
import data.dataSorters.PldRowSorter;
import gui.configurations.Configuration;
import gui.mainEngine.Gui;
import gui.tableElements.commons.JvTable;
import gui.tableElements.commons.MyTableModel;
import gui.tableElements.tableConstructors.TableConstructionClusterTablesPhasesZoomA;
import gui.tableElements.tableConstructors.TableConstructionIDU;
import gui.tableElements.tableConstructors.TableConstructionWithClusters;
import gui.tableElements.tableConstructors.TableConstructionZoomArea;
import gui.tableElements.tableRenderers.IDUHeaderTableRenderer;
import gui.tableElements.tableRenderers.IDUTableRenderer;
import gui.treeElements.TreeConstructionGeneral;
import phaseAnalyzer.commons.Phase;
import phaseAnalyzer.engine.PhaseAnalyzerMainEngine;
import tableClustering.clusterExtractor.engine.TableClusteringMainEngine;

public class DataService implements IDataService 
{

	@Override
	public PPLFile readFile(File file) 
	{
		PPLFile pplFile = new PPLFile();
		try {
			pplFile.readFile(file);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return pplFile;

	}
	
	
	@Override
	public GlobalDataKeeper setGlobalData(String datasetTxt, String transitionsFile) {
		GlobalDataKeeper globalDataKeeper = new GlobalDataKeeper();
		try{
			globalDataKeeper=new GlobalDataKeeper(datasetTxt, transitionsFile);
			globalDataKeeper.setData();
			System.out.println(globalDataKeeper.getAllPPLTables().size());
			//System.out.println(pplFile.getFile().toString());

	      
		}
		catch(Exception ex){
			
		}	
		return globalDataKeeper;
	}
	
	public String[][] sortRows(String[][] finalRows, TreeMap<String,PPLTable> pplTables){
		PldRowSorter sorter=new PldRowSorter();
		return sorter.sortRows(finalRows, pplTables);
	}
	
	@Override
	public TableConstructionIDU createTableConstructionIDU(TreeMap<String,PPLSchema> AllPPLSchemas, TreeMap<Integer,PPLTransition> AllPPLTransitions)
	{
		TableConstructionIDU constructedTable=new TableConstructionIDU(AllPPLSchemas,AllPPLTransitions);
		constructedTable.constructColumns();
		constructedTable.constructRows();
		return constructedTable;
	}
	
	@Override
	public TableConstructionClusterTablesPhasesZoomA createClusterTablesPhasesZoomA(TreeMap<String,PPLSchema> allPPLSchemas, ArrayList<Phase> phases, ArrayList<String> tablesOfCluster)
	{
		TableConstructionClusterTablesPhasesZoomA constructedTable=new TableConstructionClusterTablesPhasesZoomA(allPPLSchemas,phases,tablesOfCluster);
		constructedTable.constructColumns();
		constructedTable.constructRows();
		return constructedTable;
	}
	
	@Override
	public TableConstructionZoomArea createTableConstructionZoomArea(GlobalDataKeeper globalDataKeeper,ArrayList<String> sSelectedTables,int selectedColumn)
	{
		TableConstructionZoomArea constructedTable=new TableConstructionZoomArea(globalDataKeeper,sSelectedTables, selectedColumn);
		constructedTable.constructColumns();
		constructedTable.constructRows();
		return constructedTable;
	}
	
	@Override
	public PhaseAnalyzerMainEngine createPhaseAnalyserEngine(Configuration configuration,
									GlobalDataKeeper globalDataKeeper,
									PPLFile pplFile) 
	{
		
        System.out.println(configuration.getTimeWeight()+" "+ configuration.getChangeWeight());
		PhaseAnalyzerMainEngine mainEngine = new PhaseAnalyzerMainEngine(pplFile.getInputCsv(),pplFile.getOutputAssessment1(),pplFile.getOutputAssessment2(), configuration.getTimeWeight(),configuration.getChangeWeight(), configuration.getPreProcessingTime(), configuration.getPreProcessingChange());

		mainEngine.parseInput();		
		System.out.println("\n\n\n");
		
		return mainEngine;
	
	}

	@Override
	public void connectTransitionsWithPhases(PhaseAnalyzerMainEngine mainEngine,
											TreeMap<Integer,PPLTransition> allPPLTransitions) 
	{
		//test an to mainEngine exei krathsei tis allages kai eksw sto GUI alliws thelei return
		
		int numberOfPhases= 56;
		if(allPPLTransitions.size()<56)
		{
		numberOfPhases=40;
		}
		mainEngine.extractPhases(numberOfPhases);
		mainEngine.connectTransitionsWithPhases(allPPLTransitions);
		
	}
	
	@Override
	public TableClusteringMainEngine createTableClusteringMainEngine(GlobalDataKeeper globalDataKeeper, int numberOfClusters) 
	{
		Double birthWeight = new Double(0.3);
		Double deathWeight = new Double(0.3);
		Double changeWeight = new Double(0.3);
	
		
		TableClusteringMainEngine mainEngine = new TableClusteringMainEngine(globalDataKeeper, birthWeight, deathWeight, changeWeight);
		mainEngine.extractClusters(numberOfClusters); //int numberOfClusters =14;
		mainEngine.print();

		return mainEngine;
	}
	
	
	
	@Override
	public JvTable makeGeneralTableIDU(Configuration configuration,
									 ArrayList<Phase> initialPhases) 
	{
		int numberOfColumns=configuration.getFinalRowsZoomArea()[0].length;
		int numberOfRows=configuration.getFinalRowsZoomArea().length;
		
		//selectedRows=new ArrayList<Integer>();
		
		String[][] rows=new String[numberOfRows][numberOfColumns];
		
		for(int i=0; i<numberOfRows; i++){
			
			rows[i][0]=configuration.getFinalRowsZoomArea()[i][0];
			
		}
		
		MyTableModel zoomModel= new MyTableModel();
		zoomModel.initializeZoomModel(configuration.getFinalColumnsZoomArea(), rows);
		
		final JvTable generalTable=new JvTable(zoomModel);
		
		generalTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		
		if(configuration.getRowHeight()<1){
			configuration.setRowHeight(1);
		}
		if (configuration.getColumnWidth() < 1) {
			configuration.setColumnWidth(1);
		}
		
		for(int i=0; i<generalTable.getRowCount(); i++)
		{
			generalTable.setRowHeight(i, configuration.getRowHeight());
		}

		
		generalTable.setShowGrid(false);
		generalTable.setIntercellSpacing(new Dimension(0, 0));
		
		
		for(int i=0; i<generalTable.getColumnCount(); i++)
		{
			generalTable.getColumnModel().getColumn(i).setPreferredWidth(configuration.getColumnWidth());	
		}
		
		int start=-1;
		int end=-1;
		
		
		if (configuration.getWholeCol()!=-1 && configuration.getWholeCol()!=0) 
		{
			start=initialPhases.get(configuration.getWholeCol()-1).getStartPos();
			end=initialPhases.get(configuration.getWholeCol()-1).getEndPos();
		}


		
		if(configuration.getWholeCol()!=-1){
			for(int i=0; i<generalTable.getColumnCount(); i++){
				if(!(generalTable.getColumnName(i).equals("Table name"))){
					if(Integer.parseInt(generalTable.getColumnName(i))>=start && Integer.parseInt(generalTable.getColumnName(i))<=end){
			
						generalTable.getColumnModel().getColumn(i).setHeaderRenderer(new IDUHeaderTableRenderer());
			
					}
				}
			}
		}
		
		return generalTable;
	}

}
