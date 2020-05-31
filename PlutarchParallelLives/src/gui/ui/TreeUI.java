package gui.ui;

import javax.swing.JScrollPane;

import gui.configurations.DataTablesConfiguration;
import gui.configurations.GuiConfiguration;

public abstract class TreeUI {
	protected GuiConfiguration configuration;
	protected DataTablesConfiguration tablesConfiguration;
	
	public TreeUI(GuiConfiguration configuration, DataTablesConfiguration tablesConfiguration)
	{
		this.configuration = configuration;
		this.tablesConfiguration = tablesConfiguration;
	}
	
	protected void setSettingsConfigurations(String text) {
		configuration.getTreeScrollPane().setViewportView(configuration.getTablesTree());
		 configuration.getTreeScrollPane().setBounds(5, 5, 250, 170);
		 configuration.getTreeScrollPane().setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		 configuration.getTreeScrollPane().setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		 configuration.getTablesTreePanel().add(configuration.getTreeScrollPane());
		 
		 configuration.getTreeLabel().setText(text);

		 configuration.getSideMenu().revalidate();
		 configuration.getSideMenu().repaint();				
	}
}
