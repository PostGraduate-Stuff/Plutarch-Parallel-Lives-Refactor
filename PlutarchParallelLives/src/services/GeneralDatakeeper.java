package services;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;

import data.dataKeeper.GlobalDataKeeper;
import gui.configurations.Configuration;
import gui.tableElements.tableConstructors.TableConstructionIDU;
import gui.tableElements.tableConstructors.TableConstructionWithClusters;
import gui.treeElements.TreeConstructionGeneral;
import phaseAnalyzer.engine.PhaseAnalyzerMainEngine;
import tableClustering.clusterExtractor.engine.TableClusteringMainEngine;

public class GeneralDatakeeper {

	private PPLFile pplFile;
	private TableConstructionIDU contructedTable;
	private GlobalDataKeeper globalDataKeeper;
	
	
	
	public void setGlobalData(Configuration configuration){
		try{
			globalDataKeeper=new GlobalDataKeeper(pplFile.getDatasetTxt(), pplFile.getTransitionsFile());
			globalDataKeeper.setData();
			System.out.println(globalDataKeeper.getAllPPLTables().size());
			
	        System.out.println(pplFile.getFile().toString());

		}
		catch(Exception ex){
			
		}
	}
	
	public PPLFile getPPLFile()
	{
		return pplFile;
	}
	
	public TableConstructionIDU getTableConstructionIDU(){
		return contructedTable;
	}

	public PPLFile getPplFile() {
		return pplFile;
	}

	public void setPplFile(PPLFile pplFile) {
		this.pplFile = pplFile;
	}

	public GlobalDataKeeper getGlobalDataKeeper() {
		return globalDataKeeper;
	}

	public void setGlobalDataKeeper(GlobalDataKeeper globalDataKeeper) {
		this.globalDataKeeper = globalDataKeeper;
	}
	
	public void setTableConstructionIDU(TableConstructionIDU contructedTable)
	{
		this.contructedTable = contructedTable;
	}

}
