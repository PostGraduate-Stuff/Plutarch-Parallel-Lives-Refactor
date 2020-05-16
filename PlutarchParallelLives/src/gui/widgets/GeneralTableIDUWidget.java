package gui.widgets;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableCellRenderer;

import data.dataKeeper.GlobalDataKeeper;
import gui.configurations.*;
import gui.mainEngine.Gui;
import gui.tableElements.commons.JvTable;
import gui.tableElements.tableRenderers.IDUTableRenderer;
import phaseAnalyzer.commons.Phase;
import services.DataService;

public class GeneralTableIDUWidget {
	private JvTable generalTable;
//	private Gui gui;
	private Configuration configuration;
	private GlobalDataKeeper globalDataKeeper;
	
	public GeneralTableIDUWidget(/*Gui gui,*/ Configuration configuration,GlobalDataKeeper globalDataKeeper) {

		/*this.gui = gui;*/
		this.configuration = configuration;
		this.globalDataKeeper = globalDataKeeper;
	}

	public void makeGeneralTableIDU (){
		DataService service = new DataService();
		String[][] sortedRows = service.sortRows(configuration.getFinalRowsZoomArea(), globalDataKeeper.getAllPPLTables());
		
		setSortedRows(sortedRows);
		setPLDVisibility(true);
	}
	
	public void setSortedRows(String[][] sortedRows)
	{
		//makeGeneralTableIDU
		
		configuration.setFinalRowsZoomArea(sortedRows);
		configuration.setSelectedRows(new ArrayList<Integer>());
	}
	
	public void setPLDVisibility(boolean action)
	{
		configuration.setShowingPld(action);
		configuration.getZoomInButton().setVisible(action);
		JButton btn = configuration.getZoomInButton();
		configuration.getZoomOutButton().setVisible(action);
		configuration.getShowThisToPopup().setVisible(action); 
		
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
		
		
		JvTableWidget jvTableWidget = new JvTableWidget(configuration, initialPhases,  globalDataKeeper);
		generalTable = jvTableWidget.makeGeneralTableIDU();
		setGeneralTablesListeners();

		return generalTable;
	}
	
