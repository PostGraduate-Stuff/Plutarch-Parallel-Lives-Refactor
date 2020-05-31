package services;

import java.awt.Dimension;
import java.io.File;
import java.util.ArrayList;
import java.util.TreeMap;

import javax.swing.JTable;

import data.dataKeeper.GlobalDataKeeper;
import data.dataKeeper.PPLFile;
import data.dataPPL.pplSQLSchema.PPLSchema;
import data.dataPPL.pplSQLSchema.PPLTable;
import data.dataPPL.pplTransition.PPLTransition;
import gui.configurations.GuiConfiguration;
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
	
	public GlobalDataKeeper initiateGlobalData(String datasetTxt, String transitionsFile);
	
	public String[][] sortRows(String[][] finalRows, TreeMap<String,PPLTable> pplTables);
	
	
}
