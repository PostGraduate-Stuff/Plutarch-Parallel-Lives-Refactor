package data.dataPPL.pplSQLSchema;

import gr.uoi.cs.daintiness.hecate.sql.Schema;

import java.util.Map;
import java.util.TreeMap;

public class PPLSchema {

	private String name;
	private TreeMap<String, PPLTable> tables;

	public PPLSchema(TreeMap<String, PPLTable> tableTreemap) {
		this.tables = tableTreemap;
	}	
	
	public PPLSchema() {
		this.tables = new TreeMap<String, PPLTable>();
	}
	
	public PPLSchema(String name) {
		this.tables = new TreeMap<String, PPLTable>();
		this.name = name;
	}

	public PPLSchema(String tempName,Schema tempHecSchema){
		this.name=tempName;
		this.tables = new TreeMap<String, PPLTable>();
	}
	
	public String getName() {
		return name;
	}
	
	public TreeMap<String, PPLTable> getTables() {
		return this.tables;
	}
	
	public void addTable(PPLTable table) {
		this.tables.put(table.getName(), table);
	}
	
	public String toString() {
		return name;
	}

	public void setTitle(String title) {
		this.name = title;
	}

	public PPLTable getTableAt(int i) {
		int helper = 0;
		if(i < 0 || i >= tables.size()){
			return null;
		}
		
		for (Map.Entry<String, PPLTable> table : tables.entrySet()) {
			if (helper == i) {
				return table.getValue();
			}
			helper++;
		}
		
		return null;
	}
}
