package services;

import java.awt.Dimension;
import java.io.File;
import java.util.ArrayList;
import java.util.TreeMap;

import javax.swing.JTable;

import data.dataKeeper.GlobalDataKeeper;
import data.dataPPL.pplSQLSchema.PPLSchema;
import data.dataPPL.pplSQLSchema.PPLTable;
import data.dataPPL.pplTransition.PPLTransition;
import gui.configurations.Configuration;
import gui.tableElements.commons.JvTable;
import gui.tableElements.commons.MyTableModel;
import gui.tableElements.tableConstructors.TableConstructionClusterTablesPhasesZoomA;
import gui.tableElements.tableConstructors.TableConstructionIDU;
import gui.tableElements.tableConstructors.TableConstructionZoomArea;
import gui.tableElements.tableRenderers.IDUHeaderTableRenderer;
import phaseAnalyzer.commons.Phase;
import phaseAnalyzer.engine.PhaseAnalyzerMainEngine;
import tableClustering.clusterExtractor.engine.TableClusteringMainEngine;

public interface IDataService {

	public PPLFile readFile(File file);
	
	public GlobalDataKeeper setGlobalData(String datasetTxt, String transitionsFile);
	
	public String[][] sortRows(String[][] finalRows, TreeMap<String,PPLTable> pplTables);
	
	public PhaseAnalyzerMainEngine createPhaseAnalyserEngine(Configuration configuration, GlobalDataKeeper globalDataKeeper,PPLFile pplFile);

	public void connectTransitionsWithPhases(PhaseAnalyzerMainEngine mainEngine, TreeMap<Integer,PPLTransition> allPPLTransitions);
	
	
	public TableClusteringMainEngine createTableClusteringMainEngine(GlobalDataKeeper globalDataKeeper, int numberOfClusters);
	
	public JvTable makeGeneralTableIDU(Configuration configuration,
				 ArrayList<Phase> initialPhases) ;
	
	public TableConstructionClusterTablesPhasesZoomA createClusterTablesPhasesZoomA(TreeMap<String,PPLSchema> allPPLSchemas, ArrayList<Phase> phases, ArrayList<String> tablesOfCluster);
	
	public TableConstructionZoomArea createTableConstructionZoomArea(GlobalDataKeeper globalDataKeeper,ArrayList<String> sSelectedTables,int selectedColumn);
	
	public TableConstructionIDU createTableConstructionIDU(TreeMap<String,PPLSchema> AllPPLSchemas, TreeMap<Integer,PPLTransition> AllPPLTransitions);

}
