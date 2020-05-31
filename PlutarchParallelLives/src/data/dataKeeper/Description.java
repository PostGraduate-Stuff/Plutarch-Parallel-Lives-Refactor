package data.dataKeeper;

public class Description {
	
	private GlobalDataKeeper globalDataKeeper;
	
	public Description(){
		
	}
	
	public Description (GlobalDataKeeper globalDataKeeper)
	{
		this.globalDataKeeper = globalDataKeeper;
	}
	
	public String getTransitionDescription(int columnNumber){
		String description= getTransitionTitle(columnNumber);     
		description=description+"Transition Changes:"+globalDataKeeper.getAllPPLTransitions().get(columnNumber).getNumberOfChangesForOneTr()+"\n";
		description=description+"Additions:"+globalDataKeeper.getAllPPLTransitions().get(columnNumber).getNumberOfAdditionsForOneTr()+"\n";
		description=description+"Deletions:"+globalDataKeeper.getAllPPLTransitions().get(columnNumber).getNumberOfDeletionsForOneTr()+"\n";
		description=description+"Updates:"+globalDataKeeper.getAllPPLTransitions().get(columnNumber).getNumberOfUpdatesForOneTr()+"\n";
		return description;
	}
	
	private String getTransitionTitle(int columnNumber)
	{
		String description="Transition ID:"+columnNumber+"\n";
    	description=description+"Old Version Name:"+globalDataKeeper.getAllPPLTransitions().
				get(columnNumber).getOldVersionName()+"\n";
		description=description+"New Version Name:"+globalDataKeeper.getAllPPLTransitions().
				get(columnNumber).getNewVersionName()+"\n";	
		return description;
	}
	
	public String getBirthDeathDescription(String rowItem)
	{
		String description="Table:"+rowItem+"\n";
		description=description+"Birth Version Name:"+globalDataKeeper.getAllPPLTables().get(rowItem).getBirth()+"\n";
		description=description+"Birth Version ID:"+globalDataKeeper.getAllPPLTables().get(rowItem).getBirthVersionID()+"\n";
		description=description+"Death Version Name:"+globalDataKeeper.getAllPPLTables().get(rowItem).getDeath()+"\n";
		description=description+"Death Version ID:"+globalDataKeeper.getAllPPLTables().get(rowItem).getDeathVersionID()+"\n";
		

		return description;
	}
	
	public String getEmptyDescription(int columnNumber){
		String description=getTransitionTitle(columnNumber);
		description=description+"Transition Changes:0"+"\n";
		description=description+"Additions:0"+"\n";
		description=description+"Deletions:0"+"\n";
		description=description+"Updates:0"+"\n";
		return description;
	}
	
	public String getPhasesDescription(int column){
      	String description="First Transition ID:"+globalDataKeeper.getPhaseCollectors().get(0).getPhases().
				get(column-1).getStartPos()+"\n";
		description=description+"Last Transition ID:"+globalDataKeeper.getPhaseCollectors().get(0).getPhases().
				get(column-1).getEndPos()+"\n";
		description=description+"Total Changes For This Phase:"+globalDataKeeper.getPhaseCollectors().get(0).getPhases().
				get(column-1).getTotalUpdates()+"\n";
		description=description+"Additions For This Phase:"+globalDataKeeper.getPhaseCollectors().get(0).getPhases().
				get(column-1).getTotalAdditionsOfPhase()+"\n";
		description=description+"Deletions For This Phase:"+globalDataKeeper.getPhaseCollectors().get(0).getPhases().
				get(column-1).getTotalDeletionsOfPhase()+"\n";
		description=description+"Updates For This Phase:"+globalDataKeeper.getPhaseCollectors().get(0).getPhases().
				get(column-1).getTotalUpdatesOfPhase()+"\n";
		return description;
	}
	
	public String getClusterDescription(int row){
		String description="Birth Version Name:"+globalDataKeeper.getClusterCollectors().get(0).getClusters().get(row).getBirthSqlFile()+"\n";
		description=description+"Birth Version ID:"+globalDataKeeper.getClusterCollectors().get(0).getClusters().get(row).getBirth()+"\n";
		description=description+"Death Version Name:"+globalDataKeeper.getClusterCollectors().get(0).getClusters().get(row).getDeathSqlFile()+"\n";
		description=description+"Death Version ID:"+globalDataKeeper.getClusterCollectors().get(0).getClusters().get(row).getDeath()+"\n";
		description=description+"Tables:"+globalDataKeeper.getClusterCollectors().get(0).getClusters().get(row).getNamesOfTables().size()+"\n";
		description=description+"Total Changes:"+globalDataKeeper.getClusterCollectors().get(0).getClusters().get(row).getTotalChanges()+"\n";
		return description;
	}
	
	public String getTransitionsOfPhasesTitles(int column){
		String description = "First Transition ID:"+globalDataKeeper.getPhaseCollectors().get(0).getPhases().
				get(column-1).getStartPos()+"\n";
		description=description+"Last Transition ID:"+globalDataKeeper.getPhaseCollectors().get(0).getPhases().
				get(column-1).getEndPos()+"\n\n";
		return description;
	}
	
	public String getOldNewVersionDescription(int column){
		String description = "Old Version:"+globalDataKeeper.getAllPPLTransitions().get(column).getOldVersionName()+"\n";
		description=description+"New Version:"+globalDataKeeper.getAllPPLTransitions().get(column).getNewVersionName()+"\n\n";
		return description;
	}

}
