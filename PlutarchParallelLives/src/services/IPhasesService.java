package services;

import java.util.TreeMap;

import data.dataKeeper.GlobalDataKeeper;
import data.dataKeeper.PPLFile;
import data.dataPPL.pplTransition.PPLTransition;
import gui.configurations.Configuration;
import phaseAnalyzer.engine.PhaseAnalyzerMainEngine;

public interface IPhasesService 
{
	public PhaseAnalyzerMainEngine createPhaseAnalyserEngine(Configuration configuration, GlobalDataKeeper globalDataKeeper,PPLFile pplFile);
	
	public void connectTransitionsWithPhases(PhaseAnalyzerMainEngine mainEngine, TreeMap<Integer,PPLTransition> allPPLTransitions);
}
