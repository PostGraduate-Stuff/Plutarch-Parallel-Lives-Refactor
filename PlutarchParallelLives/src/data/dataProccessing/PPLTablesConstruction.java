package data.dataProccessing;

import java.util.Map;
import java.util.TreeMap;

import data.dataPPL.pplSQLSchema.PPLSchema;
import data.dataPPL.pplSQLSchema.PPLTable;

public class PPLTablesConstruction {
	
	private static TreeMap<String,PPLSchema> allPPLSchemas = new TreeMap<String,PPLSchema>();
	private TreeMap<String,PPLTable> allPPLTables = null;

	public PPLTablesConstruction(TreeMap<String,PPLSchema> tmpAllPPLSchemas){
		allPPLTables=new TreeMap<String,PPLTable>();
		allPPLSchemas=tmpAllPPLSchemas;
	}
	
	public void makeAllPPLTables(){
		
		int versionID=0;
		
		for (Map.Entry<String,PPLSchema> pplSchema : allPPLSchemas.entrySet()) {
			PPLSchema oneSchema = pplSchema.getValue();
			fillAllPPLTables(oneSchema,versionID);
			findTable(oneSchema,versionID);
			versionID++;
			
		}
		
	}
	
	private void fillAllPPLTables(PPLSchema oneSchema, int versionID){
		for(int j=0; j<oneSchema.getTables().size(); j++){
			PPLTable oneTable=oneSchema.getTableAt(j);
			if(!allPPLTables.containsKey(oneTable.getName())){
				oneTable.setBirth(oneSchema.getName());
				oneTable.setBirthVersionID(versionID);
				oneTable.setDeath(allPPLSchemas.get(allPPLSchemas.lastKey()).getName());
				oneTable.setDeathVersionID(allPPLSchemas.size()-1);
				oneTable.setActive();
				allPPLTables.put(oneTable.getName(),oneTable);
				oneTable=new PPLTable();
				
			}
		}
	}
	
	private void findTable(PPLSchema oneSchema, int versionID)
	{
		boolean found=false;
		for (Map.Entry<String,PPLTable> pplTable : allPPLTables.entrySet()) {
			found=false;
			for(int tablePosition=0; tablePosition<oneSchema.getTables().size(); tablePosition++){
				PPLTable oneTable=oneSchema.getTableAt(tablePosition);
				if(pplTable.getKey().equals(oneTable.getName())){
					found=true;
					break;
				}
			}
			
			if(!found && pplTable.getValue().getActive()){
				pplTable.getValue().setDeath(oneSchema.getName());
				pplTable.getValue().setDeathVersionID(versionID);
				pplTable.getValue().setActive();

			}
		}
	}
	
	public TreeMap<String,PPLTable> getAllPPLTables(){
		return allPPLTables;
	}
}
