package tests.ui;
import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.antlr.v4.runtime.RecognitionException;
import org.junit.Test;

import data.dataKeeper.GlobalDataKeeper;
import data.dataKeeper.PPLFile;
import gui.configurations.GuiConfiguration;
import gui.mainEngine.Gui;
import gui.tableElements.tableConstructors.TableConstructionWithClusters;
import gui.ui.functionality.DataGeneratorUI;
import phaseAnalyzer.engine.PhaseAnalyzerMainEngine;
import services.DataService;
import services.TableService;

public class UITester {
	private String path = "filesHandler/inis/biosql.ini";
	
	@Test
	public void testDataGeneratorUI() throws RecognitionException, IOException 
	{
		Gui gui = new Gui();
		DataGeneratorUI dataUI = new DataGeneratorUI(gui.getConfiguration(), gui.getDataConfiguration(), gui.getTablesConfiguration());
		gui.getDataConfiguration().initiateLoadState();
		dataUI.importDataFromFile(new File(path));
		TableService service = new TableService();
		dataUI.setConstructedIDUTable(service.createTableConstructionIDU(dataUI.getGlobalDataKeeper().getAllPPLSchemas(), dataUI.getGlobalDataKeeper().getAllPPLTransitions()));
		dataUI.setIDUPanel();
		dataUI.makeGeneralTableIDU();
		dataUI.createPhaseAnalyserEngine();
		dataUI.createTableClusteringMainEngine(14);
		
		//fillTable
		TableConstructionWithClusters clusterTable = dataUI.constructClustersTable();
		testClusterColumns(clusterTable);
		testClusterRows(clusterTable);
	}
	
	
	public void testClusterColumns(TableConstructionWithClusters clusterTable)
	{
		final String[] columns= clusterTable.getConstructedColumns();
		String newColumns = "";
		for(int i = 0; i< columns.length; i++){
			newColumns += columns[i];
		}
		
		String originalColumns = "Table name"
									+ "Phase 0"
									+ "Phase 1"
									+ "Phase 2"
									+ "Phase 3"
									+ "Phase 4"
									+ "Phase 5"
									+ "Phase 6"
									+ "Phase 7"
									+ "Phase 8"
									+ "Phase 9"
									+ "Phase 10"
									+ "Phase 11"
									+ "Phase 12"
									+ "Phase 13"
									+ "Phase 14"
									+ "Phase 15"
									+ "Phase 16"
									+ "Phase 17"
									+ "Phase 18"
									+ "Phase 19"
									+ "Phase 20"
									+ "Phase 21"
									+ "Phase 22"
									+ "Phase 23"
									+ "Phase 24"
									+ "Phase 25"
									+ "Phase 26"
									+ "Phase 27"
									+ "Phase 28"
									+ "Phase 29"
									+ "Phase 30"
									+ "Phase 31"
									+ "Phase 32"
									+ "Phase 33"
									+ "Phase 34"
									+ "Phase 35"
									+ "Phase 36"
									+ "Phase 37"
									+ "Phase 38"
									+ "Phase 39";
		
		assertEquals(originalColumns,newColumns);
		
	}

	public void testClusterRows(TableConstructionWithClusters clusterTable)
	{
		final String[][] rows= clusterTable.getConstructedRows();
	}
}
