/**
 * 
 */
package phaseAnalyzer.analysis;

import java.util.ArrayList;
import java.util.Iterator;

import phaseAnalyzer.commons.Phase;
import phaseAnalyzer.commons.PhaseCollector;
import phaseAnalyzer.commons.TransitionHistory;
import phaseAnalyzer.commons.TransitionStats;

public class BottomUpPhaseExtractor implements IPhaseExtractor {


	@Override
	public PhaseCollector extractAtMostKPhases(	TransitionHistory transitionHistory, int numPhases,
											float timeWeight,float changeWeight,boolean preProcessingTime,boolean preProcessingChange) {
		
		PhaseCollector initSolution = new PhaseCollector();
		this.init(transitionHistory, initSolution);
		
		PhaseCollector preProcessedSolutionTime = new PhaseCollector();
		preProcessedSolutionTime = initSolution;
		if (preProcessingTime){
			preProcessedSolutionTime = performTimePreprocessing(transitionHistory, initSolution);
		}

		PhaseCollector preProcessedSolutionChanges = new PhaseCollector();
		preProcessedSolutionChanges = preProcessedSolutionTime; 
		if (preProcessingChange){	
			preProcessedSolutionChanges = performChangePreprocessing(transitionHistory, preProcessedSolutionTime);
		}
		
		PhaseCollector currentSolution = new PhaseCollector();
		currentSolution = this.newPhaseCollector(transitionHistory, preProcessedSolutionChanges,timeWeight,changeWeight);

		while (currentSolution.getPhases().size() > numPhases){
			currentSolution = this.newPhaseCollector(transitionHistory, currentSolution,timeWeight,changeWeight);
		}
		return currentSolution;
	}


	private PhaseCollector performTimePreprocessing(
				TransitionHistory transitionHistory, PhaseCollector initSolution) {
		
		PhaseCollector preProcessedSolutionTime = new PhaseCollector();
		preProcessedSolutionTime = this.preProcessOverTime(transitionHistory, initSolution);
		int oldSize=initSolution.getPhases().size();

		while(oldSize!=preProcessedSolutionTime.getPhases().size()){

			oldSize=preProcessedSolutionTime.getPhases().size();
			preProcessedSolutionTime = this.preProcessOverTime(transitionHistory, preProcessedSolutionTime);
			
		}
		return preProcessedSolutionTime;
	}


	private PhaseCollector performChangePreprocessing(TransitionHistory transitionHistory,
														PhaseCollector preProcessedSolutionTime) {
		int oldSize;
		PhaseCollector preProcessedSolutionChanges = new PhaseCollector();
		preProcessedSolutionChanges=this.preProcessOverChanges(transitionHistory, preProcessedSolutionTime);
		oldSize=preProcessedSolutionTime.getPhases().size();

		while(oldSize!=preProcessedSolutionChanges.getPhases().size()){

			oldSize=preProcessedSolutionChanges.getPhases().size();
			preProcessedSolutionChanges = this.preProcessOverChanges(transitionHistory, preProcessedSolutionChanges);
			
		}
		return preProcessedSolutionChanges;
	}

	public PhaseCollector newPhaseCollector(TransitionHistory transitionHistory, PhaseCollector prevCollector,float timeWeight,float changeWeight){
		PhaseCollector newCollector = new PhaseCollector();
		ArrayList<Phase> oldPhases = prevCollector.getPhases();
		int oldSize = oldPhases.size();
		if (oldSize == 0){
			
			System.out.println("Sth went terribly worng at method XXX");
			System.exit(-10);
		}

		double distances[] = calculateDistances(oldPhases, timeWeight, changeWeight);

	    int posIterator=-1;
	    double minDist = Double.MAX_VALUE;
	    for(int i=1; i<oldSize; i++){
	    	if(distances[i]<minDist){
	    		posIterator = i;
	    		minDist = distances[i];
	    	}
	    }
		
		ArrayList<Phase> newPhases = calculateNewPhases(oldPhases, posIterator);
		newCollector.setPhases(newPhases);

		return newCollector;
	}
	
	private ArrayList<Phase> calculateNewPhases(ArrayList<Phase> oldPhases, int posIterator) {
	ArrayList<Phase> newPhases = new ArrayList<Phase>();
	Phase toMerge = oldPhases.get(posIterator-1);
	Phase newPhase = toMerge.mergeWithNextPhase(oldPhases.get(posIterator));

		
		for(int i=0; i < posIterator-1; i++){
			Phase phase = oldPhases.get(i);
			newPhases.add(phase);
		}
		newPhases.add(newPhase);
		if(posIterator<oldPhases.size()-1){
			for(int i=posIterator+1; i < oldPhases.size(); i++){
				Phase phase = oldPhases.get(i);
				newPhases.add(phase);
			}		
		}
		return newPhases;
	}


