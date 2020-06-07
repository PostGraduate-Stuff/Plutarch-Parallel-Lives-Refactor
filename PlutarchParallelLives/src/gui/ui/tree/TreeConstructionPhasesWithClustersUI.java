package gui.ui.tree;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;

import gui.configurations.DataTablesConfiguration;
import gui.configurations.GuiConfiguration;
import gui.treeElements.TreeConstructionPhasesWithClusters;
import tableClustering.clusterExtractor.commons.Cluster;

public class TreeConstructionPhasesWithClustersUI extends TreeUI{

	public ArrayList<Cluster> clusters;
	
	public TreeConstructionPhasesWithClustersUI(final GuiConfiguration configuration, final DataTablesConfiguration tablesConfiguration, ArrayList<Cluster> clusters)
	{
		super(configuration,tablesConfiguration);
		this.clusters = clusters;
	}
	
	public void fillClustersTree(){
		
		 TreeConstructionPhasesWithClusters treePhaseWithClusters = new TreeConstructionPhasesWithClusters(clusters);
		 configuration.setTablesTree(treePhaseWithClusters.constructTree());
		 
		 addTreeSectionListener();
		 
		 addTreeMouseListener();
		 
		 setSettingsConfigurations("Clusters Tree");
	}

	

	private void addTreeMouseListener() {
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
					          
					            	tablesConfiguration.getLifeTimeTable().repaint();
					            	
					            }
					        });
					        popupMenu.add(showDetailsItem);
					        popupMenu.show(configuration.getTablesTree(), e.getX(),e.getY());
							        	
						}
					
				   }
			});		
	}

	private void addTreeSectionListener() {
		configuration.getTablesTree().addTreeSelectionListener(new TreeSelectionListener () {
		    public void valueChanged(TreeSelectionEvent ae) { 
		    	TreePath selection = ae.getPath();
		    	configuration.getSelectedFromTree().add(selection.getLastPathComponent().toString());
		    	System.out.println(selection.getLastPathComponent().toString()+" is selected");
		    	
		    }
	 });		
	}


}