	private void setGeneralTablesListeners(){
		final IDUTableRenderer renderer = new IDUTableRenderer(configuration,configuration.getFinalRowsZoomArea(),globalDataKeeper, configuration.getSegmentSize());
		//generalTable.setDefaultRenderer(Object.class, renderer);
		
		
		
		generalTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer()
		{
		    
			private static final long serialVersionUID = 1L;

			@Override
		    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
		    {
		        final Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		        
		        String tmpValue=configuration.getFinalRowsZoomArea()[row][column];
		        String columnName=table.getColumnName(column);
		        Color fr=new Color(0,0,0);
		        
		        c.setForeground(fr);
		        setOpaque(true);
		      
		        if(column == configuration.getWholeColZoomArea() && configuration.getWholeColZoomArea()!=0){
		        	
		        	String description="Transition ID:"+table.getColumnName(column)+"\n";
		        	description=description+"Old Version Name:"+globalDataKeeper.getAllPPLTransitions().
	        				get(Integer.parseInt(table.getColumnName(column))).getOldVersionName()+"\n";
	        		description=description+"New Version Name:"+globalDataKeeper.getAllPPLTransitions().
	        				get(Integer.parseInt(table.getColumnName(column))).getNewVersionName()+"\n";		        		
	        		
        			description=description+"Transition Changes:"+globalDataKeeper.getAllPPLTransitions().get(Integer.parseInt(table.getColumnName(column))).getNumberOfChangesForOneTr()+"\n";
        			description=description+"Additions:"+globalDataKeeper.getAllPPLTransitions().get(Integer.parseInt(table.getColumnName(column))).getNumberOfAdditionsForOneTr()+"\n";
        			description=description+"Deletions:"+globalDataKeeper.getAllPPLTransitions().get(Integer.parseInt(table.getColumnName(column))).getNumberOfDeletionsForOneTr()+"\n";
        			description=description+"Updates:"+globalDataKeeper.getAllPPLTransitions().get(Integer.parseInt(table.getColumnName(column))).getNumberOfUpdatesForOneTr()+"\n";

        			
        			configuration.getDescriptionText().setText(description);
		        	
		        	Color cl = new Color(255,69,0,100);

	        		c.setBackground(cl);
	        		return c;
		        }
		        else if(configuration.getSelectedColumnZoomArea()==0){
		    		
		        	if (isSelected){
		        		Color cl = new Color(255,69,0,100);
		        		c.setBackground(cl);
		        		
		        		String description="Table:"+configuration.getFinalRowsZoomArea()[row][0]+"\n";
		        		description=description+"Birth Version Name:"+globalDataKeeper.getAllPPLTables().get(configuration.getFinalRowsZoomArea()[row][0]).getBirth()+"\n";
		        		description=description+"Birth Version ID:"+globalDataKeeper.getAllPPLTables().get(configuration.getFinalRowsZoomArea()[row][0]).getBirthVersionID()+"\n";
		        		description=description+"Death Version Name:"+globalDataKeeper.getAllPPLTables().get(configuration.getFinalRowsZoomArea()[row][0]).getDeath()+"\n";
		        		description=description+"Death Version ID:"+globalDataKeeper.getAllPPLTables().get(configuration.getFinalRowsZoomArea()[row][0]).getDeathVersionID()+"\n";
		        		description=description+"Total Changes:"+globalDataKeeper.getAllPPLTables().get(configuration.getFinalRowsZoomArea()[row][0]).getTotalChanges()+"\n";

		        		
		        		configuration.getDescriptionText().setText(description);
		        		
		        		return c;
		        		
		        		
		        	}
		        }
		        else{


		        	if(configuration.getSelectedFromTree().contains(configuration.getFinalRowsZoomArea()[row][0])){


		        		Color cl = new Color(255,69,0,100);
		        		
		        		c.setBackground(cl);
		        		
		        		return c;
		        	}
		        	
			      
		        	
		        	if (isSelected && hasFocus){

		        		String description="";
		        		if(!table.getColumnName(column).contains("Table name")){
			        		description="Table:"+configuration.getFinalRowsZoomArea()[row][0]+"\n";
			        		
			        		description=description+"Old Version Name:"+globalDataKeeper.getAllPPLTransitions().
			        				get(Integer.parseInt(table.getColumnName(column))).getOldVersionName()+"\n";
			        		description=description+"New Version Name:"+globalDataKeeper.getAllPPLTransitions().
			        				get(Integer.parseInt(table.getColumnName(column))).getNewVersionName()+"\n";		        		
			        		if(globalDataKeeper.getAllPPLTables().get(configuration.getFinalRowsZoomArea()[row][0]).
			        				getTableChanges().getTableAtChForOneTransition(Integer.parseInt(table.getColumnName(column)))!=null){
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
			        			description=description+"Transition Changes:0"+"\n";
			        			description=description+"Additions:0"+"\n";
			        			description=description+"Deletions:0"+"\n";
			        			description=description+"Updates:0"+"\n";
			        			
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
	        			insersionColor=new Color(154,205,50,200);
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
	        			c.setBackground(Color.GRAY);
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
				
		generalTable.addMouseListener(new MouseAdapter() {
			@Override
			   public void mouseClicked(MouseEvent e) {
				
				if (e.getClickCount() == 1) {
					JTable target = (JTable)e.getSource();
			         
					configuration.setSelectedRowsFromMouse(target.getSelectedRows());
					configuration.setSelectedColumnZoomArea(target.getSelectedColumn());
			         renderer.setSelCol(configuration.getSelectedColumnZoomArea());
			         target.getSelectedColumns();
			         
			         configuration.getZoomAreaTable().repaint();
				}
				
			  }
		});
		
		generalTable.addMouseListener(new MouseAdapter() {
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
							System.out.println(generalTable.getValueAt(configuration.getSelectedRowsFromMouse()[rowsSelected], 0));
						}
						final JPopupMenu popupMenu = new JPopupMenu();
				        JMenuItem showDetailsItem = new JMenuItem("Clear Selection");
				        showDetailsItem.addActionListener(new ActionListener() {

				            @Override
				            public void actionPerformed(ActionEvent e) {
				            	configuration.setSelectedFromTree(new ArrayList<String>());
				            	configuration.getZoomAreaTable().repaint();
				            }
				        });
				        popupMenu.add(showDetailsItem);
				        popupMenu.show(generalTable, e.getX(),e.getY());
						        						        
						    
					}
				
			   }
		});
		
		
		generalTable.getTableHeader().addMouseListener(new MouseAdapter() {
		    @Override
		    public void mouseClicked(MouseEvent e) {
		    	configuration.setWholeColZoomArea(generalTable.columnAtPoint(e.getPoint()));
		        renderer.setWholeCol(generalTable.columnAtPoint(e.getPoint()));
		        //String name = generalTable.getColumnName(wholeColZoomArea);
		        //System.out.println("Column index selected " + wholeColZoomArea + " " + name);
		        generalTable.repaint();
		    }
		});
		
		generalTable.getTableHeader().addMouseListener(new MouseAdapter() {
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

					            	generalTable.repaint();
					            }
					        });
					        popupMenu.add(showDetailsItem);
					        popupMenu.show(generalTable, e.getX(),e.getY());
					    
				}
			
		   }
		    
		});
		
		configuration.setZoomAreaTable(generalTable);
		configuration.getTmpScrollPaneZoomArea().setViewportView(configuration.getZoomAreaTable());
		configuration.getTmpScrollPaneZoomArea().setAlignmentX(0);
		configuration.getTmpScrollPaneZoomArea().setAlignmentY(0);
		configuration.getTmpScrollPaneZoomArea().setBounds(300,300,950,250);
		configuration.getTmpScrollPaneZoomArea().setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		configuration.getTmpScrollPaneZoomArea().setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        
		//configuration.getLifeTimePanel().setCursor(gui.getCursor());
		configuration.getLifeTimePanel().add(configuration.getTmpScrollPaneZoomArea());
	
	
}

	
}