	private double[] calculateDistances(ArrayList<Phase> oldPhases, float timeWeight, float changeWeight) {
		double distances[] = new double[oldPhases.size()];
		distances[0] = Double.MAX_VALUE;
		int positionIterator = 0;
		
	    Iterator<Phase> phaseIter = oldPhases.iterator();
	    Phase previousPhase = phaseIter.next();
	    while (phaseIter.hasNext()){
	      Phase phase = phaseIter.next();
	      positionIterator++;
	      distances[positionIterator] = phase.calculateDistance(previousPhase,timeWeight,changeWeight);
	      
	      
	      previousPhase = phase;
	    }
	    return distances;
	}


	private PhaseCollector preProcessOverTime(TransitionHistory transitionHistory, PhaseCollector prevCollector){
		PhaseCollector preProcessedCollector = new PhaseCollector();
		ArrayList<Phase> oldPhases = prevCollector.getPhases();

		ArrayList<Phase> newPhases = calculateNewPhasesProcessOverTime(oldPhases,transitionHistory);
	    
	    if(newPhases.size()!=0){
	    	preProcessedCollector.setPhases(newPhases);
			return preProcessedCollector;
	    }
    	preProcessedCollector.setPhases(oldPhases);
	    
		return preProcessedCollector;
	}
	
	private ArrayList<Phase> calculateNewPhasesProcessOverTime(ArrayList<Phase> oldPhases, TransitionHistory transitionHistory) {
		ArrayList<Phase> newPhases =new ArrayList<Phase>();
		Iterator<Phase> phaseIter = oldPhases.iterator();
		int position=0;

	    Phase previousPhase = phaseIter.next();
	    while (phaseIter.hasNext()){
	    	position++;
	    	Phase phase = phaseIter.next();
	    	if(((transitionHistory.getValues().get(phase.getStartPos()).getTime()-transitionHistory.getValues().get(previousPhase.getEndPos()).getTime())/84600)<3){
	    		Phase toMerge = previousPhase;
	    		Phase newPhase = toMerge.mergeWithNextPhase(phase);
	    		
	    		for(int i=0; i < position-1; i++){
	    			Phase oldPhase = oldPhases.get(i);
	    			newPhases.add(oldPhase);
	    		}
	    		newPhases.add(newPhase);
	    		if(position<oldPhases.size()-1){

	    			for(int i=position+1; i < oldPhases.size(); i++){

	    				Phase oldPhase = oldPhases.get(i);
	    				newPhases.add(oldPhase);
	    			}		
	    		}
	    		
	    		break;
	    	}
	    	previousPhase = phase;
	    }
	    return newPhases;
	}


	private PhaseCollector preProcessOverChanges(TransitionHistory transitionHistory, PhaseCollector prevCollector){
		
		PhaseCollector preProcessedCollector = new PhaseCollector();
		ArrayList<Phase> oldPhases = prevCollector.getPhases();

		
	    ArrayList<Phase> newPhases = calculateNewPhasesProcessOverChanges(oldPhases,transitionHistory);
	   
	    
	    if(newPhases.size()!=0){
	    	preProcessedCollector.setPhases(newPhases);
			return preProcessedCollector;

	    }
	    preProcessedCollector.setPhases(oldPhases);

		return preProcessedCollector;
	}
	
	private ArrayList<Phase> calculateNewPhasesProcessOverChanges(ArrayList<Phase> oldPhases,TransitionHistory transitionHistory) {
		ArrayList<Phase> newPhases =new ArrayList<>();
	    Iterator<Phase> phaseIter = oldPhases.iterator();
	    Phase previousPhase = phaseIter.next();
		int position=0;

	    while (phaseIter.hasNext()){
	    	position++;
	    	Phase phase = phaseIter.next();
	    	if(((transitionHistory.getValues().get(phase.getStartPos()).getTotalAttributeChange()-transitionHistory.getValues().get(previousPhase.getEndPos()).getTotalAttributeChange()==0))){
	    		Phase toMerge = previousPhase;
	    		Phase newPhase = toMerge.mergeWithNextPhase(phase);
	    		
	    		for(int i=0; i < position-1; i++){
	    			Phase oldPhase = oldPhases.get(i);
	    			newPhases.add(oldPhase);
	    		}
	    		newPhases.add(newPhase);
	    		if(position<oldPhases.size()-1){
	    			for(int i=position+1; i < oldPhases.size(); i++){
	    				Phase oldPhase = oldPhases.get(i);
	    				newPhases.add(oldPhase);
	    			}		
	    		}
	    		
	    		break;
	    	}
	    	previousPhase = phase;
	    }
	    return newPhases;
	}


	public PhaseCollector init(TransitionHistory transitionHistory, PhaseCollector phaseCollector){
		for(TransitionStats v: transitionHistory.getValues()){
			Phase phase = new Phase(transitionHistory);
			int position = transitionHistory.getValues().indexOf(v);
			phase.setStartPos(position);
			phase.setEndPos(position);
			phase.setTotalUpdates(v.getTotalAttributeChange());
			phaseCollector.addPhase(phase);
			
		}
		return phaseCollector;
	}
}
