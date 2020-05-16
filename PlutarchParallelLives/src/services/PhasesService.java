package services;

import java.util.TreeMap;

import data.dataKeeper.GlobalDataKeeper;
import data.dataKeeper.PPLFile;
import data.dataPPL.pplTransition.PPLTransition;
import gui.configurations.Configuration;
import phaseAnalyzer.engine.PhaseAnalyzerMainEngine;

public class PhasesService implements IPhasesService
{
	@Override
	public PhaseAnalyzerMainEngine createPhaseAnalyserEngine(Configuration configuration,
									GlobalDataKeeper globalDataKeeper,
									PPLFile pplFile) 
	{
		
        System.out.println(configuration.getTimeWeight()+" "+ configuration.getChangeWeight());
		PhaseAnalyzerMainEngine mainEngine = new PhaseAnalyzerMainEngine(pplFile.getInputCsv(),pplFile.getOutputAssessment1(),pplFile.getOutputAssessment2(), configuration.getTimeWeight(),configuration.getChangeWeight(), configuration.getPreProcessingTime(), configuration.getPreProcessingChange());

		mainEngine.parseInput();		
		System.out.println("\n\n\n");
		
		return mainEngine;
	
	}

	@Override
	public void connectTransitionsWithPhases(PhaseAnalyzerMainEngine mainEngine,
											TreeMap<Integer,PPLTransition> allPPLTransitions) 
	{
		//test an to mainEngine exei krathsei tis allages kai eksw sto GUI alliws thelei return
		
		int numberOfPhases= 56;
		if(allPPLTransitions.size()<56)
		{
		numberOfPhases=40;
		}
		mainEngine.extractPhases(numberOfPhases);
		mainEngine.connectTransitionsWithPhases(allPPLTransitions);
		
	}
}
