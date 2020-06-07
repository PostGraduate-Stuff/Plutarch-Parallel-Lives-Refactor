package gui.ui.zoom;

import data.dataKeeper.GlobalDataKeeper;
import gui.configurations.DataConfiguration;
import gui.configurations.DataTablesConfiguration;
import gui.configurations.GuiConfiguration;
import gui.tableElements.tableConstructors.TableConstructionZoomArea;
import gui.ui.table.ZoomAreaTableUI;
import services.TableService;

public class SelectionToZoomAreaUI {
	public void showSelectionToZoomArea(int selectedColumn, final GlobalDataKeeper globalDataKeeper, final GuiConfiguration configuration, final DataConfiguration dataConfiguration, final DataTablesConfiguration tablesConfiguration){
		TableService service = new TableService();
		TableConstructionZoomArea table=service.createTableConstructionZoomArea(globalDataKeeper,configuration.getTablesSelected(),selectedColumn);
		final String[] columns= table.getConstructedColumns();
		final String[][] rows= table.getConstructedRows();
		
		dataConfiguration.setSegmentSizeZoomArea(table.getSegmentSize());

		System.out.println("Schemas: "+globalDataKeeper.getAllPPLSchemas().size());
		System.out.println("C: "+columns.length+" R: "+rows.length);

		dataConfiguration.setFinalColumnsZoomArea(columns);
		dataConfiguration.setFinalRowsZoomArea(rows);
		configuration.getTabbedPane().setSelectedIndex(0);
		
		ZoomAreaTableUI zoomAreaTableWidget = new ZoomAreaTableUI(configuration, dataConfiguration, tablesConfiguration, globalDataKeeper);
		zoomAreaTableWidget.makeZoomAreaTable();
	}

}
