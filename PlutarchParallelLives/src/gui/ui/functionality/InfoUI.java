package gui.ui.functionality;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import gui.dialogs.ProjectInfoDialog;

public class InfoUI {

	public JMenuItem showInfo(final DataGeneratorUI dataGenerator){
		JMenuItem infoItem = new JMenuItem("Info");
		infoItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(dataGenerator.getPplFile() == null){
					JOptionPane.showMessageDialog(null, "Select a Project first");
					return;	
				}
				
				dataGenerator.getPplFile().displayProjectsDescription();
				dataGenerator.getGlobalDataKeeper().printPPLData();
				
				ProjectInfoDialog infoDialog = new ProjectInfoDialog(dataGenerator.getPplFile().getProjectName(),
																	 dataGenerator.getPplFile().getDatasetTxt(),
																	 dataGenerator.getPplFile().getInputCsv(),
																	 dataGenerator.getPplFile().getTransitionsFile(),
																	 dataGenerator.getGlobalDataKeeper().getAllPPLSchemas().size(),
																	 dataGenerator.getGlobalDataKeeper().getAllPPLTransitions().size(), 
																	 dataGenerator.getGlobalDataKeeper().getAllPPLTables().size());
				
				infoDialog.setVisible(true);
			
			}
		});
		return infoItem;
	}
}
