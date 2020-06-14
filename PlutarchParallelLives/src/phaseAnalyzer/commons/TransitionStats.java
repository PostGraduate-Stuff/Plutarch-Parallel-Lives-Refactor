/**
 * This class represents the stats of a single transition of the input.
 */
package phaseAnalyzer.commons;

public class TransitionStats {
	private int transitionId;
	private int time;
	private String oldVersionFile;
	private String newVersionFile;
	private int numberOfOldTables;
	private int numberOfNewTables;
	private int numberOfOldAtributes;
	private int numberOfNewAttributes;
	private int numberOfTablesInsertions;
	private int numberOfTablesDeletions;
	private int numberOfAttributeInsertions;
	private int numberOfAttributeDeletions;
	private int numberOfAttributeWithTypeAlt;
	private int numberOfAttributeInKeyAlt;
	private int numberOfAttributeInsertionsInNewTables;
	private int numberOfAttributeDeletionsWithDeletedTables; 
	
	private int totalTableInsertionsDeletions;
	private int totalAttributeInsertionsDeletions;
	private int totalAttributeUpdates;
	private int totalAttributeChange;
	private int timeDistanceFromPrevious;
	private int totalUpdatesInTransition;
	
	public TransitionStats(int transitionId, 
			int time, 
			String oldVersionFile,
			String newVersionFile, 
			int numberOfOldTables, 
			int numberOfNewTables,
			int numberOfOldAttributes, 
			int numberOfNewAttributes, 
			int numberOfTablesInsertions,
			int numberOfTablesDeletions, 
			int numberOfAttributeInsertions, 
			int numberOfAttributeDeletions,
			int numberOfAttributesWithTypeAlt, 
			int numberOfAttributesInKeyAlt,
			int numberOfAttributeInsertionsInNewTables, 
			int numberOfAttributeDeletionsWithDeletedTables,
			int totalUpdatesInTransitions) {
		
		this.transitionId = transitionId;
		this.time = time;
		this.oldVersionFile = oldVersionFile;
		this.newVersionFile = newVersionFile;
		this.numberOfOldTables = numberOfOldTables;
		this.numberOfNewTables = numberOfNewTables;
		this.numberOfOldAtributes = numberOfOldAttributes;
		this.numberOfNewAttributes = numberOfNewAttributes;
		this.numberOfTablesInsertions = numberOfTablesInsertions;
		this.numberOfTablesDeletions = numberOfTablesDeletions;
		this.numberOfAttributeInsertions = numberOfAttributeInsertions;
		this.numberOfAttributeDeletions = numberOfAttributeDeletions;
		this.numberOfAttributeWithTypeAlt = numberOfAttributesWithTypeAlt;
		this.numberOfAttributeInKeyAlt = numberOfAttributesInKeyAlt;
		this.numberOfAttributeInsertionsInNewTables = numberOfAttributeInsertionsInNewTables;
		this.numberOfAttributeDeletionsWithDeletedTables = numberOfAttributeDeletionsWithDeletedTables;
		this.totalUpdatesInTransition = totalUpdatesInTransitions;
		
		this.totalTableInsertionsDeletions = this.numberOfTablesInsertions+ this.numberOfTablesDeletions;
		this.totalAttributeInsertionsDeletions = this.numberOfAttributeInsertions + this.numberOfAttributeDeletions;
		this.totalAttributeUpdates = this.numberOfAttributeWithTypeAlt + this.numberOfAttributeInKeyAlt;
		this.totalAttributeChange = this.totalAttributeInsertionsDeletions + this.totalAttributeUpdates +   this.numberOfAttributeInsertionsInNewTables + this.numberOfAttributeDeletionsWithDeletedTables;
	}
	

	public int getTransitionId() {
		return transitionId;
	}
	public int getTime() {
		return time;
	}
	public String getOldVersionFile() {
		return oldVersionFile;
	}
	public String getNewVersionFile() {
		return newVersionFile;
	}
	public int getNumberOfOldTables() {
		return numberOfOldTables;
	}
	public int getNumberOfNewTables() {
		return numberOfNewTables;
	}
	public int getNumberOfOldAttributes() {
		return numberOfOldAtributes;
	}
	public int getNumberOfNewAttributes() {
		return numberOfNewAttributes;
	}
	public int getNumberOfTablesInsertions() {
		return numberOfTablesInsertions;
	}
	public int getNumberOfTablesDeletions() {
		return numberOfTablesDeletions;
	}
	public int getNumberOfAttributeInsertions() {
		return numberOfAttributeInsertions;
	}
	public int getNumberOfAttributeDeletions() {
		return numberOfAttributeDeletions;
	}
	public int getNumberOfAttributesWithTypeAlt() {
		return numberOfAttributeWithTypeAlt;
	}
	public int getNumberOfAttributesInKeyAlt() {
		return numberOfAttributeInKeyAlt;
	}
	public int getNumberOfAttributeInsertionsInNewTables() {
		return numberOfAttributeInsertionsInNewTables;
	}
	public int getNumberOfAttributeDeletionsWithDeletedTables() {
		return numberOfAttributeDeletionsWithDeletedTables;
	}
	public int getTotalTableInsertionsDeletions() {
		return totalTableInsertionsDeletions;
	}
	public int getTotalAttributeInsertionsDeletions() {
		return totalAttributeInsertionsDeletions;
	}
	public int getTotalAttributeUpdates() {
		return totalAttributeUpdates;
	}
	public int getTimeDistanceFromPrevious() {
		return timeDistanceFromPrevious;
	}
	public int getTotalAttributeChange() {
		return totalAttributeChange;
	}
	public int getTotalUpdatesInTransition(){
		return totalUpdatesInTransition;
	}
	public void setTimeDistanceFromPrevious(int timeDistanceFromPrevious) {
		this.timeDistanceFromPrevious = timeDistanceFromPrevious;
	}
	
	public String toStringShort(){
		String shortDescription = new String();
		shortDescription =  transitionId + "\t" + time + "\t" + timeDistanceFromPrevious + "\t" + numberOfTablesInsertions + "\t" + numberOfTablesDeletions + "\t" + numberOfAttributeInsertions + "\t" + numberOfAttributeDeletions + "\t" + numberOfAttributeWithTypeAlt + "\t" + numberOfAttributeInKeyAlt + "\t" + numberOfAttributeInsertionsInNewTables + "\t" + numberOfAttributeDeletionsWithDeletedTables; 
		return shortDescription;
	}

}

