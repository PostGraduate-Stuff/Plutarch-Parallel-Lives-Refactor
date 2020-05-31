package gui.configurations;

import gui.tableElements.commons.JvTable;
import gui.tableElements.commons.MyTableModel;

public class DataTablesConfiguration 
{
	//data-tables
	private MyTableModel detailedModel = null;
	private MyTableModel generalModel = null;
	private MyTableModel zoomModel = null;
	
	private JvTable LifeTimeTable=null;
	private JvTable zoomAreaTable=null;
	//data-tables
	
	public MyTableModel getDetailedModel() {
		return detailedModel;
	}

	public void setDetailedModel(MyTableModel detailedModel) {
		this.detailedModel = detailedModel;
	}

	public MyTableModel getGeneralModel() {
		return generalModel;
	}

	public void setGeneralModel(MyTableModel generalModel) {
		this.generalModel = generalModel;
	}

	public MyTableModel getZoomModel() {
		return zoomModel;
	}

	public void setZoomModel(MyTableModel zoomModel) {
		this.zoomModel = zoomModel;
	}

	public JvTable getLifeTimeTable() {
		return LifeTimeTable;
	}

	public void setLifeTimeTable(JvTable lifeTimeTable) {
		LifeTimeTable = lifeTimeTable;
	}

	public JvTable getZoomAreaTable() {
		return zoomAreaTable;
	}

	public void setZoomAreaTable(JvTable zoomAreaTable) {
		this.zoomAreaTable = zoomAreaTable;
	}
}
