package gui.ui.functionality;

import gui.configurations.DataConfiguration;
import gui.configurations.GuiConfiguration;
import gui.tableElements.tableConstructors.TableConstruction;

public class PLDHandler 
{
	public void setGeneralTablePhases(final TableConstruction table, final DataGeneratorUI dataGenerator, final GuiConfiguration configuration,
												final DataConfiguration dataConfiguration){
		table.constructColumns();
		table.constructRows();
		final String[] columns= table.getConstructedColumns();
		final String[][] rows= table.getConstructedRows();
		dataConfiguration.setSegmentSize(table.getSegmentSize());
		System.out.println("Schemas: "+dataGenerator.getGlobalDataKeeper().getAllPPLSchemas().size());
		System.out.println("C: "+columns.length+" R: "+rows.length);

		dataConfiguration.setFinalColumns(columns);
		dataConfiguration.setFinalRows(rows);
		configuration.getTabbedPane().setSelectedIndex(0);
		dataGenerator.makeGeneralTablePhases();
	}
}
