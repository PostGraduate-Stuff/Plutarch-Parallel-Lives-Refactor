package gui.ui.functionality;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import org.antlr.v4.runtime.RecognitionException;

import gui.dialogs.CreateProjectJDialog;

public class ProjectCreatorUI extends ProjectHandler {
	
	public JMenuItem createProject(final DataGeneratorUI dataGenerator){
		JMenuItem mntmCreateProject = new JMenuItem("Create Project");
		mntmCreateProject.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				CreateProjectJDialog createProjectDialog=new CreateProjectJDialog("","","","","","");
				initiateDatagenerator(createProjectDialog, dataGenerator, createProjectDialog.getFile());
			}
		});
		return mntmCreateProject;
	}
}
