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
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableCellRenderer;

import data.dataKeeper.Description;
import data.dataKeeper.GlobalDataKeeper;
import gui.configurations.Configuration;
import gui.tableElements.commons.JvTable;
import gui.tableElements.commons.MyTableModel;

public class ZoomAreaTableWidget extends TableWidget{
	private GlobalDataKeeper globalDataKeeper;
	private Description descriptionHelper;
	
	public ZoomAreaTableWidget(Configuration configuration, GlobalDataKeeper globalDataKeeper){
		super(configuration);
		this.globalDataKeeper = globalDataKeeper;
		descriptionHelper = new Description(globalDataKeeper);
	}

	public void makeZoomAreaTable() {
		configuration.setShowingPld(false);
		String[][] rowsZoom = creatRowstable();
		configuration.setZoomModel(new MyTableModel(configuration.getFinalColumnsZoomArea(), rowsZoom));
		createtable(); 
		setDefaultRender(rowsZoom);
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
						final ArrayList<String> tablesSelected = new ArrayList<String>();
						for(int rowsSelected=0; rowsSelected<configuration.getSelectedRowsFromMouse().length; rowsSelected++){
							tablesSelected.add((String) table.getValueAt(configuration.getSelectedRowsFromMouse()[rowsSelected], 0));
							System.out.println(tablesSelected.get(rowsSelected));
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
			         
					configuration.setSelectedRowsFromMouse( target.getSelectedRows());
					configuration.setSelectedColumnZoomArea(target.getSelectedColumn());
					configuration.getZoomAreaTable().repaint();
				}
			    
			   }
		});
	}

	private void setDefaultRender(final String[][] rowsZoom) {
		
		table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer()
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
		        
		        if(column==	configuration.getWholeColZoomArea()){
		        	String description = descriptionHelper.getTransitionDescription(Integer.parseInt(table.getColumnName(column)));
	    			configuration.getDescriptionText().setText(description);
		        	Color cl = new Color(255,69,0,100);
	        		c.setBackground(cl);
	        		
	        		return c;
		        }
		        else if(configuration.getSelectedColumnZoomArea()==0){
		        	if (isSelected){
		        		String description=descriptionHelper.getBirthDeathDescription(configuration.getFinalRowsZoomArea()[row][0]);
		        		description=description+"Total Changes:"+globalDataKeeper.getAllPPLTables().get(configuration.getFinalRowsZoomArea()[row][0]).
		        				getTotalChangesForOnePhase(Integer.parseInt(table.getColumnName(1)), Integer.parseInt(table.getColumnName(table.getColumnCount()-1)))+"\n";
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
			        		        		
			        		if(globalDataKeeper.getAllPPLTables().get(configuration.getFinalRowsZoomArea()[row][0]).getTableChanges().getTableAtChForOneTransition(Integer.parseInt(table.getColumnName(column)))!=null)
			        		{
			        			description=description+"Transition Changes:"+globalDataKeeper.getAllPPLTables().get(configuration.getFinalRowsZoomArea()[row][0]).
			        				getTableChanges().getTableAtChForOneTransition(Integer.parseInt(table.getColumnName(column))).size()+"\n";
			        			description=description+"Additions:"+globalDataKeeper.getAllPPLTables().get(configuration.getFinalRowsZoomArea()[row][0]).
			        					getNumberOfAdditionsForOneTr(Integer.parseInt(table.getColumnName(column)))+"\n";
			        			description=description+"Deletions:"+globalDataKeeper.getAllPPLTables().get(configuration.getFinalRowsZoomArea()[row][0]).
			        					getNumberOfDeletionsForOneTr(Integer.parseInt(table.getColumnName(column)))+"\n";
			        			description=description+"Updates:"+globalDataKeeper.getAllPPLTables().get(configuration.getFinalRowsZoomArea()[row][0]).
			        					getNumberOfUpdatesForOneTr(Integer.parseInt(table.getColumnName(column)))+"\n";
			        			
			        		}
			        		else{
			        			description = descriptionHelper.getEmptyDescription(column);
			        			
			        		}
			        		
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
		
	}

	private void createtable() {
		table=new JvTable(configuration.getZoomModel());
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		table.setShowGrid(false);
		table.setIntercellSpacing(new Dimension(0, 0));
		table.getColumnModel().getColumn(0).setPreferredWidth(150);
		for(int i=1; i<table.getColumnCount(); i++){
			table.getColumnModel().getColumn(i).setPreferredWidth(20);
			table.getColumnModel().getColumn(i).setMaxWidth(20);
			table.getColumnModel().getColumn(i).setMinWidth(20);
		}
	
	}

	private String[][] creatRowstable() {
		int numberOfColumns=configuration.getFinalRowsZoomArea()[0].length;
		int numberOfRows=configuration.getFinalRowsZoomArea().length;
		String[][] rowsZoom=new String[numberOfRows][numberOfColumns];
		for(int i=0; i<numberOfRows; i++){
			rowsZoom[i][0]=configuration.getFinalRowsZoomArea()[i][0];
		}
		return rowsZoom;
	}

}
