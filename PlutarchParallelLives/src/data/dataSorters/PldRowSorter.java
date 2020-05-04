package data.dataSorters;

import java.util.Map;
import java.util.TreeMap;

import data.dataKeeper.GlobalDataKeeper;
import data.dataPPL.pplSQLSchema.PPLTable;

public class PldRowSorter {
	

	public PldRowSorter(){
	}
	
	public String[][] sortRows(String[][] finalRows, TreeMap<String,PPLTable> pplTables){
		
		String[][] sortedRows=new String[finalRows.length][finalRows[0].length];
		
		PPLTableSortingClass tablesSorter=new PPLTableSortingClass();
		
	    Map<String, PPLTable> wtf=pplTables;//globalDataKeeper.getAllPPLTables();
	    int counter=0;
	    for(Map.Entry<String, PPLTable> ppl:tablesSorter.entriesSortedByBirthDeath(wtf)){
			for(int i=0; i<finalRows.length; i++ ){
				if (finalRows[i][0].equals(ppl.getKey())) {
					for(int j=0;j<finalRows[0].length; j++){
						sortedRows[counter][j]=finalRows[i][j];
						
					}                               
				}
			}
			
			counter++;
		}
		
		return sortedRows;
		
		
	}
	
}
