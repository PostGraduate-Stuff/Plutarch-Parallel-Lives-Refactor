package gui.widgets;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableCellRenderer;

import data.dataKeeper.GlobalDataKeeper;
import gui.configurations.Configuration;
import gui.tableElements.commons.JvTable;
import gui.tableElements.commons.MyTableModel;
import services.DataService;

public class ZoomAreaTableForClusterWidget {
	private Configuration configuration;
	private GlobalDataKeeper globalDataKeeper;
	
	public ZoomAreaTableForClusterWidget(Configuration configuration, GlobalDataKeeper globalDataKeeper){
		this.configuration = configuration;
		this.globalDataKeeper = globalDataKeeper;
	}
	
	public void makeZoomAreaTableForCluster() {
		configuration.setShowingPld(false);
		int numberOfColumns=configuration.getFinalRowsZoomArea()[0].length;
		int numberOfRows=configuration.getFinalRowsZoomArea().length;
		configuration.getUndoButton().setVisible(true);
		
		final String[][] rowsZoom=new String[numberOfRows][numberOfColumns];
		
		for(int i=0; i<numberOfRows; i++){
			
			rowsZoom[i][0]=configuration.getFinalRowsZoomArea()[i][0];
			
		}
		
		configuration.setZoomModel(new MyTableModel(configuration.getFinalColumnsZoomArea(), rowsZoom));
		
		final JvTable zoomTable=new JvTable(configuration.getZoomModel());
		
		zoomTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		
		
		zoomTable.setShowGrid(false);
		zoomTable.setIntercellSpacing(new Dimension(0, 0));
		

		
		for(int i=0; i<zoomTable.getColumnCount(); i++){
			if(i==0){
				zoomTable.getColumnModel().getColumn(0).setPreferredWidth(150);
				
			}
			else{
				
				zoomTable.getColumnModel().getColumn(i).setPreferredWidth(10);
				zoomTable.getColumnModel().getColumn(i).setMaxWidth(10);
				zoomTable.getColumnModel().getColumn(i).setMinWidth(70);
			}
		}
		
		zoomTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer()
		{
		    
			public static final long serialVersionUID = 1L;

			@Override
		    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
		    {
		        final Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		        
		        
		        
		        String tmpValue=configuration.getFinalRowsZoomArea()[row][column];
		        String columnName=table.getColumnName(column);
		        Color fr=new Color(0,0,0);
		        c.setForeground(fr);
		        
		        if(column==configuration.getWholeColZoomArea() && configuration.getWholeColZoomArea()!=0){
		        	
		        	String description=table.getColumnName(column)+"\n";
		          	description=description+"First Transition ID:"+globalDataKeeper.getPhaseCollectors().get(0).getPhases().
	        				get(column-1).getStartPos()+"\n";
	        		description=description+"Last Transition ID:"+globalDataKeeper.getPhaseCollectors().get(0).getPhases().
	        				get(column-1).getEndPos()+"\n";
	        		description=description+"Total Changes For This Phase:"+globalDataKeeper.getPhaseCollectors().get(0).getPhases().
	        				get(column-1).getTotalUpdates()+"\n";
	        		description=description+"Additions For This Phase:"+globalDataKeeper.getPhaseCollectors().get(0).getPhases().
	        				get(column-1).getTotalAdditionsOfPhase()+"\n";
	        		description=description+"Deletions For This Phase:"+globalDataKeeper.getPhaseCollectors().get(0).getPhases().
	        				get(column-1).getTotalDeletionsOfPhase()+"\n";
	        		description=description+"Updates For This Phase:"+globalDataKeeper.getPhaseCollectors().get(0).getPhases().
	        				get(column-1).getTotalUpdatesOfPhase()+"\n";
		        	
	        		configuration.getDescriptionText().setText(description);
		        	
		        	Color cl = new Color(255,69,0,100);

	        		c.setBackground(cl);
	        		return c;
		        }
		        else if(configuration.getSelectedColumnZoomArea()==0){
		        	if (isSelected){
		        		
		        		
			        		String description="Table:"+configuration.getFinalRowsZoomArea()[row][0]+"\n";
			        		description=description+"Birth Version Name:"+globalDataKeeper.getAllPPLTables().get(configuration.getFinalRowsZoomArea()[row][0]).getBirth()+"\n";
			        		description=description+"Birth Version ID:"+globalDataKeeper.getAllPPLTables().get(configuration.getFinalRowsZoomArea()[row][0]).getBirthVersionID()+"\n";
			        		description=description+"Death Version Name:"+globalDataKeeper.getAllPPLTables().get(configuration.getFinalRowsZoomArea()[row][0]).getDeath()+"\n";
			        		description=description+"Death Version ID:"+globalDataKeeper.getAllPPLTables().get(configuration.getFinalRowsZoomArea()[row][0]).getDeathVersionID()+"\n";
			        		description=description+"Total Changes:"+globalDataKeeper.getAllPPLTables().get(configuration.getFinalRowsZoomArea()[row][0]).getTotalChanges()+"\n";
			        		configuration.getDescriptionText().setText(description);

		        	
		        		
		        		Color cl = new Color(255,69,0,100);
		        		
		        		c.setBackground(cl);
		        		return c;
		        	}
		        }
		        else{
		        	
		        	
		        	if (isSelected && hasFocus){
			        	
		        		String description="";
		        		if(!table.getColumnName(column).contains("Table name")){
		        			
			        		
		        			description="Transition "+table.getColumnName(column)+"\n";
			        		
		        			description=description+"Old Version:"+globalDataKeeper.getAllPPLTransitions().get(Integer.parseInt(table.getColumnName(column))).getOldVersionName()+"\n";
			        		description=description+"New Version:"+globalDataKeeper.getAllPPLTransitions().get(Integer.parseInt(table.getColumnName(column))).getNewVersionName()+"\n\n";
			
		        			//description=description+"First Transition ID:"+globalDataKeeper.getPhaseCollectors().get(0).getPhases().
			        				//get(column-1).getStartPos()+"\n";
			        		//description=description+"Last Transition ID:"+globalDataKeeper.getPhaseCollectors().get(0).getPhases().
			        			//	get(column-1).getEndPos()+"\n\n";
		        			description=description+"Table:"+configuration.getFinalRowsZoomArea()[row][0]+"\n";
			        		description=description+"Birth Version Name:"+globalDataKeeper.getAllPPLTables().get(configuration.getFinalRowsZoomArea()[row][0]).getBirth()+"\n";
			        		description=description+"Birth Version ID:"+globalDataKeeper.getAllPPLTables().get(configuration.getFinalRowsZoomArea()[row][0]).getBirthVersionID()+"\n";
			        		description=description+"Death Version Name:"+globalDataKeeper.getAllPPLTables().get(configuration.getFinalRowsZoomArea()[row][0]).getDeath()+"\n";
			        		description=description+"Death Version ID:"+globalDataKeeper.getAllPPLTables().get(configuration.getFinalRowsZoomArea()[row][0]).getDeathVersionID()+"\n";
			        		description=description+"Total Changes For This Phase:"+tmpValue+"\n";
			        		
			        		configuration.getDescriptionText().setText(description);

		        		}
		        		
		        		Color cl = new Color(255,69,0,100);
		        		
		        		c.setBackground(cl);
		        		return c;
			        }
		        	
		        }


		        try{
		        	int numericValue=Integer.parseInt(tmpValue);
		        	Color insersionColor=null;
					setToolTipText(Integer.toString(numericValue));

		        	
	        		if(numericValue==0){
	        			insersionColor=new Color(0,100,0);
	        		}
	        		else if(numericValue> 0&& numericValue<=configuration.getSegmentSizeZoomArea()[3]){
	        			
	        			insersionColor=new Color(176,226,255);
		        	}
	        		else if(numericValue>configuration.getSegmentSizeZoomArea()[3] && numericValue<=2*configuration.getSegmentSizeZoomArea()[3]){
	        			insersionColor=new Color(92,172,238);
	        		}
	        		else if(numericValue>2*configuration.getSegmentSizeZoomArea()[3] && numericValue<=3*configuration.getSegmentSizeZoomArea()[3]){
	        			
	        			insersionColor=new Color(28,134,238);
	        		}
	        		else{
	        			insersionColor=new Color(16,78,139);
	        		}
	        		c.setBackground(insersionColor);
		        	
		        	return c;
		        }
		        catch(Exception e){
		        		

		        	
	        		if(tmpValue.equals("")){
	        			c.setBackground(Color.DARK_GRAY);
	        			return c; 
	        		}
	        		else{
	        			if(columnName.contains("v")){
	        				c.setBackground(Color.lightGray);
	        				setToolTipText(columnName);
	        			}
	        			else{
	        				Color tableNameColor=new Color(205,175,149);
	        				c.setBackground(tableNameColor);
	        			}
		        		return c; 
	        		}
		        		
		        		
		        }
		    }
		});
		
