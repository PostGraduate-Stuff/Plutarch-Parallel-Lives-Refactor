package gui.ui.table;

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
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableCellRenderer;

import data.dataKeeper.Description;
import data.dataKeeper.GlobalDataKeeper;
import gui.configurations.DataConfiguration;
import gui.configurations.DataTablesConfiguration;
import gui.configurations.GuiConfiguration;
import gui.tableElements.commons.JvTable;
import gui.tableElements.commons.MyTableModel;
import gui.ui.zoom.SelectionToZoomAreaUI;

public class ZoomAreaTableForClusterUI extends TableUI {

	private GlobalDataKeeper globalDataKeeper;
	
	private Description descriptionHelper;
	
	public ZoomAreaTableForClusterUI(final GuiConfiguration configuration, final GlobalDataKeeper globalDataKeeper, final DataTablesConfiguration tablesConfiguration, final DataConfiguration dataConfiguration){
		super(configuration, dataConfiguration, tablesConfiguration);
		this.globalDataKeeper = globalDataKeeper;
		this.descriptionHelper = new Description(globalDataKeeper);
	}
	
	public void makeZoomAreaTableForCluster() {
		configuration.setShowingPld(false);
		
		configuration.getUndoButton().setVisible(true);
		
		final String[][] rowsZoom = createZoomArea();
		
		tablesConfiguration.setZoomModel(new MyTableModel(dataConfiguration.getFinalColumnsZoomArea(), rowsZoom));
		
		createtable(); 
		
		setDefaultRender();
	
		setLeftClickListener();
		setRightClickListener();
		
		setTableLeftClickListener();
		setTableRightClickListener();
	
		setSettingsConfigurations();
	}

	

