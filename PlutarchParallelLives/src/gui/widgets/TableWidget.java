package gui.widgets;

import javax.swing.JScrollPane;

import gui.configurations.Configuration;
import gui.tableElements.commons.JvTable;

public abstract class TableWidget {
	protected Configuration configuration;
	protected JvTable table;
	
	public TableWidget(Configuration configuration)
	{
		this.configuration = configuration;
	}
	protected void setSettingsConfigurations() {
		configuration.setZoomAreaTable(table);
		configuration.getTmpScrollPaneZoomArea().setViewportView(configuration.getZoomAreaTable());
		configuration.getTmpScrollPaneZoomArea().setAlignmentX(0);
		configuration.getTmpScrollPaneZoomArea().setAlignmentY(0);
		configuration.getTmpScrollPaneZoomArea().setBounds(300,300,950,250);
		configuration.getTmpScrollPaneZoomArea().setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		configuration.getTmpScrollPaneZoomArea().setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		configuration.getLifeTimePanel().add(configuration.getTmpScrollPaneZoomArea());
	}
}
