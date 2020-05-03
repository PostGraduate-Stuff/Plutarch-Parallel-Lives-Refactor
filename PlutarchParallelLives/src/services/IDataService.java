package services;

import java.io.File;

import data.dataKeeper.GlobalDataKeeper;
import gui.configurations.Configuration;

public interface IDataService {

	public PPLFile readFile(File file);
	
	public GlobalDataKeeper setGlobalData(String datasetTxt, String transitionsFile);
}
