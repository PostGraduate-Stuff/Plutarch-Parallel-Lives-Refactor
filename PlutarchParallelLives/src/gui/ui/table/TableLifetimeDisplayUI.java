package gui.ui.table;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.GroupLayout;
import javax.swing.JDialog;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.GroupLayout.Alignment;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;

import gui.colorPicker.ColorCache;
import gui.colorPicker.ColourPickerFactory;
import gui.configurations.DataConfiguration;
import gui.configurations.DataTablesConfiguration;
import gui.configurations.GuiConfiguration;
import gui.tableElements.commons.JvTable;
import gui.tableElements.commons.MyTableModel;
import gui.tableElements.tableConstructors.TableConstructionAllSquaresIncluded;
import gui.ui.functionality.DataGeneratorUI;

public class TableLifetimeDisplayUI extends TableUI{
	private final DataGeneratorUI dataGenerator;
	
	public TableLifetimeDisplayUI(final GuiConfiguration configuration, final DataConfiguration dataConfiguration,
			final DataGeneratorUI dataGenerator, final DataTablesConfiguration tablesConfiguration) {
		super(configuration,dataConfiguration,tablesConfiguration);
		this.dataGenerator = dataGenerator;
	}

	public JMenuItem createShowTableMenuItem(){
		
		JMenuItem mntmShowLifetimeTable = new JMenuItem("Show Full Detailed LifeTime Table");
		mntmShowLifetimeTable.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				if(dataGenerator.getPplFile() == null)
				{
					JOptionPane.showMessageDialog(null, "Select a Project first");
					return;
				}
				
				TableConstructionAllSquaresIncluded table=new TableConstructionAllSquaresIncluded(dataGenerator.getGlobalDataKeeper());
				table.constructColumns();
				table.constructRows();
				final String[] columns= table.getConstructedColumns();
				final String[][] rows= table.getConstructedRows();
				dataConfiguration.setSegmentSizeDetailedTable(table.getSegmentSize());
				configuration.getTabbedPane().setSelectedIndex(0);
				makeDetailedTable(columns,rows,true);
			
			}
		});
		return mntmShowLifetimeTable;
	}

	public void makeDetailedTable(String[] columns , String[][] rows, final boolean levelized){
		
		tablesConfiguration.setDetailedModel(new MyTableModel(columns,rows));
		
		final JvTable tmpLifeTimeTable= new JvTable(tablesConfiguration.getDetailedModel());
		
		tmpLifeTimeTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		
		
		assignLifeTimeTable(tmpLifeTimeTable.getColumnModel().getColumn(0),150);
		
		
		for(int i=1; i<tmpLifeTimeTable.getColumnCount(); i++){
			
			if(!levelized){
				assignLifeTimeTable(tmpLifeTimeTable.getColumnModel().getColumn(i),20);
				continue;
			}
			
			if(tmpLifeTimeTable.getColumnName(i).contains("v")){
				assignLifeTimeTable(tmpLifeTimeTable.getColumnModel().getColumn(i),100);

				continue;
			}
			
			assignLifeTimeTable(tmpLifeTimeTable.getColumnModel().getColumn(i),25);
		}
		
		tmpLifeTimeTable.setName("LifeTimeTable");
		
		tmpLifeTimeTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer()
		{
		    
			public static final long serialVersionUID = 1L;

			@Override
		    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
		    {
		        final Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		        
		        String tmpValue=(String) table.getValueAt(row, column);
		        String columnName=table.getColumnName(column);
		        Color fr=new Color(0,0,0);
		        c.setForeground(fr);
		        
		        
		        if(configuration.getSelectedColumn()==0){
		        	if (isSelected){
		        		Color cl = new Color(255,69,0, 100);
		        		c.setBackground(cl);
		        		return c;
		        	}
		        }
		        else{
		        	if (isSelected && hasFocus){
		        		c.setBackground(Color.YELLOW);
		        		return c;
			        }
		        }
		        
		        try{
		        	int numericValue=Integer.parseInt(tmpValue);
		        	Color insersionColor=null;
		        	ColorCache.loadCache();
		        	
		        	
		        	ColourPickerFactory clonedShape = (ColourPickerFactory) ColorCache.getColour(columnName);
		        	insersionColor = clonedShape.draw(numericValue, dataConfiguration);
		        	
		        	c.setBackground(insersionColor);
		        	return c;
		        }
		        catch(Exception e){
		        		
	        		if(tmpValue.equals("")){
	        			c.setBackground(Color.black);
		        		return c; 
	        		}
        			if(columnName.contains("v")){
        				c.setBackground(Color.lightGray);
        				if(levelized==false){
        					setToolTipText(columnName);
        				}
    	        		return c; 
        			}
    				Color tableNameColor=new Color(205,175,149);
    				c.setBackground(tableNameColor);
        			
	        		return c; 
        		}
		    }
		});
		
		tmpLifeTimeTable.setOpaque(true);
	    
		tmpLifeTimeTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
	    tmpLifeTimeTable.getSelectionModel().addListSelectionListener(new RowListener());
	    tmpLifeTimeTable.getColumnModel().getSelectionModel().addListSelectionListener(new ColumnListener());
	    
	    
	    
	    JScrollPane detailedScrollPane=new JScrollPane();
	    detailedScrollPane.setViewportView(tmpLifeTimeTable);
	    detailedScrollPane.setAlignmentX(0);
	    detailedScrollPane.setAlignmentY(0);
	    detailedScrollPane.setBounds(0,0,1280,650);
	    detailedScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
	    detailedScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
       
	   // detailedScrollPane.setCursor(getCursor());
	    
	    JDialog detailedDialog=new JDialog();
	    detailedDialog.setBounds(100, 100, 1300, 700);
	    
	    JPanel panelToAdd=new JPanel();
	    
	    GroupLayout gl_panel = new GroupLayout(panelToAdd);
	    gl_panel.setHorizontalGroup(
	    		gl_panel.createParallelGroup(Alignment.LEADING)
		);
	    gl_panel.setVerticalGroup(
	    		gl_panel.createParallelGroup(Alignment.LEADING)
		);
		panelToAdd.setLayout(gl_panel);
	    
	    panelToAdd.add(detailedScrollPane);
	    detailedDialog.getContentPane().add(panelToAdd);
	    detailedDialog.setVisible(true);
		
		
	}

	public void assignLifeTimeTable(TableColumn tableColumn, int value){
		tableColumn.setPreferredWidth(value);
		tableColumn.setMaxWidth(value);
		tableColumn.setMinWidth(value);
	}
	
	public class RowListener implements ListSelectionListener {
        public void valueChanged(ListSelectionEvent event) {
            if (event.getValueIsAdjusting()) {
                return;
            }
            
            int selectedRow = tablesConfiguration.getLifeTimeTable().getSelectedRow();
            configuration.getSelectedRows().add(selectedRow);
           
     
        }
    }
	public class ColumnListener implements ListSelectionListener {
        public void valueChanged(ListSelectionEvent event) {
            if (event.getValueIsAdjusting()) {
                return;
            }
           
        }
    }
}
