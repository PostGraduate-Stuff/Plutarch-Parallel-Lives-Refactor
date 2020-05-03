package gui.tableElements.tableConstructors;

public interface PldConstruction {

	public void constructColumns();
	public void constructRows();
	public Integer[] getSegmentSize();
	
	public String[] getConstructedColumns();
	
	public String[][] getConstructedRows();
	
}
