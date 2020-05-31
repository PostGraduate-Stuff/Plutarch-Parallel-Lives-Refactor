package gui.ui;

import javax.swing.JScrollPane;

import gui.configurations.DataConfiguration;
import gui.configurations.DataTablesConfiguration;
import gui.configurations.GuiConfiguration;
import gui.tableElements.commons.JvTable;

public abstract class TableUI {
	protected GuiConfiguration configuration;
	protected DataConfiguration dataConfiguration;
	protected DataTablesConfiguration tablesConfiguration;
	protected JvTable table;
	
	public TableUI(GuiConfiguration configuration, DataConfiguration dataConfiguration, DataTablesConfiguration tablesConfiguration)
	{
		this.configuration = configuration;
		this.dataConfiguration = dataConfiguration;
		this.tablesConfiguration = tablesConfiguration;
	}
	protected void setSettingsConfigurations() {
		tablesConfiguration.setZoomAreaTable(table);
		configuration.getTmpScrollPaneZoomArea().setViewportView(tablesConfiguration.getZoomAreaTable());
		configuration.getTmpScrollPaneZoomArea().setAlignmentX(0);
		configuration.getTmpScrollPaneZoomArea().setAlignmentY(0);
		configuration.getTmpScrollPaneZoomArea().setBounds(300,300,950,250);
		configuration.getTmpScrollPaneZoomArea().setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		configuration.getTmpScrollPaneZoomArea().setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		configuration.getLifeTimePanel().add(configuration.getTmpScrollPaneZoomArea());
	}
}
