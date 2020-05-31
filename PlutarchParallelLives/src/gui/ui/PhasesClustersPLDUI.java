package gui.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import gui.configurations.DataConfiguration;
import gui.configurations.DataTablesConfiguration;
import gui.configurations.GuiConfiguration;
import gui.dialogs.ParametersJDialog;
import gui.tableElements.tableConstructors.TableConstructionWithClusters;

public class PhasesClustersPLDUI 
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
					ParametersJDialog jD=new ParametersJDialog(true);
					
					jD.setModal(true);
					
					jD.setVisible(true);
					
					if(jD.getConfirmation()){
					
						dataConfiguration.setTimeWeight(jD.getTimeWeight());
						dataConfiguration.setChangeWeight(jD.getChangeWeight());
						dataConfiguration.setPreProcessingTime(jD.getPreProcessingTime());
						dataConfiguration.setPreProcessingChange(jD.getPreProcessingChange());
			            dataConfiguration.setNumberOfPhases(jD.getNumberOfPhases());
			            dataConfiguration.setNumberOfClusters(jD.getNumberOfClusters());
			            dataConfiguration.setBirthWeight(jD.geBirthWeight());
			            dataConfiguration.setDeathWeight(jD.getDeathWeight());
			            dataConfiguration.setChangeWeightCl(jD.getChangeWeightCluster());
			            
			            System.out.println(dataConfiguration.getTimeWeight()+" "+dataConfiguration.getChangeWeight());
			            
			            
			            dataGenerator.createPhaseAnalyserEngine();
			    		
			    		
			            dataGenerator.createTableClusteringMainEngine(dataConfiguration.getNumberOfClusters());
			    		
						if(dataGenerator.getGlobalDataKeeper().getPhaseCollectors().size()==0)
						{
							JOptionPane.showMessageDialog(null, "Extract Phases first");
							return;
						}
						TableConstructionWithClusters table=new TableConstructionWithClusters(dataGenerator.getGlobalDataKeeper());
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
						dataGenerator.fillClustersTree(dataGenerator.getGlobalDataKeeper().getClusterCollectors().get(0).getClusters());
							
					}
				
			}
		});
		return mntmShowGeneralLifetimePhasesWithClustersPLD;
	}
}
