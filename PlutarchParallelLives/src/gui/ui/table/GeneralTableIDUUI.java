package gui.ui.table;

import java.awt.Color;
import java.awt.Component;
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
import gui.configurations.*;
import gui.tableElements.commons.JvTable;
import gui.tableElements.tableRenderers.IDUTableRenderer;
import phaseAnalyzer.commons.Phase;
import services.DataService;

public class GeneralTableIDUUI extends TableUI{
	
	private GlobalDataKeeper globalDataKeeper;
	
	public GeneralTableIDUUI(final GuiConfiguration configuration, final DataConfiguration dataConfiguration, final DataTablesConfiguration tablesConfiguration,GlobalDataKeeper globalDataKeeper) {

		super(configuration,dataConfiguration,tablesConfiguration);
		this.globalDataKeeper = globalDataKeeper;
	}

	public void makeGeneralTableIDU (){
		DataService service = new DataService();
		String[][] sortedRows = service.sortRows(dataConfiguration.getFinalRowsZoomArea(), globalDataKeeper.getAllPPLTables());
		setSortedRows(sortedRows);
		configuration.setPLDVisibility(true);
	}
	
	public void setSortedRows(String[][] sortedRows)
	{
		dataConfiguration.setFinalRowsZoomArea(sortedRows);
		configuration.setSelectedRows(new ArrayList<Integer>());
	}
	

	
	public JvTable constructGeneralTableIDU() 
	{
		ArrayList<Phase> initialPhases = new ArrayList<>();
		if(globalDataKeeper.getPhaseCollectors() != null  && 
				globalDataKeeper.getPhaseCollectors().size() >0 && 
				globalDataKeeper.getPhaseCollectors().get(0) != null)
		{
			initialPhases = globalDataKeeper.getPhaseCollectors().get(0).getPhases();
		}
		
		JvTableUI jvTableWidget = new JvTableUI(configuration, dataConfiguration, initialPhases);
		table = jvTableWidget.makeGeneralTableIDU();
		setGeneralTablesListeners();

		return table;
	}
	
	private void setGeneralTablesListeners(){
		final IDUTableRenderer renderer = new IDUTableRenderer(configuration,dataConfiguration.getFinalRowsZoomArea(),globalDataKeeper, dataConfiguration.getSegmentSize());
		table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer()
		{
			private static final long serialVersionUID = 1L;

			@Override
		    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
		    {
		        final Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		        
		        String tmpValue=dataConfiguration.getFinalRowsZoomArea()[row][column];
		        String columnName=table.getColumnName(column);
		        Color fr=new Color(0,0,0);
		        c.setForeground(fr);
		        setOpaque(true);
		      
		        if(column == configuration.getWholeColZoomArea() && configuration.getWholeColZoomArea()!=0){
		        	
		        	Description descriptionTag = new Description(globalDataKeeper);
		        	String description= descriptionTag.getTransitionDescription(column);
        			configuration.getDescriptionText().setText(description);
        			Color cl = new Color(255,69,0,100);
        			c.setBackground(cl);
        			return c;
		        }
		        else if(configuration.getSelectedColumnZoomArea()==0){
		    		
		        	if (isSelected){
		        		Description descriptionTag = new Description(globalDataKeeper);
			        	String description= descriptionTag.getBirthDeathDescription(dataConfiguration.getFinalRowsZoomArea()[row][0]);
			        	description=description+"Total Changes:"+globalDataKeeper.getAllPPLTables().get(dataConfiguration.getFinalRowsZoomArea()[row][0]).getTotalChanges()+"\n";
	        			configuration.getDescriptionText().setText(description);
	        			
	        			Color cl = new Color(255,69,0,100);
		        		c.setBackground(cl);
		        		return c;
		        	}
		        }
		        else{
		        	if(configuration.getSelectedFromTree().contains(dataConfiguration.getFinalRowsZoomArea()[row][0])){
		        		Color cl = new Color(255,69,0,100);
		        		c.setBackground(cl);
		        		return c;
		        	}
			      
		        	if (isSelected && hasFocus){
		        		Description descriptionTag = new Description(globalDataKeeper);
		        		String description="";
		        		if(!table.getColumnName(column).contains("Table name")){
		        			if(globalDataKeeper.getAllPPLTables().get(dataConfiguration.getFinalRowsZoomArea()[row][0]).getTableChanges().getTableAtomicChangeForOneTransition(Integer.parseInt(table.getColumnName(column)))!=null){
		    		        	description= descriptionTag.getTransitionDescription(column);
		            			configuration.getDescriptionText().setText(description);
			        		}
			        		else{
		    		        	description= descriptionTag.getEmptyDescription(column);
		            			configuration.getDescriptionText().setText(description);
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
					setToolTipText(Integer.toString(numericValue));
					Color insersionColor=setInsertionColourForTable(numericValue, dataConfiguration.getSegmentSizeZoomArea()[3]);
	        		c.setBackground(insersionColor);
		        	
		        	return c;
		        }
		        catch(Exception e){
	        		if(tmpValue.equals("")){
	        			c.setBackground(Color.GRAY);
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
				
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				
				if (e.getClickCount() == 1) {
					JTable target = (JTable)e.getSource();
				     
					configuration.setSelectedRowsFromMouse(target.getSelectedRows());
					configuration.setSelectedColumnZoomArea(target.getSelectedColumn());
				    renderer.setSelCol(configuration.getSelectedColumnZoomArea());
				    target.getSelectedColumns();
				    tablesConfiguration.getZoomAreaTable().repaint();
				}
			}
		});
		
		table.addMouseListener(new MouseAdapter() {
			@Override
			   public void mouseReleased(MouseEvent e) {
					if(SwingUtilities.isRightMouseButton(e)){
						System.out.println("Right Click");
						JTable target1 = (JTable)e.getSource();
						target1.getSelectedColumns();
						configuration.setSelectedRowsFromMouse(target1.getSelectedRows());
						System.out.println(target1.getSelectedColumns().length);
						System.out.println(target1.getSelectedRow());
						for(int rowsSelected=0; rowsSelected<configuration.getSelectedRowsFromMouse().length; rowsSelected++){
							System.out.println(table.getValueAt(configuration.getSelectedRowsFromMouse()[rowsSelected], 0));
						}
						final JPopupMenu popupMenu = new JPopupMenu();
				        JMenuItem showDetailsItem = new JMenuItem("Clear Selection");
				        showDetailsItem.addActionListener(new ActionListener() {

				            @Override
				            public void actionPerformed(ActionEvent e) {
				            	configuration.setSelectedFromTree(new ArrayList<String>());
				            	tablesConfiguration.getZoomAreaTable().repaint();
				            }
				        });
				        popupMenu.add(showDetailsItem);
				        popupMenu.show(table, e.getX(),e.getY());
					}
			   }
		});
		
		table.getTableHeader().addMouseListener(new MouseAdapter() {
		    @Override
		    public void mouseClicked(MouseEvent e) {
		    	configuration.setWholeColZoomArea(table.columnAtPoint(e.getPoint()));
		        renderer.setWholeCol(table.columnAtPoint(e.getPoint()));
		        table.repaint();
		    }
		});
		
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
					        renderer.setWholeCol(configuration.getWholeColZoomArea());

					        table.repaint();
			            }
			        });
			        popupMenu.add(showDetailsItem);
			        popupMenu.show(table, e.getX(),e.getY());
				}
		   }
		});
		
		setSettingsConfigurations();
	}
}
