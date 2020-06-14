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

public class TableService implements ITableService {
	@Override
	public TableClusteringMainEngine createTableClusteringMainEngine(GlobalDataKeeper globalDataKeeper, int numberOfClusters) 
	{
		Double birthWeight = new Double(0.3);
		Double deathWeight = new Double(0.3);
		Double changeWeight = new Double(0.3);
	
		
		TableClusteringMainEngine mainEngine = new TableClusteringMainEngine(globalDataKeeper, birthWeight, deathWeight, changeWeight);
		mainEngine.extractClusters(numberOfClusters);
		mainEngine.print();

		return mainEngine;
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
	public TableConstructionClusterTablesPhasesZoomA createClusterTablesPhasesZoomA(TreeMap<String,PPLSchema> allPPLSchemas, ArrayList<Phase> phases, ArrayList<String> tablesOfCluster)
	{
		TableConstructionClusterTablesPhasesZoomA constructedTable=new TableConstructionClusterTablesPhasesZoomA(allPPLSchemas,phases,tablesOfCluster);
		constructedTable.constructColumns();
		constructedTable.constructRows();
		return constructedTable;
	}
	
	@Override
	public TableConstructionIDU createTableConstructionIDU(TreeMap<String,PPLSchema> AllPPLSchemas, TreeMap<Integer,PPLTransition> AllPPLTransitions)
	{
		TableConstructionIDU constructedTable=new TableConstructionIDU(AllPPLSchemas,AllPPLTransitions);
		constructedTable.constructColumns();
		constructedTable.constructRows();
		return constructedTable;
	}
	
}
