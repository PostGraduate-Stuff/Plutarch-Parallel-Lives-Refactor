package gui.widgets;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JMenuItem;

import gui.mainEngine.Gui;

public class LoadProject
{

	private String name= "Load Project";
	private JMenuItem mntmLoadProject;
	
	public void initialize(final Gui mygui)
	{
		JMenuItem mntmLoadProject = new JMenuItem("Load Project");
		mntmLoadProject.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				File dir=new File("filesHandler/inis");
				JFileChooser fcOpen1 = new JFileChooser();
				fcOpen1.setCurrentDirectory(dir);
				int returnVal = fcOpen1.showDialog(mygui, "Open");
				
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					//mygui.initiateLoadState();
					//mygui.importData(fcOpen1.getSelectedFile());
				}
				
			}
		});
	}
	
	public JMenuItem getLoadProject()
	{
		return mntmLoadProject;
	}
}
