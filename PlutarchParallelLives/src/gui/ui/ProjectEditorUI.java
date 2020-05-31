package gui.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import org.antlr.v4.runtime.RecognitionException;

import gui.configurations.DataConfiguration;
import gui.configurations.DataTablesConfiguration;
import gui.configurations.GuiConfiguration;
import gui.dialogs.CreateProjectJDialog;
import gui.mainEngine.Gui;

public class ProjectEditorUI extends JFrame
{

	public JMenuItem editProject(final DataGeneratorUI dataGenerator)
	{
		JMenuItem mntmEditProject = new JMenuItem("Edit Project");
		mntmEditProject.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent arg0) 
			{
				String fileName=null;
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
			
				createProjectDialog.setModal(true);
				
				createProjectDialog.setVisible(true);
				
				if(createProjectDialog.getConfirmation())
				{
					createProjectDialog.setVisible(false);
					file = createProjectDialog.getFile();
		            System.out.println(file.toString());
		            System.out.println("!!"+file.getName());
		            
					try 
					{
						dataGenerator.importData(fcOpen1.getSelectedFile());
					} 
					catch (RecognitionException e) 
					{
						JOptionPane.showMessageDialog(null, "Something seems wrong with this file");
						return;
					}
					
				}
					
			}
		});
		
		return mntmEditProject;
	}
}
