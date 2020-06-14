package gui.tableElements.commons;

import javax.swing.table.AbstractTableModel;

public class MyTableModel extends AbstractTableModel {
	
	private static final long serialVersionUID = 1L;
	
	private boolean DEBUG=false;
	private String[] columns=null;
	private String[][] rows=null;

	
	public MyTableModel(String[] tmpColumns, String [][] tmpRows){
		
		this.columns = tmpColumns;
		this.rows = tmpRows;
		
	}
	public MyTableModel(){}
	
	
	public void initializeZoomModel(String[] finalColumnsZoomArea,  String[][] finalRowsZoomArea) 
	{
		this.columns = finalColumnsZoomArea;
		int numberOfColumns = finalRowsZoomArea[0].length;
		int numberOfRows = finalRowsZoomArea.length;
		
		this.rows=new String[numberOfRows][numberOfColumns];
		
		for(int i=0; i<numberOfRows; i++){
			this.rows[i][0]=finalRowsZoomArea[i][0];
		}
	
	}


	
	public void setData(String[] tmpColumns, String [][] tmpRows){
		columns=tmpColumns;
		rows=tmpRows;
	}
	
	public int getColumnCount() {
        return columns.length;
    }

    public int getRowCount() {
        return rows.length;
    }

    public String getColumnName(int col) {
        return columns[col];
    }

    public String getRowName(int row) {
        return rows[row][0];
    }
    
    public Object getValueAt(int row, int col) {
        return rows[row][col];
    }
    
  
    public Class<? extends Object> getColumnClass(int c) {
    	
    	Object object=getValueAt(0, c);
    	
    	if(object==null){
    		return Object.class;
    	}
        return getValueAt(0, c).getClass();
    }

    public boolean isCellEditable(int row, int col) {
        return false;
    }

    public void setValueAt(String value, int row, int col) {
        if (DEBUG) {
            System.out.println("Setting value at " + row + "," + col
                               + " to " + value
                               + " (an instance of "
                               + value.getClass() + ")");
        }

        rows[row][col] = value;
        fireTableCellUpdated(row, col);

        if (DEBUG) {
            System.out.println("New value of data:");
            printDebugData();
        }
    }

    public String printDebugData() {
        int numRows = getRowCount();
        int numCols = getColumnCount();
        String OldDetailedModel = "";
        
        for (int i=0; i < numRows; i++) {
            System.out.print("	row " + i + ":");
            OldDetailedModel+= "    row " + i + ":";
            for (int j=0; j < numCols; j++) {
                System.out.print("  " + rows[i][j]);
                OldDetailedModel+= "  " + rows[i][j];
            }
            System.out.println();
            
        }
        System.out.println("--------------------------");
        OldDetailedModel+= "--------------------------";
        return OldDetailedModel;
    }
}
