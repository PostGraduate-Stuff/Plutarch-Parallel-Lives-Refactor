package gui.widgets;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;

import data.dataKeeper.GlobalDataKeeper;
import gui.configurations.Configuration;
import gui.treeElements.TreeConstructionPhases;

public class TreeConstructionPhasesWidget 
{
	private GlobalDataKeeper globalDataKeeper;
	private Configuration configuration;
	
	public TreeConstructionPhasesWidget(GlobalDataKeeper globalDataKeeper, Configuration configuration)
	{
		this.globalDataKeeper = globalDataKeeper;
		this.configuration = configuration;
	}
	
	public void fillPhasesTree(){
		
		 TreeConstructionPhases tc=new TreeConstructionPhases(globalDataKeeper);
		 configuration.setTablesTree(tc.constructTree());
		 
		 configuration.getTablesTree().addTreeSelectionListener(new TreeSelectionListener () {
			    public void valueChanged(TreeSelectionEvent ae) { 
			    	TreePath selection = ae.getPath();
			    	configuration.getSelectedFromTree().add(selection.getLastPathComponent().toString());
			    	System.out.println(selection.getLastPathComponent().toString()+" is selected");
			    	
			    }
		 });
		 
		 configuration.getTablesTree().addMouseListener(new MouseAdapter() {
				@Override
				   public void mouseReleased(MouseEvent e) {
					
						if(SwingUtilities.isRightMouseButton(e)){
							System.out.println("Right Click Tree");
							
							final JPopupMenu popupMenu = new JPopupMenu();
					        JMenuItem showDetailsItem = new JMenuItem("Show This into the Table");
					        showDetailsItem.addActionListener(new ActionListener() {

					            @Override
					            public void actionPerformed(ActionEvent e) {
					          
					            	configuration.getLifeTimeTable().repaint();
					            	
					            }
					        });
					        popupMenu.add(showDetailsItem);
					        popupMenu.show(configuration.getTablesTree(), e.getX(),e.getY());
							        							        
							
						}
					
				   }
			});
		 
		 configuration.getTreeScrollPane().setViewportView(configuration.getTablesTree());
		 configuration.getTreeScrollPane().setBounds(5, 5, 250, 170);
		 configuration.getTreeScrollPane().setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		 configuration.getTreeScrollPane().setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		 configuration.getTablesTreePanel().add(configuration.getTreeScrollPane());
		 
		 configuration.getTreeLabel().setText("Phases Tree");

		 configuration.getSideMenu().revalidate();
		 configuration.getSideMenu().repaint();
		
	}
	

}
