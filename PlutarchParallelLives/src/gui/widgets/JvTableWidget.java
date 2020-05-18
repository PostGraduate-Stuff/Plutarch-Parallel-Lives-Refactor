package gui.widgets;

import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.JTable;

import gui.configurations.Configuration;
import gui.tableElements.commons.JvTable;
import gui.tableElements.commons.MyTableModel;
import gui.tableElements.tableRenderers.IDUHeaderTableRenderer;
import phaseAnalyzer.commons.Phase;

public class JvTableWidget {
	private Configuration configuration;
	private ArrayList<Phase> initialPhases;
	private JvTable generalTable;
	
	public JvTableWidget(Configuration configuration, ArrayList<Phase> initialPhases) {
		this.configuration = configuration;
		this.initialPhases = initialPhases;
	}

	public JvTable makeGeneralTableIDU() 
	{
		int numberOfColumns=configuration.getFinalRowsZoomArea()[0].length;
		int numberOfRows=configuration.getFinalRowsZoomArea().length;
		
		String[][] rows= createFinalZoomAreaRows(numberOfRows, numberOfColumns);
		
		MyTableModel zoomModel= new MyTableModel();
		zoomModel.initializeZoomModel(configuration.getFinalColumnsZoomArea(), rows);
		
		createJvTable(zoomModel); 
		
		return generalTable;
	}


	private void createJvTable(MyTableModel zoomModel) {
		generalTable =new JvTable(zoomModel);
		
		generalTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		
		if(configuration.getRowHeight()<1){
			configuration.setRowHeight(1);
		}
		if (configuration.getColumnWidth() < 1) {
			configuration.setColumnWidth(1);
		}
		
		for(int i=0; i<generalTable.getRowCount(); i++){
			generalTable.setRowHeight(i, configuration.getRowHeight());
		}

		generalTable.setShowGrid(false);
		generalTable.setIntercellSpacing(new Dimension(0, 0));
		
		for(int i=0; i<generalTable.getColumnCount(); i++){
			generalTable.getColumnModel().getColumn(i).setPreferredWidth(configuration.getColumnWidth());	
		}
		
		setHeaders(generalTable);
	}


	private void setHeaders(JvTable generalTable) {
		int start=-1;
		int end=-1;
		
		if (configuration.getWholeCol()!=-1 && configuration.getWholeCol()!=0) {
			start=initialPhases.get(configuration.getWholeCol()-1).getStartPos();
			end=initialPhases.get(configuration.getWholeCol()-1).getEndPos();
		}
		
		if(configuration.getWholeCol()!=-1){
			for(int i=0; i<generalTable.getColumnCount(); i++){
				if(!(generalTable.getColumnName(i).equals("Table name"))){
					if(Integer.parseInt(generalTable.getColumnName(i))>=start && Integer.parseInt(generalTable.getColumnName(i))<=end){
						generalTable.getColumnModel().getColumn(i).setHeaderRenderer(new IDUHeaderTableRenderer());
					}
				}
			}
		}		
	}

	private String[][] createFinalZoomAreaRows(int numberOfRows, int numberOfColumns) {
		String[][] rows = new String[numberOfRows][numberOfColumns];
				
		for(int i=0; i<numberOfRows; i++){
			rows[i][0]=configuration.getFinalRowsZoomArea()[i][0];
		}
		
		return rows;
	}
}
