package gui.widgets;

import javax.swing.JOptionPane;

import gui.configurations.Configuration;
import gui.tableElements.tableConstructors.TableConstructionIDU;
import services.TableService;

public class PLDShowWidget {
	private Configuration configuration;
	private DataGeneratorWidget dataGenerator;
	
	public PLDShowWidget(Configuration configuration, DataGeneratorWidget dataGenerator) {
		this.configuration = configuration;
		this.dataGenerator = dataGenerator;
	}
	 
	public void showPLD(){
		if(dataGenerator.getPplFile() == null)
		{
			JOptionPane.showMessageDialog(null, "Select a Project first");
			return;
		}
		
		configuration.getZoomInButton().setVisible(true);
		configuration.getZoomOutButton().setVisible(true);
		TableService service = new TableService();
		TableConstructionIDU table = service.createTableConstructionIDU(dataGenerator.getGlobalDataKeeper().getAllPPLSchemas(), dataGenerator.getGlobalDataKeeper().getAllPPLTransitions());
		configuration.setSegmentSizeZoomArea(table.getSegmentSize());
		System.out.println("Schemas: "+dataGenerator.getGlobalDataKeeper().getAllPPLSchemas().size());
		System.out.println("C: "+table.getConstructedColumns().length+" R: "+table.getConstructedRows().length);

		configuration.setFinalColumnsZoomArea(table.getConstructedColumns());
		configuration.setFinalRowsZoomArea(table.getConstructedRows());
		configuration.getTabbedPane().setSelectedIndex(0);
		dataGenerator.makeGeneralTableIDU();
		dataGenerator.fillTree();
	}
}
