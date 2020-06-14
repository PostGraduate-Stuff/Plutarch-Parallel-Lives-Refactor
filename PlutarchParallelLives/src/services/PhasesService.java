package services;

import java.util.TreeMap;

import data.dataKeeper.GlobalDataKeeper;
import data.dataKeeper.PPLFile;
import data.dataPPL.pplTransition.PPLTransition;
import gui.configurations.DataConfiguration;
import phaseAnalyzer.engine.PhaseAnalyzerMainEngine;

public class PhasesService implements IPhasesService
{
	@Override
	public PhaseAnalyzerMainEngine createPhaseAnalyserEngine(DataConfiguration dataConfiguration,
									GlobalDataKeeper globalDataKeeper,
									PPLFile pplFile) 
	{
		
        System.out.println(dataConfiguration.getTimeWeight()+" "+ dataConfiguration.getChangeWeight());
		PhaseAnalyzerMainEngine mainEngine = new PhaseAnalyzerMainEngine(pplFile.getInputCsv(),pplFile.getOutputAssessment1(),pplFile.getOutputAssessment2(), dataConfiguration.getTimeWeight(),dataConfiguration.getChangeWeight(), dataConfiguration.getPreProcessingTime(), dataConfiguration.getPreProcessingChange());

		mainEngine.parseInput();		
		System.out.println("\n\n\n");
		
		return mainEngine;
	
	}

	@Override
	public void connectTransitionsWithPhases(PhaseAnalyzerMainEngine mainEngine,
											TreeMap<Integer,PPLTransition> allPPLTransitions) 
	{
		
		int numberOfPhases= 56;
		if(allPPLTransitions.size()<56)
		{
		numberOfPhases=40;
		}
		mainEngine.extractPhases(numberOfPhases);
		mainEngine.connectTransitionsWithPhases(allPPLTransitions);
		
	}
}
