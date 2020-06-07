package gui.ui.zoom;

import java.util.ArrayList;

import data.dataKeeper.GlobalDataKeeper;
import gui.configurations.DataConfiguration;
import gui.configurations.DataTablesConfiguration;
import gui.configurations.GuiConfiguration;
import gui.tableElements.tableConstructors.PldConstruction;
import gui.ui.table.ZoomAreaTableForClusterUI;
import services.TableService;

public class ClusterSelectionToZoomAreaUI 
{
	private GuiConfiguration configuration;
	private DataConfiguration dataConfiguration;
	private DataTablesConfiguration tablesConfiguration;
	private GlobalDataKeeper globalDataKeeper;
	private int selectedColumn; 
	
	public ClusterSelectionToZoomAreaUI(final GuiConfiguration configuration, final GlobalDataKeeper globalDataKeeper,int selectedColumn, final DataTablesConfiguration tablesConfiguration, final DataConfiguration dataConfiguration) {
		this.configuration = configuration;
		this.globalDataKeeper = globalDataKeeper;
		this.selectedColumn = selectedColumn;
		this.dataConfiguration = dataConfiguration;
		this.tablesConfiguration = tablesConfiguration;
	}
	
	public void showClusterSelectionToZoomArea()
	{
		ArrayList<String> tablesOfCluster = createTableCluster();
	
		updateConfigurationWithPldTable(tablesOfCluster);
		ZoomAreaTableForClusterUI zoomAreaTableForClusterWidget = new ZoomAreaTableForClusterUI(configuration, globalDataKeeper, tablesConfiguration, dataConfiguration);
		zoomAreaTableForClusterWidget.makeZoomAreaTableForCluster();
		
	}
	
	private ArrayList<String> createTableCluster(){
		ArrayList<String> tablesOfCluster = new ArrayList<>();
		
		for(int i=0; i <configuration.getTablesSelected().size(); i++){
			String[] selectedClusterSplit= configuration.getTablesSelected().get(i).split(" ");
			int cluster=Integer.parseInt(selectedClusterSplit[1]);
			ArrayList<String> namesOfTables=globalDataKeeper.getClusterCollectors().get(0).getClusters().get(cluster).getNamesOfTables();
			for(int j=0; j<namesOfTables.size(); j++){
				tablesOfCluster.add(namesOfTables.get(j));
			}
			System.out.println(configuration.getTablesSelected().get(i));
		}
		return tablesOfCluster;
	}
	
	private void updateConfigurationWithPldTable(ArrayList<String> tablesOfCluster){
		PldConstruction table;
		TableService service = new TableService(); 
		if (selectedColumn==0) {
			table = service.createClusterTablesPhasesZoomA(globalDataKeeper.getAllPPLSchemas(),globalDataKeeper.getPhaseCollectors().get(0).getPhases(),tablesOfCluster);
			
		}
		else{
			table = service.createTableConstructionZoomArea(globalDataKeeper,tablesOfCluster,selectedColumn);
		}
	
		final String[] columns= table.getConstructedColumns();
		final String[][] rows= table.getConstructedRows();
		dataConfiguration.setSegmentSizeZoomArea(table.getSegmentSize());
		System.out.println("Schemas: "+globalDataKeeper.getAllPPLSchemas().size());
		System.out.println("C: "+columns.length+" R: "+rows.length);

		dataConfiguration.setFinalColumnsZoomArea(columns);
		dataConfiguration.setFinalRowsZoomArea(rows);
		configuration.getTabbedPane().setSelectedIndex(0);
		
	}
}
