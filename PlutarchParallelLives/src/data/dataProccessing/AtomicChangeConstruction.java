package data.dataProccessing;

import gr.uoi.cs.daintiness.hecate.sql.Attribute;
import gr.uoi.cs.daintiness.hecate.transitions.Deletion;
import gr.uoi.cs.daintiness.hecate.transitions.Insersion;
import gr.uoi.cs.daintiness.hecate.transitions.Transition;
import gr.uoi.cs.daintiness.hecate.transitions.TransitionList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import data.dataPPL.pplTransition.AtomicChange;

public class AtomicChangeConstruction {
	
	private static ArrayList<AtomicChange> atomicChanges = null;
	private static ArrayList<TransitionList> allTransitions = new ArrayList<TransitionList>();

	public AtomicChangeConstruction(ArrayList<TransitionList> alltempTransitions){
		atomicChanges = new ArrayList<AtomicChange>();
		allTransitions=alltempTransitions;
	}
	
	public void makeAtomicChanges(){
		for(int i=0; i<allTransitions.size(); i++){
			TransitionList currentTransitionList=allTransitions.get(i);
			String oldVersion = currentTransitionList.getOldVersion();
			String newVersion = currentTransitionList.getNewVersion();
			ArrayList<Transition> currentTransitions = currentTransitionList.getList();
			extractAtomicChanges(oldVersion, newVersion, currentTransitions, i);
		}
	}
	
	private void extractAtomicChanges(String oldVersion, String newVersion, ArrayList<Transition> currentTransitions, int transitionIterator){
		for(int transition=0; transition<currentTransitions.size(); transition++){
			Collection<Attribute> tempAffectedAttributes = currentTransitions.get(transition).getAffAttributes();
			Iterator<Attribute> iterator = tempAffectedAttributes.iterator();
			while(iterator.hasNext()){					
				Attribute tmpHecAttribute = (Attribute) iterator.next();
				String tempType = figureActionType(currentTransitions, transition);
				AtomicChange tmpAtomicChange= new AtomicChange(currentTransitions.get(transition).getAffTable().getName(),tmpHecAttribute.getName(),tempType,oldVersion,newVersion,transitionIterator);
				atomicChanges.add(tmpAtomicChange);
			}
		}
	}
	
	private String figureActionType(ArrayList<Transition> currentTransitions, int transition){
		if(currentTransitions.get(transition) instanceof Insersion){
			if(currentTransitions.get(transition).getType().equals("UpdateTable")){
				return "Addition";
			}
			return "Addition of New Table";
		}
		if(currentTransitions.get(transition) instanceof Deletion){
			if(currentTransitions.get(transition).getType().equals("UpdateTable")){
				return "Deletion";
			}
			return "Deletion of whole Table";
		}
		
		if(currentTransitions.get(transition).getType().equals("TypeChange")){
			return "Type Change";
		}
		return "Key Change";
	}
	
	public ArrayList<AtomicChange> getAtomicChanges(){
		return atomicChanges;
	}

}
