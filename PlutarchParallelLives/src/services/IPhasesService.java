package services;

import java.util.TreeMap;

import data.dataKeeper.GlobalDataKeeper;
import data.dataKeeper.PPLFile;
import data.dataPPL.pplTransition.PPLTransition;
import gui.configurations.DataConfiguration;
import gui.configurations.GuiConfiguration;
import phaseAnalyzer.engine.PhaseAnalyzerMainEngine;

public interface IPhasesService 
{
	public PhaseAnalyzerMainEngine createPhaseAnalyserEngine(DataConfiguration configuration, GlobalDataKeeper globalDataKeeper,PPLFile pplFile);
	
	public void connectTransitionsWithPhases(PhaseAnalyzerMainEngine mainEngine, TreeMap<Integer,PPLTransition> allPPLTransitions);
}
