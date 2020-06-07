package gui.ui.functionality;

import javax.swing.JOptionPane;

import gui.configurations.DataConfiguration;
import gui.configurations.GuiConfiguration;
import gui.tableElements.tableConstructors.TableConstructionIDU;
import services.TableService;

public class PLDShowUI {
	
	public void showPLD(final GuiConfiguration configuration, final DataConfiguration dataConfiguration, final DataGeneratorUI dataGenerator){
		if(dataGenerator.getPplFile() == null)
		{
			JOptionPane.showMessageDialog(null, "Select a Project first");
			return;
		}
		
		configuration.getZoomInButton().setVisible(true);
		configuration.getZoomOutButton().setVisible(true);
		
		TableService service = new TableService();
		TableConstructionIDU table = service.createTableConstructionIDU(dataGenerator.getGlobalDataKeeper().getAllPPLSchemas(), dataGenerator.getGlobalDataKeeper().getAllPPLTransitions());
		dataConfiguration.setSegmentSizeZoomArea(table.getSegmentSize());
		System.out.println("Schemas: "+dataGenerator.getGlobalDataKeeper().getAllPPLSchemas().size());
		System.out.println("C: "+table.getConstructedColumns().length+" R: "+table.getConstructedRows().length);

		dataConfiguration.setFinalColumnsZoomArea(table.getConstructedColumns());
		dataConfiguration.setFinalRowsZoomArea(table.getConstructedRows());
		configuration.getTabbedPane().setSelectedIndex(0);
		dataGenerator.makeGeneralTableIDU();
		dataGenerator.fillTree();
	}
}
