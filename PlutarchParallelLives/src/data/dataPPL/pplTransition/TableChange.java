package data.dataPPL.pplTransition;

import java.util.ArrayList;
import java.util.TreeMap;

public class TableChange {
	private String affectedTable;
	private TreeMap<Integer,ArrayList<AtomicChange>> atomicChanges = new TreeMap<Integer,ArrayList<AtomicChange>>();
	private ArrayList<AtomicChange> atomicChangesForOneTransition = new ArrayList<AtomicChange>();
	
	public TableChange(String tmpAffectedTable, TreeMap<Integer,ArrayList<AtomicChange>> tmpAtomicChanges){
		this.affectedTable = tmpAffectedTable;
		this.atomicChanges = tmpAtomicChanges;
	}
	
	public TableChange(){
	}
	
	public TableChange(String tmpAffectedTable,ArrayList<AtomicChange> tmpAtomicChanges){
		affectedTable = tmpAffectedTable;
		atomicChangesForOneTransition = tmpAtomicChanges;
	}
	
	public int getNumberOfActionForOneTransition(Integer transition, String type){
		int actions = 0;
		ArrayList<AtomicChange> tmpAtomicChanges = atomicChanges.get(transition);
		
		for(int i=0;i<tmpAtomicChanges.size();i++){
			AtomicChange atomicChange = tmpAtomicChanges.get(i);
			if(atomicChange.getType().contains(type)) {
				actions++;
			}
		}
		return actions;
	}
	
	public int getNumberOfActionsForOneTrantition(String action){
		int actions = 0;
		for(int i=0;i<atomicChangesForOneTransition.size();i++){
			AtomicChange atCh=atomicChangesForOneTransition.get(i);
			if(atCh.getType().contains(action)) {
				actions++;
			}
		}
		return actions;
	}
	
	public String toString(){
		String message = "Table Change \n";
		
		for(int i=0; i < atomicChanges.size(); i++){
			message=message+atomicChanges.get(i).toString();
			message=message+"\n";
		}
		return message;		
	}
	
	public String getAffectedTableName(){
		return affectedTable;	
	}
	
	public TreeMap<Integer,ArrayList<AtomicChange>> getTableAtomicChanges(){
		return atomicChanges;
	}
	
	public ArrayList<AtomicChange> getTableAtomicChangeForOneTransition(Integer transition){
		return atomicChanges.get(transition);
	}
	
	public ArrayList<AtomicChange> getTableAtomicChangeForOneTransition(){
		return atomicChangesForOneTransition;
	}
}
