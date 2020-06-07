package gui.tableElements.tableConstructors;

public abstract class TableConstruction implements PldConstruction
{
	protected String[] constructedColumns;
	protected String[][] constructedRows;

	public abstract void constructColumns();

	public abstract void constructRows();

	public abstract Integer[] getSegmentSize();

	@Override
	public String[] getConstructedColumns()
	{
		return constructedColumns;
	}
	
	@Override
	public String[][] getConstructedRows()
	{
		return constructedRows;
	}

}
