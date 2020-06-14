/**
 * This class represents the sequence of transitions that we receive as input.
 */
package phaseAnalyzer.commons;

import java.util.ArrayList;

public class TransitionHistory {
	private ArrayList<TransitionStats> values;
	private int totalUpdates;
	private double totalTime;
	
	public TransitionHistory() {
		this.values = new ArrayList<TransitionStats>();
	}
	
	public TransitionHistory(ArrayList<TransitionStats> values) {
		this.values = values;
	}
	
	public int getTotalUpdates(){
		return this.totalUpdates;
	}

	public void addValue(TransitionStats transitionStats){
		values.add(transitionStats);
	}
	
	public String consoleVerticalReport(){
		String output = "";
		for (TransitionStats value: values){
			System.out.println(value.toStringShort());
			output+=value.toStringShort();
		}
		System.out.println();
		return output;
	}

	public ArrayList<TransitionStats> getValues() {
		return values;
	}
		
	public void setTotalUpdates(int totalUpdates){
		this.totalUpdates=totalUpdates;
	}

	public void setTotalTime(){
		this.totalTime=(this.values.get(this.values.size()-1).getTime()-this.values.get(0).getTime())/86400;
	}
	
	public double getTotalTime(){
		return this.totalTime;
	}
}
