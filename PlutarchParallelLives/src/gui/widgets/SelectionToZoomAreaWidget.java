package gui.widgets;

import data.dataKeeper.GlobalDataKeeper;
import gui.configurations.Configuration;
import gui.tableElements.tableConstructors.TableConstructionZoomArea;
import services.TableService;

public class SelectionToZoomAreaWidget {
	public void showSelectionToZoomArea(int selectedColumn, GlobalDataKeeper globalDataKeeper, Configuration configuration){
		TableService service = new TableService();
		TableConstructionZoomArea table=service.createTableConstructionZoomArea(globalDataKeeper,configuration.getTablesSelected(),selectedColumn);
		final String[] columns= table.getConstructedColumns();
		final String[][] rows= table.getConstructedRows();
		
		configuration.setSegmentSizeZoomArea(table.getSegmentSize());

		System.out.println("Schemas: "+globalDataKeeper.getAllPPLSchemas().size());
		System.out.println("C: "+columns.length+" R: "+rows.length);

		configuration.setFinalColumnsZoomArea(columns);
		configuration.setFinalRowsZoomArea(rows);
		configuration.getTabbedPane().setSelectedIndex(0);
		
		ZoomAreaTableWidget zoomAreaTableWidget = new ZoomAreaTableWidget(configuration, globalDataKeeper);
		zoomAreaTableWidget.makeZoomAreaTable();
	}

}
