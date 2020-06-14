package data.dataPPL.pplTransition;

import java.util.ArrayList;

public class PPLTransition {
private String oldSchema;
	private String newSchema;
	private int pplTransitionID;
	private ArrayList<TableChange> tableChanges = new ArrayList<TableChange>();
	
	public PPLTransition(String tmpOldSchema, String tmpNewSchema,int pplTransitionID){
		oldSchema = tmpOldSchema;
		newSchema = tmpNewSchema;
		this.pplTransitionID=pplTransitionID;	
	}

	public int getNumberOfActionsForOneTr(String type){
		int actions = 0;
		
		for(int i=0; i<tableChanges.size(); i++){
			actions = actions + tableChanges.get(i).getNumberOfActionsForOneTrantition(type);
		}
		return actions;
	}
	
	public int getNumberOfChangesForOneTr(){
		int totalChanges=0;
		
		for(int i=0; i<tableChanges.size(); i++){
			totalChanges=totalChanges+tableChanges.get(i).getTableAtomicChangeForOneTransition().size();
		}
		return totalChanges;
	}
	
	public void setTableChanges(ArrayList<TableChange> tmpTableChanges){
		tableChanges = tmpTableChanges;	
	}
	
	public ArrayList<TableChange> getTableChanges(){	
		return tableChanges;
	}
	
	public int getPPLTransitionID() {
		return pplTransitionID;
	}
	
	public String getNewVersionName(){
		return newSchema;
	}
	
	public String getOldVersionName(){
		return oldSchema;
	}
}
