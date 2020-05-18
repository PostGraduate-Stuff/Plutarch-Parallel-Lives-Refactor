package  gui.widgets;

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

import data.dataKeeper.Description;
import data.dataKeeper.GlobalDataKeeper;
import gui.configurations.Configuration;
import  gui.tableElements.commons.JvTable;
import  gui.tableElements.commons.MyTableModel;

public class GeneralTablePhasesWidget 
{
	private JvTable generalTable;
	private Configuration configuration;
	private GlobalDataKeeper globalDataKeeper;
	
	public GeneralTablePhasesWidget(Configuration configuration,GlobalDataKeeper globalDataKeeper) {
		this.configuration = configuration;
		this.globalDataKeeper = globalDataKeeper;
	}
	
	public void initializeGeneralTablePhases(){
		generalTable = makeGeneralTablePhases();
		setMoreTableListeners();
	}
	
	public JvTable makeGeneralTablePhases() {
		
		System.out.println("GeneralTable Phases Rows");
		int numberOfColumns=configuration.getFinalRows()[0].length;
		int numberOfRows=configuration.getFinalRows().length;
		System.out.println("numberOfColumns:  "+ numberOfColumns);
		System.out.println("numberOfRows:  "+ numberOfRows);
		
		String[][] rows=new String[numberOfRows][numberOfColumns];
		
		for(int i=0; i<numberOfRows; i++){
			
			rows[i][0]=configuration.getFinalRows()[i][0];
			System.out.println(rows[i][0]);
		}
		
		MyTableModel generalModel=new MyTableModel(configuration.getFinalColumns(), rows);
		
		final JvTable generalTable=new JvTable(generalModel);
		
		generalTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		
		generalTable.setShowGrid(false);
		generalTable.setIntercellSpacing(new Dimension(0, 0));
		
		
		generalTable.getColumnModel().getColumn(0).setPreferredWidth(86);
		for(int i=1; i<generalTable.getColumnCount(); i++)
		{
			generalTable.getColumnModel().getColumn(i).setPreferredWidth(1);
		}
		return generalTable;
	}
	
