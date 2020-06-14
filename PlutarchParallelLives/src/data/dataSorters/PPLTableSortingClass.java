package data.dataSorters;

import java.util.Comparator;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import data.dataPPL.pplSQLSchema.PPLTable;

public class PPLTableSortingClass {
	
	public SortedSet<Map.Entry<String,PPLTable>> entriesSortedByBirthDeath(Map<String,PPLTable> map) {
	    SortedSet<Map.Entry<String,PPLTable>> sortedEntries = new TreeSet<Map.Entry<String,PPLTable>>(
	        new Comparator<Map.Entry<String,PPLTable>>() {
	            @Override public int compare(Map.Entry<String,PPLTable> entry1, Map.Entry<String,PPLTable> entry2) {
	            	
	            	if (entry1.getValue().getBirthVersionID() < entry2.getValue().getBirthVersionID()) {
						return -1;
					}
		        	else if(entry1.getValue().getBirthVersionID() > entry2.getValue().getBirthVersionID()){
		        		return 1;
		        	}
		        	else{
		        		if (entry1.getValue().getDeathVersionID() < entry2.getValue().getDeathVersionID()) {
							return -1;
						}
		        		return 1;
		        	}
		        }
	        }
	    );
	    sortedEntries.addAll(map.entrySet());
	    return sortedEntries;
	}

}
