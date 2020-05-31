package gui.colorPicker;

import java.awt.Color;

import gui.configurations.DataConfiguration;

public abstract class ColourPickerFactory implements Cloneable, IColourPicker
{
	protected int id;
	protected String type;
   
	public abstract Color draw(int numericValue, final DataConfiguration dataConfiguration);
   
	public String getType(){
		return type;
	}
   
	public int getId() {
		return id;
	}
   
	public Object clone() {
		Object clone = null;
      
	try {
		clone = super.clone();
         
	} catch (CloneNotSupportedException e) {
		e.printStackTrace();
	}
      
	return clone;
	}
}
