package phaseAnalyzer.commons;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import data.dataPPL.pplTransition.PPLTransition;

public class Phase {
	private int startPos;
	private int endPos;
	private String startSQLFile;
	private String endSQLFile;
	private ArrayList<Phase> subPhases = new ArrayList<Phase>();
	private int totalUpdates;
	private TransitionHistory transitionHistory;
	private double sum=0;
	private TreeMap<Integer,PPLTransition> phasePPLTransitions = new TreeMap<Integer,PPLTransition>();
	
	public Phase(TransitionHistory transitionHistory) {
		this.transitionHistory = transitionHistory;
		subPhases=new ArrayList<Phase>();
	}
	
	public void setStartPos(int startPos) {
		this.startPos = startPos;
		startSQLFile = this.transitionHistory.getValues().get(startPos).getOldVersionFile();
	}
	
	public void setEndPos(int endPos) {
		this.endPos = endPos;
		endSQLFile = this.transitionHistory.getValues().get(endPos).getNewVersionFile();
	}
	
	public void setTransitionHistory(TransitionHistory transitionHistory) {
		this.transitionHistory = transitionHistory;
	}

	public void setTotalUpdates(int totalUpdates) {
		this.totalUpdates = totalUpdates;
	}
	
	public int getTotalAdditionsOfPhase(){
		int additions=0;
		for(Map.Entry<Integer, PPLTransition> pplTransition:phasePPLTransitions.entrySet()){
			additions=additions+pplTransition.getValue().getNumberOfActionsForOneTr("Addition");
		}
		return additions;
	}
	
	public int getTotalDeletionsOfPhase(){
		int deletions=0;
		for(Map.Entry<Integer, PPLTransition> pplTransition:phasePPLTransitions.entrySet()){
			deletions=deletions+pplTransition.getValue().getNumberOfActionsForOneTr("Deletion");
		}
		return deletions;
	}
	
	public int getTotalUpdatesOfPhase(){
		int updates=0;
		for(Map.Entry<Integer, PPLTransition> pplTransition:phasePPLTransitions.entrySet()){
			updates=updates+pplTransition.getValue().getNumberOfActionsForOneTr("Change");
		}
		return updates;
	}
	
	public double getSum(){
		return sum;
	}
	
	public TreeMap<Integer,PPLTransition> getPhasePPLTransitions(){
		return phasePPLTransitions;
	}
	
	public int getStartPos() {
		return startPos;
	}
	
	public int getEndPos() {
		return endPos;
	}
	
	public TransitionHistory getTransitionHistory() {
		return transitionHistory;
	}
	
	public int getTotalUpdates() {
		return totalUpdates;
	}
	
	public String toStringShort(){
		String helper = new String();
		DecimalFormat decimalFormat = new DecimalFormat("0.00");
		float average=(float)totalUpdates/(endPos-startPos+1);
		helper = startPos + "\t" + endPos + "\t" + totalUpdates+ "\t" + decimalFormat.format(average) + "\t" ;
		ArrayList<TransitionStats> transitionStats = transitionHistory.getValues();
		for(int i=startPos; i<=endPos; i++){
			TransitionStats currentTransition=transitionStats.get(i);
			int currentUpdates=currentTransition.getNumberOfAttributeDeletions() + currentTransition.getNumberOfAttributeDeletionsWithDeletedTables()+ currentTransition.getNumberOfAttributesInKeyAlt() +
					currentTransition.getNumberOfAttributeInsertions() + currentTransition.getNumberOfAttributeInsertionsInNewTables() + currentTransition.getNumberOfAttributesWithTypeAlt();
			float subtraction=Math.abs(currentUpdates-average);
			double power=Math.pow(subtraction, 1);
			sum=sum+power;
			helper=helper+currentUpdates+ "\t" + decimalFormat.format(subtraction)+ "\t" + decimalFormat.format(power);
			if(i==endPos){
				helper=helper+"\t"+decimalFormat.format(sum);
			}
			helper=helper+"\n"+"\t"+"\t"+"\t"+"\t";
		}
		return helper;
	}

	public double calculateDistance(Phase anotherPhase,float timeWeight, float changeWeight){
		int transitionHistoryTotalUpdates = transitionHistory.getTotalUpdates();
		double changeDistance = Math.abs(this.totalUpdates - anotherPhase.totalUpdates)/((double)transitionHistoryTotalUpdates);
		double timeDistance = 0;
		Phase subsequent, preceding; 
		if(this.startPos > anotherPhase.endPos){
			subsequent = this;
			preceding = anotherPhase;
		}
		else{
			preceding = this;
			subsequent = anotherPhase;
		}
		timeDistance = (((transitionHistory.getValues().get(subsequent.startPos).getTime() - transitionHistory.getValues().get(preceding.endPos).getTime())/86400))/(transitionHistory.getTotalTime());
		double totalDistance = changeWeight * changeDistance + timeWeight * timeDistance;
		return totalDistance;
	}

	public Phase mergeWithNextPhase(Phase nextPhase){
		Phase newPhase = new Phase(transitionHistory);
		newPhase.startPos = this.startPos;
		newPhase.endPos = nextPhase.endPos;
		newPhase.startSQLFile = this.startSQLFile;
		newPhase.endSQLFile = nextPhase.endSQLFile;
		newPhase.totalUpdates = this.totalUpdates + nextPhase.totalUpdates;
		newPhase.subPhases.add(this);
		newPhase.subPhases.add(nextPhase);
		for(int i=0; i<this.subPhases.size(); i++){	
			newPhase.subPhases.add(this.subPhases.get(i));
		}
		for(int i=0; i<nextPhase.subPhases.size(); i++){	
			newPhase.subPhases.add(nextPhase.subPhases.get(i));
		}
		return newPhase;
	}
	
	public void connectWithPPLTransitions(TreeMap<Integer,PPLTransition> allPPLTransitions){
		boolean found = false;
		for (Map.Entry<Integer,PPLTransition> transition : allPPLTransitions.entrySet()) {
			if(transition.getValue().getOldVersionName().equals(startSQLFile)){
				found=true;
			}
			if(found) {
				this.phasePPLTransitions.put(transition.getKey(), transition.getValue());
			}
			if(transition.getValue().getNewVersionName().equals(endSQLFile)){
				break;				
			}
		}
		System.out.println(startPos+" "+startSQLFile+" "+endPos+" "+endSQLFile);
	}
	
	public int getSize(){
		return phasePPLTransitions.size();
	}
}
