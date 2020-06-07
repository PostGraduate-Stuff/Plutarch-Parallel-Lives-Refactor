package gui.ui.functionality;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenuItem;


public class ProjectLoaderUI extends JFrame
{
	private static final long serialVersionUID = 1L;

	public JMenuItem loadProject(final DataGeneratorUI dataGenerator){
		JMenuItem mntmLoadProject = new JMenuItem("Load Project");
		mntmLoadProject.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				File dir=new File("filesHandler/inis");
				JFileChooser fcOpen1 = new JFileChooser();
				fcOpen1.setCurrentDirectory(dir);
				int returnVal = fcOpen1.showDialog(ProjectLoaderUI.this, "Open");
				
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					dataGenerator.importData(fcOpen1.getSelectedFile());
				}
				
			}
		});
		return mntmLoadProject;
	}
	
	
}
