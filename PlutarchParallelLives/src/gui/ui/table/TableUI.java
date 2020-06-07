package gui.ui.table;

import java.awt.Color;
import java.awt.Component;

import gui.configurations.DataConfiguration;
import gui.configurations.DataTablesConfiguration;
import gui.configurations.GuiConfiguration;
import gui.tableElements.commons.JvTable;

public abstract class TableUI {
	protected GuiConfiguration configuration;
	protected DataConfiguration dataConfiguration;
	protected DataTablesConfiguration tablesConfiguration;
	protected JvTable table;
	
	public TableUI(final GuiConfiguration configuration, final DataConfiguration dataConfiguration, final DataTablesConfiguration tablesConfiguration)
	{
		this.configuration = configuration;
		this.dataConfiguration = dataConfiguration;
		this.tablesConfiguration = tablesConfiguration;
	}
	protected void setSettingsConfigurations() {
		tablesConfiguration.setZoomAreaTable(table);
		configuration.setScrollPaneSettings(table);
	}
	
	protected Color setInsertionColourForTable(int numericValue, int comparator) {
		if(numericValue==0){
			return new Color(154,205,50,200);
		}
		return setInsertionColour(numericValue,comparator);
	}
	
	protected Color setInsertionColourForZoomTable(int numericValue, int comparator) {
		if(numericValue==0){
			return new Color(0,100,0);
		}
		return setInsertionColour(numericValue,comparator);
	}
	
	private Color setInsertionColour(int numericValue, int comparator) {
		if(numericValue> 0&& numericValue<=comparator){
			return new Color(176,226,255);
    	}
		if(numericValue>comparator && numericValue<=2*comparator){
			return new Color(92,172,238);
		}
		if(numericValue>2*comparator && numericValue<=3*comparator){
			return new Color(28,134,238);
		}
		return new Color(16,78,139);
	}
}