		zoomTable.addMouseListener(new MouseAdapter() {
			@Override
			   public void mouseClicked(MouseEvent e) {
				
				if (e.getClickCount() == 1) {
					JTable target = (JTable)e.getSource();
			         
			         configuration.setSelectedRowsFromMouse(target.getSelectedRows());
			         configuration.setSelectedColumnZoomArea(target.getSelectedColumn());
			         configuration.getZoomAreaTable().repaint();
				}
			   
			   }
		});
		
		zoomTable.addMouseListener(new MouseAdapter() {
			@Override
			   public void mouseReleased(MouseEvent e) {
				
					if(SwingUtilities.isRightMouseButton(e)){
						System.out.println("Right Click");

							JTable target1 = (JTable)e.getSource();
							configuration.setSelectedColumnZoomArea(target1.getSelectedColumn());
							configuration.setSelectedRowsFromMouse(target1.getSelectedRows());
							System.out.println(target1.getSelectedColumn());
							System.out.println(target1.getSelectedRow());
							
							configuration.setTablesSelected(new ArrayList<String>());

							for(int rowsSelected=0; rowsSelected<configuration.getSelectedRowsFromMouse().length; rowsSelected++){
								configuration.getTablesSelected().add((String) zoomTable.getValueAt(configuration.getSelectedRowsFromMouse()[rowsSelected], 0));
								System.out.println(configuration.getTablesSelected().get(rowsSelected));
							}
			            	if (zoomTable.getColumnName(configuration.getSelectedColumnZoomArea()).contains("Phase")) {

								final JPopupMenu popupMenu = new JPopupMenu();
						        JMenuItem showDetailsItem = new JMenuItem("Show Details");
						        showDetailsItem.addActionListener(new ActionListener() {

						            @Override
						            public void actionPerformed(ActionEvent e) {
						            	configuration.setFirstLevelUndoColumnsZoomArea(configuration.getFinalColumnsZoomArea());
						            	configuration.setFirstLevelUndoRowsZoomArea(configuration.getFinalRowsZoomArea());
						            	SelectionToZoomAreaWidget widget = new SelectionToZoomAreaWidget();
						            	widget.showSelectionToZoomArea(configuration.getSelectedColumnZoomArea(), globalDataKeeper, configuration);
										
						            	
						            }
						        });
						        popupMenu.add(showDetailsItem);
						        popupMenu.show(zoomTable, e.getX(),e.getY());
			            	}
						        
						
					}
				
			   }
		});
		
