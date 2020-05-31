package gui.colorPicker;

import java.awt.Color;

import gui.configurations.DataConfiguration;

public class ColourD extends ColourPickerFactory
{
	public ColourD(){
		type = "D";
		id = 1;
	}
	
	@Override
	public Color draw(int numericValue, DataConfiguration dataConfiguration) {
		Color insersionColor;
		if(numericValue==0){
			insersionColor=new Color(255,231,186);
		}
		else if(numericValue>0 && numericValue<=dataConfiguration.getSegmentSizeDetailedTable()[2]){
			
			insersionColor=new Color(255,106,106);
    	}
		else if(numericValue>dataConfiguration.getSegmentSizeDetailedTable()[2] && numericValue<=2*dataConfiguration.getSegmentSizeDetailedTable()[2]){
			insersionColor=new Color(255,0,0);
		}
		else if(numericValue>2*dataConfiguration.getSegmentSizeDetailedTable()[2] && numericValue<=3*dataConfiguration.getSegmentSizeDetailedTable()[2]){
			
			insersionColor=new Color(205,0,0);
		}
		else{
			insersionColor=new Color(139,0,0);
		}
		return insersionColor;
	}

	
}
