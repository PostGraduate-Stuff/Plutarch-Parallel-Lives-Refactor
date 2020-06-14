/**
 * 
 */
package phaseAnalyzer.parser;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.Scanner;
import java.util.StringTokenizer;

import phaseAnalyzer.commons.TransitionHistory;
import phaseAnalyzer.commons.TransitionStats;

public class SimpleTextParser implements IParser {
	
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
	private int numberOfAttributesWithTypeAlt; 
	private int numberOfAttributesInKeyAlt;
	private int numberOfAttributeInsertionsInNewTables; 
	private int numberOfAttributeDeletionsWithDeletedTables;
	private int totalUpdatesInOneTransition;
	private int totalUpdates=0;
	
	private final int _NumFields = 16;
	
	private Scanner inputStream = null;
	
	@Override
	public TransitionHistory parse(String fileName, String delimeter) {
		
		try {
			System.out.println("csv:"+fileName);
			inputStream = new Scanner(new FileInputStream(fileName));
			
		} catch (FileNotFoundException e) {
			System.out.println("Problem opening files.");
			System.exit(0);
		}
		TransitionHistory transitionHistory = readData(delimeter);
		inputStream.close();
	    if (transitionHistory.getValues().size() == 0)
    	{
    		return transitionHistory;
    	}
	    setTransitions(transitionHistory);
		return transitionHistory;
	}
	
	
	private TransitionHistory readData(String delimeter){
		TransitionHistory transitionHistory =  new TransitionHistory();
		String line = inputStream.nextLine();
		int count =1;
		while (inputStream.hasNextLine()) {
			line = inputStream.nextLine();
			count++;
			StringTokenizer tokenizer = new StringTokenizer(line, delimeter);
			if(tokenizer.countTokens() != _NumFields){
				System.out.println("Wrong Input format. I found " + tokenizer.countTokens() + " values, but I expect " + _NumFields + " values per row!");
				System.exit(0);				
			}
			readFields(tokenizer);
			totalUpdatesInOneTransition=numberOfAttributeInsertions+numberOfAttributeDeletions+numberOfAttributesWithTypeAlt+numberOfAttributesInKeyAlt+numberOfAttributeInsertionsInNewTables+numberOfAttributeDeletionsWithDeletedTables;
			TransitionStats stats = new TransitionStats(transitionId, time, oldVersionFile, newVersionFile, numberOfOldTables, numberOfNewTables, numberOfOldAtributes, numberOfNewAttributes, numberOfTablesInsertions, numberOfTablesDeletions, numberOfAttributeInsertions,numberOfAttributeDeletions,numberOfAttributesWithTypeAlt, numberOfAttributesInKeyAlt, numberOfAttributeInsertionsInNewTables, numberOfAttributeDeletionsWithDeletedTables,totalUpdatesInOneTransition);
			transitionHistory.addValue(stats);
			totalUpdates= totalUpdates+numberOfAttributeInsertions+numberOfAttributeDeletions+numberOfAttributesWithTypeAlt+numberOfAttributesInKeyAlt+numberOfAttributeInsertionsInNewTables+numberOfAttributeDeletionsWithDeletedTables;
		}
		transitionHistory.setTotalUpdates(totalUpdates);
		transitionHistory.setTotalTime();
		System.out.println(count + " lines parsed");
		return transitionHistory;
	}
	
	private void readFields(StringTokenizer tokenizer){
		String token = tokenizer.nextToken();
		transitionId = Integer.parseInt(token);
		token = tokenizer.nextToken();
		time = Integer.parseInt(token);		
		oldVersionFile = tokenizer.nextToken();
		newVersionFile = tokenizer.nextToken();
		token = tokenizer.nextToken();
		numberOfOldTables = Integer.parseInt(token);		
		token = tokenizer.nextToken();
		numberOfNewTables= Integer.parseInt(token);  
		token = tokenizer.nextToken();
		numberOfOldAtributes= Integer.parseInt(token);  
		token = tokenizer.nextToken();
		numberOfNewAttributes= Integer.parseInt(token);  
		token = tokenizer.nextToken();
		numberOfTablesInsertions= Integer.parseInt(token);  
		token = tokenizer.nextToken();
		numberOfTablesDeletions= Integer.parseInt(token);  
		token = tokenizer.nextToken();
		numberOfAttributeInsertions= Integer.parseInt(token); 
		token = tokenizer.nextToken();
		numberOfAttributeDeletions= Integer.parseInt(token); 
		token = tokenizer.nextToken();
		numberOfAttributesWithTypeAlt= Integer.parseInt(token);  
		token = tokenizer.nextToken();
		numberOfAttributesInKeyAlt= Integer.parseInt(token);  
		token = tokenizer.nextToken();
		numberOfAttributeInsertionsInNewTables= Integer.parseInt(token);  
		token = tokenizer.nextToken();
		numberOfAttributeDeletionsWithDeletedTables= Integer.parseInt(token);
	}

	private void setTransitions(TransitionHistory transitionHistory){
		Iterator<TransitionStats> transitionsIter = transitionHistory.getValues().iterator();
	    TransitionStats previousTransition, currentTransition;
	    currentTransition = transitionsIter.next();
	    currentTransition.setTimeDistanceFromPrevious(0);
	    previousTransition = currentTransition;
	    
	    while (transitionsIter.hasNext()){
		    currentTransition = transitionsIter.next();
		    currentTransition.setTimeDistanceFromPrevious(currentTransition.getTime() - previousTransition.getTime());
		    previousTransition = currentTransition;
	    }
	}
}
