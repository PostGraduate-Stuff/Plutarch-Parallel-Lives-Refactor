package gui.ui.functionality;

import java.io.File;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.antlr.v4.runtime.RecognitionException;

import gui.dialogs.CreateProjectJDialog;

public class ProjectHandler extends JFrame
{
	private static final long serialVersionUID = 1L;

	public void initiateDatagenerator(final CreateProjectJDialog createProjectDialog, final DataGeneratorUI dataGenerator, File file)
	{
		createProjectDialog.setModal(true);
		createProjectDialog.setVisible(true);
		if(createProjectDialog.getConfirmation())
		{
			createProjectDialog.setVisible(false);
			File fileSelected = createProjectDialog.getFile();
            System.out.println(fileSelected.toString());
            System.out.println("!!"+fileSelected.getName());
            
			try 
			{
				dataGenerator.importData(file);
			} 
			catch (RecognitionException e) 
			{
				JOptionPane.showMessageDialog(null, "Something seems wrong with this file");
				return;
			}
			
		}
	}
}