	public void setMoreTableListeners()
	{
		generalTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer()
		{
			public static final long serialVersionUID = 1L;

			@Override
		    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
		    {
		        final Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		        	        
		        String tmpValue=configuration.getFinalRows()[row][column];
		        String columnName=table.getColumnName(column);
		        Color fr=new Color(0,0,0);
		        c.setForeground(fr);
		        
		        if(column==configuration.getWholeCol() && configuration.getWholeCol()!=0){
		        	Description descriptionTag = new Description(globalDataKeeper);
		        	String description=table.getColumnName(column)+"\n";
		          	description=description+descriptionTag.getPhasesDescription(column);
	        		configuration.getDescriptionText().setText(description);
		        	
		        	Color cl = new Color(255,69,0,100);

	        		c.setBackground(cl);
	        		return c;
		        }
		        else if(configuration.getSelectedColumn()==0){
		        	if (isSelected){
		        		Description descriptionTag = new Description(globalDataKeeper);
		        		if(configuration.getFinalRows()[row][0].contains("Cluster")){
			        		String description="Cluster:"+configuration.getFinalRows()[row][0]+"\n";
				          	description=description+descriptionTag.getClusterDescription(row);
			        		configuration.getDescriptionText().setText(description);
		        		}
		        		else{
		        			String description=descriptionTag.getBirthDeathDescription(configuration.getFinalRows()[row][0]);
		        			description=description+"Total Changes:"+globalDataKeeper.getAllPPLTables().get(configuration.getFinalRowsZoomArea()[row][0]).getTotalChanges()+"\n";
			        		configuration.getDescriptionText().setText(description);
		        		}
		        		Color cl = new Color(255,69,0,100);
		        		c.setBackground(cl);
		        		return c;
		        	}
		        }
		        else{
		        	
		        	if(configuration.getSelectedFromTree().contains(configuration.getFinalRows()[row][0])){

		        		Color cl = new Color(255,69,0,100);
		        		
		        		c.setBackground(cl);
		        		
		        		return c;
		        	}
		        	
		        	if (isSelected && hasFocus){
			        	
		        		String description="";
		        		if(!table.getColumnName(column).contains("Table name")){
		        			Description descriptionHelper = new Description(globalDataKeeper);
			        		if(configuration.getFinalRows()[row][0].contains("Cluster")){

				        		description=configuration.getFinalRows()[row][0]+"\n";
				        		description=description+"Tables:"+globalDataKeeper.getClusterCollectors().get(0).getClusters().get(row).getNamesOfTables().size()+"\n\n";

				        		description=description+table.getColumnName(column)+"\n";
				        		description = description+descriptionHelper.getTransitionsOfPhasesTitles(column);
				        		description=description+"Total Changes For This Phase:"+tmpValue+"\n";
				        		
			        		}
			        		else{
			        			description=table.getColumnName(column)+"\n";
			        			description = description+descriptionHelper.getTransitionsOfPhasesTitles(column);
			        			description = description+descriptionHelper.getBirthDeathDescription(configuration.getFinalRows()[row][0]);
				        		description=description+"Total Changes For This Phase:"+tmpValue+"\n";
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
	        		else if(numericValue> 0&& numericValue<=configuration.getSegmentSize()[3]){
	        			
	        			insersionColor=new Color(176,226,255);
		        	}
	        		else if(numericValue>configuration.getSegmentSize()[3] && numericValue<=2*configuration.getSegmentSize()[3]){
	        			insersionColor=new Color(92,172,238);
	        		}
	        		else if(numericValue>2*configuration.getSegmentSize()[3] && numericValue<=3*configuration.getSegmentSize()[3]){
	        			
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
	        			c.setBackground(Color.gray);
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
					configuration.setSelectedColumn(target.getSelectedColumn());
					configuration.getLifeTimeTable().repaint();
				}

			   }
		});
		
		generalTable.addMouseListener(new MouseAdapter() {
			@Override
			   public void mouseReleased(MouseEvent e) {
					if(e.getButton() == MouseEvent.BUTTON3){
						System.out.println("Right Click");

						JTable target1 = (JTable)e.getSource();
						configuration.setSelectedColumn(target1.getSelectedColumn());
						configuration.setSelectedRowsFromMouse(new int[target1.getSelectedRows().length]);
						configuration.setSelectedRowsFromMouse(target1.getSelectedRows());
						
						final String sSelectedRow = (String) generalTable.getValueAt(target1.getSelectedRow(),0);
						configuration.setTablesSelected(new ArrayList<String>());

						for(int rowsSelected=0; rowsSelected<configuration.getSelectedRowsFromMouse().length; rowsSelected++){
							configuration.getTablesSelected().add((String) generalTable.getValueAt(configuration.getSelectedRowsFromMouse()[rowsSelected], 0));
						}
						
						JPopupMenu popupMenu = new JPopupMenu();
				        JMenuItem showDetailsItem = new JMenuItem("Show Details for the selection");
				        showDetailsItem.addActionListener(new ActionListener() {

				            @Override
				            public void actionPerformed(ActionEvent le) {
				            	if(sSelectedRow.contains("Cluster ")){
				            		ClusterSelectionToZoomAreaWidget clusterSelectionToZoomArea = new ClusterSelectionToZoomAreaWidget(configuration, globalDataKeeper, configuration.getSelectedColumn());
				            		clusterSelectionToZoomArea.showClusterSelectionToZoomArea();
				            	}
				            	else{
				            		SelectionToZoomAreaWidget selectionToZoomAreaWidget = new SelectionToZoomAreaWidget();
				            		selectionToZoomAreaWidget.showSelectionToZoomArea(configuration.getSelectedColumn(), globalDataKeeper, configuration);
				            	}
				            }
				        });
				        popupMenu.add(showDetailsItem);
				        JMenuItem clearSelectionItem = new JMenuItem("Clear Selection");
				        clearSelectionItem.addActionListener(new ActionListener() {

				            @Override
				            public void actionPerformed(ActionEvent le) {
				            	
				            	configuration.setSelectedFromTree(new ArrayList<String>());
				            	configuration.getLifeTimeTable().repaint();
				            }
				        });
				        popupMenu.add(clearSelectionItem);
				        popupMenu.show(generalTable, e.getX(),e.getY());
					}
			   }
		});
		
		generalTable.getTableHeader().addMouseListener(new MouseAdapter() {
		    @Override
		    public void mouseClicked(MouseEvent e) {
		    	configuration.setWholeCol(generalTable.columnAtPoint(e.getPoint()));
		        String name = generalTable.getColumnName(configuration.getWholeCol());
		        System.out.println("Column index selected " + configuration.getWholeCol() + " " + name);
		        generalTable.repaint();
		        if (configuration.isShowingPld()) {
		        	GeneralTableIDUWidget widget = new GeneralTableIDUWidget(configuration, globalDataKeeper);
		        	widget.makeGeneralTableIDU();
				}
		    }
		});
		
		generalTable.getTableHeader().addMouseListener(new MouseAdapter() {
		    @Override
		    public void mouseReleased(MouseEvent e) {
		    	if(SwingUtilities.isRightMouseButton(e)){
					System.out.println("Right Click");
					
							final JPopupMenu popupMenu = new JPopupMenu();
					        JMenuItem clearColumnSelectionItem = new JMenuItem("Clear Column Selection");
					        clearColumnSelectionItem.addActionListener(new ActionListener() {

					            @Override
					            public void actionPerformed(ActionEvent e) {
					            	configuration.setWholeCol(-1);
					            	generalTable.repaint();
					            	if(configuration.isShowingPld()){
					            		GeneralTableIDUWidget widget = new GeneralTableIDUWidget(configuration, globalDataKeeper);
							        	widget.makeGeneralTableIDU();
					            	}
					            }
					        });
					        popupMenu.add(clearColumnSelectionItem);
					        JMenuItem showDetailsItem = new JMenuItem("Show Details for this Phase");
					        showDetailsItem.addActionListener(new ActionListener() {

					            @Override
					            public void actionPerformed(ActionEvent e) {
									String sSelectedRow=configuration.getFinalRows()[0][0];
									System.out.println("?"+sSelectedRow);
									configuration.setTablesSelected(new ArrayList<String>());
					            	for(int i=0; i<configuration.getFinalRows().length; i++)
					            		configuration.getTablesSelected().add((String) generalTable.getValueAt(i, 0));

					            	if(!sSelectedRow.contains("Cluster ")){
					            		SelectionToZoomAreaWidget widget = new SelectionToZoomAreaWidget();
					            		widget.showSelectionToZoomArea(configuration.getWholeCol(), globalDataKeeper, configuration);
					            	}
					            	else{
					            		ClusterSelectionToZoomAreaWidget widget = new ClusterSelectionToZoomAreaWidget(configuration, globalDataKeeper, configuration.getWholeCol());
					            		widget.showClusterSelectionToZoomArea();
					            	}
					            	
					            }
					        });
					        popupMenu.add(showDetailsItem);
					        popupMenu.show(generalTable, e.getX(),e.getY());
				}
		   }
		});
		setSettingsConfigurations();
	}
	private void setSettingsConfigurations(){
		configuration.setLifeTimeTable(generalTable);
		
		configuration.getTmpScrollPane().setViewportView(configuration.getLifeTimeTable());
		configuration.getTmpScrollPane().setAlignmentX(0);
		configuration.getTmpScrollPane().setAlignmentY(0);
		configuration.getTmpScrollPane().setBounds(300,30,950,265);
		configuration.getTmpScrollPane().setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		configuration.getTmpScrollPane().setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
	    
		configuration.getLifeTimePanel().add(configuration.getTmpScrollPane());
	}
}
