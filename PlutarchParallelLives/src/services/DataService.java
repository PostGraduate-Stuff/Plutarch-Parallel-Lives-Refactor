package services;

import java.io.File;
import java.io.IOException;
import java.util.TreeMap;

import data.dataKeeper.GlobalDataKeeper;
import data.dataKeeper.PPLFile;
import data.dataPPL.pplSQLSchema.PPLTable;
import data.dataPPL.pplTransition.PPLTransition;
import data.dataSorters.PldRowSorter;
import gui.configurations.Configuration;
import phaseAnalyzer.engine.PhaseAnalyzerMainEngine;

public class DataService implements IDataService 
{

	@Override
	public PPLFile readFile(File file) 
	{
		PPLFile pplFile = new PPLFile();
		try {
			pplFile.readFile(file);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return pplFile;

	}
	
	
	@Override
	public GlobalDataKeeper setGlobalData(String datasetTxt, String transitionsFile) {
		GlobalDataKeeper globalDataKeeper = new GlobalDataKeeper();
		try{
			globalDataKeeper=new GlobalDataKeeper(datasetTxt, transitionsFile);
			globalDataKeeper.setData();
			System.out.println(globalDataKeeper.getAllPPLTables().size());
			//System.out.println(pplFile.getFile().toString());

	      
		}
		catch(Exception ex){
			
		}	
		return globalDataKeeper;
	}
	
	public String[][] sortRows(String[][] finalRows, TreeMap<String,PPLTable> pplTables){
		PldRowSorter sorter=new PldRowSorter();
		return sorter.sortRows(finalRows, pplTables);
	}
	

	
	
	


}
