package gui.ui.functionality;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import org.antlr.v4.runtime.RecognitionException;


import gui.dialogs.CreateProjectJDialog;

public class ProjectEditorUI extends ProjectHandler
{
	private static final long serialVersionUID = 1L;

	public JMenuItem editProject(final DataGeneratorUI dataGenerator)
	{
		JMenuItem mntmEditProject = new JMenuItem("Edit Project");
		mntmEditProject.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent arg0) 
			{
				File dir=new File("filesHandler/inis");
				JFileChooser fcOpen1 = new JFileChooser();
				fcOpen1.setCurrentDirectory(dir);
				int returnVal = fcOpen1.showDialog(ProjectEditorUI.this, "Open");
				
				if (returnVal != JFileChooser.APPROVE_OPTION) 
				{
					return;
				}
					
	            File file = fcOpen1.getSelectedFile();
	            dataGenerator.readFile(file);
	
				System.out.println(dataGenerator.getPplFile().getProjectName());
				
				CreateProjectJDialog createProjectDialog=new CreateProjectJDialog(dataGenerator.getPplFile().getProjectName(),
																					dataGenerator.getPplFile().getDatasetTxt(),
																					dataGenerator.getPplFile().getInputCsv(),
																					dataGenerator.getPplFile().getOutputAssessment1(),
																					dataGenerator.getPplFile().getOutputAssessment2(),
																					dataGenerator.getPplFile().getTransitionsFile());
				initiateDatagenerator(createProjectDialog, dataGenerator, file);
			
				
					
			}
		});
		
		return mntmEditProject;
	}
}
