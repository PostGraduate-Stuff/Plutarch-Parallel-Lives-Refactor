package gui.widgets;

import java.util.ArrayList;

import data.dataKeeper.GlobalDataKeeper;
import gui.configurations.Configuration;
import gui.tableElements.tableConstructors.PldConstruction;
import gui.tableElements.tableConstructors.TableConstructionClusterTablesPhasesZoomA;
import gui.tableElements.tableConstructors.TableConstructionZoomArea;
import services.DataService;
import services.TableService;

public class ClusterSelectionToZoomAreaWidget 
{
	public void showClusterSelectionToZoomArea(Configuration configuration, GlobalDataKeeper globalDataKeeper, int selectedColumn,String selectedCluster)
	{

		ArrayList<String> tablesOfCluster=new ArrayList<String>();
		for(int i=0; i <configuration.getTablesSelected().size(); i++){
			String[] selectedClusterSplit= configuration.getTablesSelected().get(i).split(" ");
			int cluster=Integer.parseInt(selectedClusterSplit[1]);
			ArrayList<String> namesOfTables=globalDataKeeper.getClusterCollectors().get(0).getClusters().get(cluster).getNamesOfTables();
			for(int j=0; j<namesOfTables.size(); j++){
				tablesOfCluster.add(namesOfTables.get(j));
			}
			System.out.println(configuration.getTablesSelected().get(i));
		}
		
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
		configuration.setSegmentSizeZoomArea(table.getSegmentSize());
		System.out.println("Schemas: "+globalDataKeeper.getAllPPLSchemas().size());
		System.out.println("C: "+columns.length+" R: "+rows.length);

		configuration.setFinalColumnsZoomArea(columns);
		configuration.setFinalRowsZoomArea(rows);
		configuration.getTabbedPane().setSelectedIndex(0);
		//makeZoomAreaTableForCluster();
		ZoomAreaTableForClusterWidget zoomAreaTableForClusterWidget = new ZoomAreaTableForClusterWidget(configuration, globalDataKeeper);
		zoomAreaTableForClusterWidget.makeZoomAreaTableForCluster();
		
	}
}
