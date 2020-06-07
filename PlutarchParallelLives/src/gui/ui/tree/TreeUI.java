package gui.ui.tree;

import gui.configurations.DataTablesConfiguration;
import gui.configurations.GuiConfiguration;

public abstract class TreeUI {
	protected GuiConfiguration configuration;
	protected DataTablesConfiguration tablesConfiguration;
	
	public TreeUI(final GuiConfiguration configuration, final DataTablesConfiguration tablesConfiguration)
	{
		this.configuration = configuration;
		this.tablesConfiguration = tablesConfiguration;
	}
	
	protected void setSettingsConfigurations(String text) {
		configuration.setTreeScrollSettings(text);
	}
}
