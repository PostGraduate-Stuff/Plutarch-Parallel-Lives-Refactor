package phaseAnalyzer.engine;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;

import phaseAnalyzer.analysis.BottomUpPhaseExtractor;
import phaseAnalyzer.analysis.IPhaseExtractor;
import phaseAnalyzer.commons.PhaseCollector;
import phaseAnalyzer.commons.TransitionHistory;
import phaseAnalyzer.parser.IParser;
import phaseAnalyzer.parser.SimpleTextParser;
import data.dataPPL.pplTransition.PPLTransition;

public class PhaseAnalyzerMainEngine {
	private IParser parser;
	private IPhaseExtractor phaseExtractor;
	private TransitionHistory transitionHistory;
	
	private ArrayList<PhaseCollector> phaseCollectors;
	private HashMap<String,ArrayList<PhaseCollector>> allPhaseCollectors;
	private String inputCsv;
	private float timeWeight;
	private float changeWeight;
	private boolean preProcessingTime;
	private boolean preProcessingChange;
	
	public PhaseAnalyzerMainEngine(String inputCsv,String outputAssessment1,String outputAssessment2,Float tmpTimeWeight, Float tmpChangeWeight,
														Boolean tmpPreProcessingTime,Boolean tmpPreProcessingChange){
		
		this.timeWeight=tmpTimeWeight;
		this.changeWeight=tmpChangeWeight;
		this.preProcessingTime=tmpPreProcessingTime;
		this.preProcessingChange=tmpPreProcessingChange;
		this.inputCsv=inputCsv;
		this.parser = new SimpleTextParser();
		this.phaseExtractor = new BottomUpPhaseExtractor();
		this.transitionHistory = new TransitionHistory();
		this.allPhaseCollectors = new HashMap<String, ArrayList<PhaseCollector>>();
		
	}

	public ArrayList<PhaseCollector> getPhaseCollectors(){
		return phaseCollectors;
	}
	
	public void extractPhases(int numPhases){
		phaseCollectors = new ArrayList<PhaseCollector>();
		PhaseCollector phaseCollector = new PhaseCollector();
		phaseCollector = phaseExtractor.extractAtMostKPhases(transitionHistory, numPhases, timeWeight, changeWeight, preProcessingTime, preProcessingChange);
		phaseCollectors.add(phaseCollector);
		allPhaseCollectors.put(inputCsv, phaseCollectors);
	}

	public void connectTransitionsWithPhases(TreeMap<Integer,PPLTransition> allPPLTransitions){
		phaseCollectors.get(0).connectPhasesWithTransitions(allPPLTransitions);
	}
	
	public String parseInput(){

		this.transitionHistory = parser.parse(inputCsv, ";"); 
		return this.transitionHistory.consoleVerticalReport();
	}
	

	
}
