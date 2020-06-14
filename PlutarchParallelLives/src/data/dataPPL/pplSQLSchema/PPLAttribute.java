package data.dataPPL.pplSQLSchema;

import gr.uoi.cs.daintiness.hecate.sql.Attribute;

public class PPLAttribute {

	private Attribute hecAttribute;
	
	
	public PPLAttribute(Attribute tempHecAttribute){
		hecAttribute=tempHecAttribute;
	}
	
	public String getName(){
		return hecAttribute.getName();
	}
	
	public Attribute getHecAttribute(){
		return hecAttribute;
	}
	
}
