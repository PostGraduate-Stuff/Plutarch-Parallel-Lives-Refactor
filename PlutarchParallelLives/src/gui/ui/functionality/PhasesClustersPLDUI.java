package gui.ui.functionality;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import gui.configurations.DataConfiguration;
import gui.configurations.DataTablesConfiguration;
import gui.configurations.GuiConfiguration;
import gui.dialogs.ParametersJDialog;
import gui.tableElements.tableConstructors.TableConstructionWithClusters;

public class PhasesClustersPLDUI extends PLDHandler
{
	public JMenuItem showGeneralLifetimePhasesWithClusters(final DataGeneratorUI dataGenerator, final GuiConfiguration configuration,
			final DataConfiguration dataConfiguration, final DataTablesConfiguration tablesConfiguration){
	
		JMenuItem mntmShowGeneralLifetimePhasesWithClustersPLD = new JMenuItem("Show Phases With Clusters PLD");
		mntmShowGeneralLifetimePhasesWithClustersPLD.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				configuration.setWholeCol(-1);
				if(dataGenerator.getPplFile() == null)
				{
					JOptionPane.showMessageDialog(null, "Please select a project first!");
					return;
				}
					ParametersJDialog jDialog=new ParametersJDialog(true);
					jDialog.setModal(true);
					jDialog.setVisible(true);
					if(jDialog.getConfirmation()){
						dataConfiguration.setConfigurationsForPhases(jDialog);
						dataConfiguration.setConfigurationsForClusters(jDialog);
			            System.out.println(dataConfiguration.getTimeWeight()+" "+dataConfiguration.getChangeWeight());
			            dataGenerator.createPhaseAnalyserEngine();
			            dataGenerator.createTableClusteringMainEngine(dataConfiguration.getNumberOfClusters());
						
			            if(dataGenerator.getGlobalDataKeeper().getPhaseCollectors().size()==0)
						{
							JOptionPane.showMessageDialog(null, "Extract Phases first");
							return;
						}
						TableConstructionWithClusters table=new TableConstructionWithClusters(dataGenerator.getGlobalDataKeeper());
						setGeneralTablePhases(table,dataGenerator,configuration,dataConfiguration);
						
						dataGenerator.fillClustersTree(dataGenerator.getGlobalDataKeeper().getClusterCollectors().get(0).getClusters());
							
					}
				
			}
		});
		return mntmShowGeneralLifetimePhasesWithClustersPLD;
	}
}
