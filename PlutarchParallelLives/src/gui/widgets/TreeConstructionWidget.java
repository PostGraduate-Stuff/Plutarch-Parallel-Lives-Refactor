package gui.widgets;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.TreeMap;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;

import data.dataPPL.pplSQLSchema.PPLSchema;
import gui.configurations.Configuration;
import gui.treeElements.TreeConstructionGeneral;

public class TreeConstructionWidget extends TreeWidget{
	
	private TreeMap<String,PPLSchema> allPPLSchemas;
	
	public TreeConstructionWidget(Configuration configuration, TreeMap<String, PPLSchema> allPPLSchemas) {
		super(configuration);
		this.allPPLSchemas = allPPLSchemas;
	}
	
	public void fillTree(){
		 TreeConstructionGeneral treeConstruction=new TreeConstructionGeneral(allPPLSchemas);
		 configuration.setTablesTree(new JTree());
		 configuration.setTablesTree(treeConstruction.constructTree());
		 
		 addTreeSectionListener();
		 
		 addTreeMouseListener();
		 
		 setSettingsConfigurations("General Tree");
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
							          
							            	configuration.getLifeTimeTable().repaint();
							            	
							            }
							        });
							        popupMenu.add(showDetailsItem);
							        popupMenu.show(configuration.getTablesTree(), e.getX(),e.getY());
							        							        
								//}
							//}
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
