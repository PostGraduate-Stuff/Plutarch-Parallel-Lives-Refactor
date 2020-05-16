package services;

import java.util.ArrayList;
import java.util.TreeMap;

import data.dataKeeper.GlobalDataKeeper;
import data.dataPPL.pplSQLSchema.PPLSchema;
import data.dataPPL.pplTransition.PPLTransition;
import gui.tableElements.tableConstructors.TableConstructionClusterTablesPhasesZoomA;
import gui.tableElements.tableConstructors.TableConstructionIDU;
import gui.tableElements.tableConstructors.TableConstructionZoomArea;
import phaseAnalyzer.commons.Phase;
import tableClustering.clusterExtractor.engine.TableClusteringMainEngine;


public interface ITableService {
	public TableClusteringMainEngine createTableClusteringMainEngine(GlobalDataKeeper globalDataKeeper, int numberOfClusters);
	
	public TableConstructionClusterTablesPhasesZoomA createClusterTablesPhasesZoomA(TreeMap<String,PPLSchema> allPPLSchemas, ArrayList<Phase> phases, ArrayList<String> tablesOfCluster);
	
	public TableConstructionZoomArea createTableConstructionZoomArea(GlobalDataKeeper globalDataKeeper,ArrayList<String> sSelectedTables,int selectedColumn);
	
	public TableConstructionIDU createTableConstructionIDU(TreeMap<String,PPLSchema> AllPPLSchemas, TreeMap<Integer,PPLTransition> AllPPLTransitions);

}
