package gui.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import org.antlr.v4.runtime.RecognitionException;

import gui.configurations.DataConfiguration;
import gui.configurations.DataTablesConfiguration;
import gui.configurations.GuiConfiguration;
import gui.dialogs.CreateProjectJDialog;

public class ProjectCreatorUI {
	
	public JMenuItem createProject(final DataGeneratorUI dataGenerator){
		JMenuItem mntmCreateProject = new JMenuItem("Create Project");
		mntmCreateProject.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				CreateProjectJDialog createProjectDialog=new CreateProjectJDialog("","","","","","");

				createProjectDialog.setModal(true);
				
				
				createProjectDialog.setVisible(true);
				
				if(createProjectDialog.getConfirmation()){
					
					createProjectDialog.setVisible(false);
					
					File file = createProjectDialog.getFile();
		            System.out.println(file.toString());
		            
		            String fileName=file.toString();
		            System.out.println("!!"+file.getName());
		          
					try  
					{
						dataGenerator.importData(file);
					} 
					catch (RecognitionException e) {
						
						JOptionPane.showMessageDialog(null, "Something seems wrong with this file");
						return;
					}
					
				}
				
			}
		});
		return mntmCreateProject;
	}
}
