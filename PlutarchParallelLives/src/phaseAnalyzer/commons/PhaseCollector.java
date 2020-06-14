
package phaseAnalyzer.commons;

import java.util.ArrayList;
import java.util.TreeMap;

import data.dataPPL.pplTransition.PPLTransition;



public class PhaseCollector {
	private double totalSum=0;
	private ArrayList<Phase> phases;
	
	public PhaseCollector(){
		phases = new ArrayList<Phase>();
	}

	public PhaseCollector(ArrayList<Phase> phases) {
		this.phases = phases;
	}
	
	public ArrayList<Phase> getPhases() {
		return phases;
	}

	public void setPhases(ArrayList<Phase> phases) {
		this.phases = phases;
	}

	public void addPhase(Phase p){
		this.phases.add(p);
	}
	
	public double getTotalSum(){
		return totalSum;
	}
	
	public int getSize(){
		return this.phases.size();
	}
	
	
	public String toStringShort() {
		String helperString=new String("");

		for(Phase phase: phases){
			helperString = helperString + phase.toStringShort()+ "\n";
			totalSum=totalSum+phase.getSum();
		}
		helperString= helperString + "\t"+ "\t"+ "\t"+totalSum;
		helperString = helperString + "\n";
		return helperString;
	}

	public String toStringShortAssessment2() {
		String helperString=new String("");
		for(int i=0; i<phases.size()-1; i++){
			
			helperString=helperString+i+"@"+(i+1)+"\t";
			float timeD = Math.abs((float)(phases.get(i).getTransitionHistory().getValues().get(phases.get(i).getEndPos()).getTime()-
					phases.get(i+1).getTransitionHistory().getValues().get(phases.get(i+1).getStartPos()).getTime())/84600);
			helperString=helperString+timeD+"\t";
			float changeD = Math.abs((float)(phases.get(i).getTransitionHistory().getValues().get(phases.get(i).getEndPos()).getTotalUpdatesInTransition()-
					phases.get(i+1).getTransitionHistory().getValues().get(phases.get(i+1).getStartPos()).getTotalUpdatesInTransition()));
			float avgD = (Math.abs((float)((phases.get(i).getTotalUpdates()/(phases.get(i).getEndPos()-phases.get(i).getStartPos()+1))-
					(phases.get(i+1).getTotalUpdates()/(phases.get(i+1).getEndPos()-phases.get(i+1).getStartPos()+1)))))/phases.get(i).getTransitionHistory().getTotalUpdates();
			helperString=helperString+changeD+"\t"+avgD+"\n";
		}
		helperString = helperString + "\n";
		return helperString;
	}

	public void connectPhasesWithTransitions(TreeMap<Integer,PPLTransition> allPPLTransitions){
		for(Phase phase: phases){
			phase.connectWithPPLTransitions(allPPLTransitions);
		}
	}
	
	

}
