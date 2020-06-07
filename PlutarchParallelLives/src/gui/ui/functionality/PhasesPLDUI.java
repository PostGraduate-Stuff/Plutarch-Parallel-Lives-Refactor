package gui.ui.functionality;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import gui.configurations.DataConfiguration;
import gui.configurations.DataTablesConfiguration;
import gui.configurations.GuiConfiguration;
import gui.dialogs.ParametersJDialog;
import gui.tableElements.tableConstructors.TableConstructionPhases;
import gui.ui.tree.TreeConstructionPhasesUI;

public class PhasesPLDUI extends PLDHandler {

	
	public JMenuItem showPhasesPLDUI(final DataGeneratorUI dataGenerator, final GuiConfiguration configuration,
			final DataConfiguration dataConfiguration, final DataTablesConfiguration tablesConfiguration){
		JMenuItem mntmShowGeneralLifetimePhasesPLD = new JMenuItem("Show Phases PLD");
		mntmShowGeneralLifetimePhasesPLD.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				if(dataGenerator.getPplFile() == null || dataGenerator.getPplFile().getProjectName()==null)
				{
					JOptionPane.showMessageDialog(null, "Please select a project first!");
					return;
				}
				
					configuration.setWholeCol(-1);
					ParametersJDialog jDialog=new ParametersJDialog(false);
					jDialog.setModal(true);
					jDialog.setVisible(true);
					if(jDialog.getConfirmation()){
						dataConfiguration.setConfigurationsForPhases(jDialog);
			            System.out.println(dataConfiguration.getTimeWeight()+" "+dataConfiguration.getChangeWeight());
			            dataGenerator.createPhaseAnalyserEngine();
						
			            
			            
			            if(dataGenerator.getGlobalDataKeeper().getPhaseCollectors().size() == 0){
							JOptionPane.showMessageDialog(null, "Extract Phases first");
							return;
						}
						TableConstructionPhases table=new TableConstructionPhases(dataGenerator.getGlobalDataKeeper());
						//TODO in service
						setGeneralTablePhases(table,dataGenerator,configuration,dataConfiguration);
						
						TreeConstructionPhasesUI widget = new TreeConstructionPhasesUI(dataGenerator.getGlobalDataKeeper(), configuration, tablesConfiguration);
						widget.fillPhasesTree();
					}
				
				
				
			}
		});
		return mntmShowGeneralLifetimePhasesPLD;
	}
}
