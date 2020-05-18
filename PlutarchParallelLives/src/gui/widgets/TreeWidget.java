package gui.widgets;

import javax.swing.JScrollPane;

import gui.configurations.Configuration;

public abstract class TreeWidget {
	protected Configuration configuration;
	
	public TreeWidget(Configuration configuration)
	{
		this.configuration = configuration;
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
