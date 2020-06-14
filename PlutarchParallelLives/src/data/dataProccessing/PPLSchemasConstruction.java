package data.dataProccessing;

import gr.uoi.cs.daintiness.hecate.sql.Attribute;
import gr.uoi.cs.daintiness.hecate.sql.Schema;
import gr.uoi.cs.daintiness.hecate.sql.Table;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import data.dataPPL.pplSQLSchema.PPLAttribute;
import data.dataPPL.pplSQLSchema.PPLSchema;
import data.dataPPL.pplSQLSchema.PPLTable;

public class PPLSchemasConstruction {

	private static ArrayList<Schema> allSchemas = new ArrayList<Schema>();
	private static TreeMap<String,PPLSchema> allPPLSchemas = null;

	public PPLSchemasConstruction(ArrayList<Schema> tempAllSchemas){
		allPPLSchemas = new TreeMap<String,PPLSchema>();
		allSchemas = new ArrayList<Schema>();
		allSchemas=tempAllSchemas;
	}
	
	
	public void makePPLSchemas(){
		for(int i=0; i<allSchemas.size(); i++){
			Schema hecSchema = new Schema();
			hecSchema=allSchemas.get(i);
			PPLSchema tempPPLSchema =createPPLSchema(hecSchema);
			allPPLSchemas.put(tempPPLSchema.getName(),tempPPLSchema);
		}
	}
	
	private PPLSchema createPPLSchema(Schema hecateSchema){
		TreeMap<String,Table> hecateTables = new TreeMap<String,Table>();
		hecateTables=hecateSchema.getTables();
		PPLSchema tmpPPLSchema = new PPLSchema(hecateSchema.getName(),hecateSchema);
		for (Map.Entry<String, Table> table : hecateTables.entrySet()) {
			PPLTable tmpPPLTable = new PPLTable(table.getValue().getName(),table.getValue());
			TreeMap<String,Attribute> hecateAttributes = new TreeMap<String,Attribute>();
			hecateAttributes=table.getValue().getAttrs();
			for (Map.Entry<String, Attribute> a : hecateAttributes.entrySet()) {
				PPLAttribute tmpPPLAttribute = new PPLAttribute(a.getValue());
				tmpPPLTable.addAttribute(tmpPPLAttribute);
			}
			tmpPPLSchema.addTable(tmpPPLTable);
		}
		return tmpPPLSchema;
	}
	
	public TreeMap<String,PPLSchema> getAllPPLSchemas(){

		return allPPLSchemas;

	}
	
}
