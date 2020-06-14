package data.dataProccessing;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import data.dataPPL.pplSQLSchema.PPLTable;
import data.dataPPL.pplTransition.AtomicChange;
import data.dataPPL.pplTransition.TableChange;

public class TableChangeConstruction {
	
	private static ArrayList<AtomicChange> atomicChanges = new ArrayList<AtomicChange>();
	private static TreeMap<String,TableChange> allTableChanges;
	private static TreeMap<String,PPLTable> allTables = new TreeMap<String,PPLTable>();

	public TableChangeConstruction(ArrayList<AtomicChange> tempAtomicChanges,TreeMap<String,PPLTable> tempAllTables){
		atomicChanges=tempAtomicChanges;
		allTables=tempAllTables;
		allTableChanges = new  TreeMap<String,TableChange>();
	}
	
	public void makeTableChanges(){
		figureAllTableChanges();
		setTableChanges();
	}
	
	private void figureAllTableChanges(){
		for(int i=0; i<atomicChanges.size(); i++){
			Integer transitionID=atomicChanges.get(i).getTransitionID();
			if(allTableChanges.containsKey(atomicChanges.get(i).getAffectedTableName())){
				setChange(atomicChanges.get(i), transitionID);
				continue;
			}
			setChangeForUnAffectedTable(atomicChanges.get(i), transitionID);
		}
	}
	
	private void setChange(AtomicChange atomicChange, Integer transitionID){
		String tableName = atomicChange.getAffectedTableName();
		if(!allTableChanges.get(tableName).getTableAtomicChanges().containsKey(transitionID)){
			ArrayList<AtomicChange> tmpAtomicChanges = new ArrayList<AtomicChange>();
			allTableChanges.get(tableName).getTableAtomicChanges().put(transitionID, tmpAtomicChanges);
		}
		allTableChanges.get(tableName).getTableAtomicChanges().get(transitionID).add(atomicChange);
	}
	
	private void setChangeForUnAffectedTable(AtomicChange atomicChange,Integer transitionID){
		TreeMap<Integer,ArrayList<AtomicChange>> tmpAtomicChanges = new TreeMap<Integer,ArrayList<AtomicChange>>();
		tmpAtomicChanges.put(transitionID,new ArrayList<AtomicChange>());
		tmpAtomicChanges.get(transitionID).add(atomicChange);
		TableChange tmpTableChange= new TableChange(atomicChange.getAffectedTableName(), tmpAtomicChanges);
		allTableChanges.put(atomicChange.getAffectedTableName(), tmpTableChange);
	}
	
	private void setTableChanges(){
		for (Map.Entry<String, TableChange> tableChange : allTableChanges.entrySet()) {
			allTables.get(tableChange.getKey()).setTableChanges(tableChange.getValue());
			allTables.get(tableChange.getKey()).setTotalChanges();
		}
	}
	
	public TreeMap<String,TableChange> getTableChanges(){
		return allTableChanges;
	}

}
