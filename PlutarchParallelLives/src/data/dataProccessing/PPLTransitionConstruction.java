package data.dataProccessing;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import data.dataPPL.pplSQLSchema.PPLSchema;
import data.dataPPL.pplTransition.AtomicChange;
import data.dataPPL.pplTransition.PPLTransition;
import data.dataPPL.pplTransition.TableChange;

public class PPLTransitionConstruction {
	
	private static TreeMap<Integer,PPLTransition> allPPLTransitions = null;
	private static TreeMap<String,PPLSchema> allPPLSchemas = new TreeMap<String,PPLSchema>();
	private static TreeMap<String,TableChange> allTableChanges = new  TreeMap<String,TableChange>();

	public PPLTransitionConstruction(TreeMap<String,PPLSchema> tempSchema, TreeMap<String,TableChange> tempTables){
		allPPLTransitions = new TreeMap<Integer,PPLTransition>();
		allPPLSchemas=tempSchema;
		allTableChanges = tempTables;
	}
	
	public void makePPLTransitions(){
		
		ArrayList<TableChange> tableChanges = extractPPLTransitions();
		setTableChanges(tableChanges);
	}
	
	private ArrayList<TableChange> extractPPLTransitions(){
		
		allPPLTransitions  = new TreeMap<Integer,PPLTransition>();
		
		ArrayList<TableChange> tableChanges = new ArrayList<TableChange>();
		
		Set<String> keys = allPPLSchemas.keySet();
		ArrayList<String> assistantKeys = new ArrayList<String>();
		
		for(String k: keys){
			assistantKeys.add(k);
		}
		
		for(int i=0; i<assistantKeys.size()-1; i++){
			
			PPLTransition pplTransition = new PPLTransition(assistantKeys.get(i),assistantKeys.get(i+1),i);
			
			allPPLTransitions.put(i,pplTransition);
			
		}
		return tableChanges;
	}
	
	private void setTableChanges(ArrayList<TableChange> tableChanges){
		for (Map.Entry<Integer,PPLTransition> transition : allPPLTransitions.entrySet()) {
			for (Map.Entry<String, TableChange> tableChange : allTableChanges.entrySet()) {
				TableChange tmpTableChange = tableChange.getValue();
				TreeMap<Integer,ArrayList<AtomicChange>> atomicChanges = tmpTableChange.getTableAtomicChanges();
				for (Map.Entry<Integer,ArrayList<AtomicChange>> atomicChange : atomicChanges.entrySet()) {
					if(atomicChange.getKey().equals(transition.getKey())){
						TableChange tempTableChange = new TableChange(tableChange.getKey(),tmpTableChange.getTableAtomicChangeForOneTransition(transition.getKey()));
						tableChanges.add(tempTableChange);
					}
				}
			}
			transition.getValue().setTableChanges(tableChanges);
			tableChanges = new ArrayList<TableChange>();
		}
	}
	
	
	public  TreeMap<Integer,PPLTransition> getAllPPLTransitions(){
		
		return allPPLTransitions;
	}
}