		// listener
		zoomTable.getTableHeader().addMouseListener(new MouseAdapter() {
		    @Override
		    public void mouseClicked(MouseEvent e) {
		    	configuration.setWholeColZoomArea(zoomTable.columnAtPoint(e.getPoint()));
		        String name = zoomTable.getColumnName(configuration.getWholeColZoomArea());
		        System.out.println("Column index selected " + configuration.getWholeCol() + " " + name);
		        zoomTable.repaint();
		    }
		});
		
		zoomTable.getTableHeader().addMouseListener(new MouseAdapter() {
		    @Override
		    public void mouseReleased(MouseEvent e) {
		    	if(SwingUtilities.isRightMouseButton(e)){
					System.out.println("Right Click");
					
							final JPopupMenu popupMenu = new JPopupMenu();
					        JMenuItem showDetailsItem = new JMenuItem("Clear Column Selection");
					        showDetailsItem.addActionListener(new ActionListener() {

					            @Override
					            public void actionPerformed(ActionEvent e) {
					            	configuration.setWholeColZoomArea(-1);
					            	zoomTable.repaint();
					            }
					        });
					        popupMenu.add(showDetailsItem);
					        popupMenu.show(zoomTable, e.getX(),e.getY());
					    
				}
			
		   }
		    
		});
		
		
		configuration.setZoomAreaTable(zoomTable);
		
		configuration.getTmpScrollPaneZoomArea().setViewportView(configuration.getZoomAreaTable());
		configuration.getTmpScrollPaneZoomArea().setAlignmentX(0);
		configuration.getTmpScrollPaneZoomArea().setAlignmentY(0);
		configuration.getTmpScrollPaneZoomArea().setBounds(300,300,950,250);
		configuration.getTmpScrollPaneZoomArea().setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		configuration.getTmpScrollPaneZoomArea().setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		

		//configuration.getLifeTimePanel().setCursor(getCursor());
		configuration.getLifeTimePanel().add(configuration.getTmpScrollPaneZoomArea());
		
		
		
	}


}