	private void setTableRightClickListener() {
		table.getTableHeader().addMouseListener(new MouseAdapter() {
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
					            	table.repaint();
					            }
					        });
					        popupMenu.add(showDetailsItem);
					        popupMenu.show(table, e.getX(),e.getY());
					    
				}
			
		   }
		    
		});
	}

	private void setTableLeftClickListener() {
		table.getTableHeader().addMouseListener(new MouseAdapter() {
		    @Override
		    public void mouseClicked(MouseEvent e) {
		    	configuration.setWholeColZoomArea(table.columnAtPoint(e.getPoint()));
		        String name = table.getColumnName(configuration.getWholeColZoomArea());
		        System.out.println("Column index selected " + configuration.getWholeCol() + " " + name);
		        table.repaint();
		    }
		});
		
	}

	private void setDefaultRender() {

		table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer()
		{
		    
			public static final long serialVersionUID = 1L;

			@Override
		    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
		    {
		        final Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		        
		        
		        
		        String tmpValue=dataConfiguration.getFinalRowsZoomArea()[row][column];
		        String columnName=table.getColumnName(column);
		        Color fr=new Color(0,0,0);
		        c.setForeground(fr);
		        
		        if(column==configuration.getWholeColZoomArea() && configuration.getWholeColZoomArea()!=0){
		        	
		        	String description=table.getColumnName(column)+"\n";
		          	description=description+descriptionHelper.getPhasesDescription(column);
		        	
	        		configuration.getDescriptionText().setText(description);
		        	
		        	Color cl = new Color(255,69,0,100);

	        		c.setBackground(cl);
	        		return c;
		        }
		        else if(configuration.getSelectedColumnZoomArea()==0){
		        	if (isSelected){
		        		String description="Table:"+dataConfiguration.getFinalRowsZoomArea()[row][0]+"\n";
		        		description=description+descriptionHelper.getBirthDeathDescription(dataConfiguration.getFinalRowsZoomArea()[row][0]);
		        		description=description+"Total Changes:"+globalDataKeeper.getAllPPLTables().get(dataConfiguration.getFinalRowsZoomArea()[row][0]).getTotalChanges()+"\n";
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
		        			int oldNewVersionNumber = getOldNewVersionNumber(table.getColumnName(column));
		        			description="Transition "+table.getColumnName(column)+"\n";
		        			description = description + descriptionHelper.getOldNewVersionDescription(oldNewVersionNumber);
		        			description=description+"Table:"+dataConfiguration.getFinalRowsZoomArea()[row][0]+"\n";
		        			description=description+descriptionHelper.getBirthDeathDescription(dataConfiguration.getFinalRowsZoomArea()[row][0]);
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
					setToolTipText(Integer.toString(numericValue));
					Color insersionColor=setInsertionColourForZoomTable(numericValue,dataConfiguration.getSegmentSizeZoomArea()[3]);
	        		c.setBackground(insersionColor);
		        	return c;
		        }
		        catch(Exception e){
	        		if(tmpValue.equals("")){
	        			c.setBackground(Color.DARK_GRAY);
	        			return c; 
	        		}
	        		
        			if(columnName.contains("v")){
        				c.setBackground(Color.lightGray);
        				setToolTipText(columnName);
    	        		return c; 
        			}
        			
    				Color tableNameColor=new Color(205,175,149);
    				c.setBackground(tableNameColor);
        			
	        		return c; 
	        		
		        }
		    }
		});
	}

	private void setRightClickListener() {
		table.addMouseListener(new MouseAdapter() {
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
								configuration.getTablesSelected().add((String) table.getValueAt(configuration.getSelectedRowsFromMouse()[rowsSelected], 0));
								System.out.println(configuration.getTablesSelected().get(rowsSelected));
							}
			            	if (table.getColumnName(configuration.getSelectedColumnZoomArea()).contains("Phase")) {

								final JPopupMenu popupMenu = new JPopupMenu();
						        JMenuItem showDetailsItem = new JMenuItem("Show Details");
						        showDetailsItem.addActionListener(new ActionListener() {

						            @Override
						            public void actionPerformed(ActionEvent e) {
						            	dataConfiguration.setFirstLevelUndoColumnsZoomArea(dataConfiguration.getFinalColumnsZoomArea());
						            	dataConfiguration.setFirstLevelUndoRowsZoomArea(dataConfiguration.getFinalRowsZoomArea());
						            	SelectionToZoomAreaUI widget = new SelectionToZoomAreaUI();
						            	widget.showSelectionToZoomArea(configuration.getSelectedColumnZoomArea(), globalDataKeeper, configuration, dataConfiguration, tablesConfiguration);
										
						            	
						            }
						        });
						        popupMenu.add(showDetailsItem);
						        popupMenu.show(table, e.getX(),e.getY());
			            	}
					}
				
			   }
		});
	}

	private void setLeftClickListener() {
		table.addMouseListener(new MouseAdapter() {
			@Override
			   public void mouseClicked(MouseEvent e) {
				
				if (e.getClickCount() == 1) {
					JTable target = (JTable)e.getSource();
			         
			         configuration.setSelectedRowsFromMouse(target.getSelectedRows());
			         configuration.setSelectedColumnZoomArea(target.getSelectedColumn());
			         tablesConfiguration.getZoomAreaTable().repaint();
				}
			   
			   }
		});
	}

	private JvTable createtable() {
		table=new JvTable(tablesConfiguration.getZoomModel());
		
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		
		
		table.setShowGrid(false);
		table.setIntercellSpacing(new Dimension(0, 0));
		

		table.getColumnModel().getColumn(0).setPreferredWidth(150);
		for(int i=1; i<table.getColumnCount(); i++){
			table.getColumnModel().getColumn(i).setPreferredWidth(10);
			table.getColumnModel().getColumn(i).setMaxWidth(10);
			table.getColumnModel().getColumn(i).setMinWidth(70);
		}
		
		return table;
	}

	private String[][] createZoomArea() {
		int numberOfColumns = dataConfiguration.getFinalRowsZoomArea()[0].length;
		int numberOfRows = dataConfiguration.getFinalRowsZoomArea().length;
		String[][] rowsZoom = new String[numberOfRows][numberOfColumns];

		for(int i=0; i<numberOfRows; i++){
			rowsZoom[i][0]=dataConfiguration.getFinalRowsZoomArea()[i][0];
		}
		
		return rowsZoom;
	}

	private int getOldNewVersionNumber(String name){
		if(name.contains("Phase")){
			String[] stringArray = name.split(" ");
			if(stringArray.length<2){
				return 0;
			}
			name = stringArray[1];
		}
		return Integer.parseInt(name);
	}
}
