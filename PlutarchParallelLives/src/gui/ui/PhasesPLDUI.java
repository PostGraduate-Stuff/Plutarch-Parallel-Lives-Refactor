package gui.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import gui.configurations.DataConfiguration;
import gui.configurations.DataTablesConfiguration;
import gui.configurations.GuiConfiguration;
import gui.dialogs.ParametersJDialog;
import gui.tableElements.tableConstructors.TableConstructionPhases;

public class PhasesPLDUI {

	
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
					ParametersJDialog jD=new ParametersJDialog(false);
					
					jD.setModal(true);
					
					
					jD.setVisible(true);
					
					if(jD.getConfirmation()){
					
						dataConfiguration.setTimeWeight(jD.getTimeWeight());
						dataConfiguration.setChangeWeight(jD.getChangeWeight());
						dataConfiguration.setPreProcessingTime(jD.getPreProcessingTime());
						dataConfiguration.setPreProcessingChange(jD.getPreProcessingChange());
						dataConfiguration.setNumberOfPhases(jD.getNumberOfPhases());
			            
			            System.out.println(dataConfiguration.getTimeWeight()+" "+dataConfiguration.getChangeWeight());
			            
			            
			            dataGenerator.createPhaseAnalyserEngine();
						
			    		
						if(dataGenerator.getGlobalDataKeeper().getPhaseCollectors().size() == 0){
							JOptionPane.showMessageDialog(null, "Extract Phases first");
							return;
						}
						
						TableConstructionPhases table=new TableConstructionPhases(dataGenerator.getGlobalDataKeeper());
						//TODO in service
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
						
						TreeConstructionPhasesUI widget = new TreeConstructionPhasesUI(dataGenerator.getGlobalDataKeeper(), configuration, tablesConfiguration);
						widget.fillPhasesTree();
					}
				
				
				
			}
		});
		return mntmShowGeneralLifetimePhasesPLD;
	}
}
