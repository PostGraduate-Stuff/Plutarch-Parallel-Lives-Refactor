package gui.colorPicker;

import java.awt.Color;

import gui.configurations.DataConfiguration;

public class ColourU extends ColourPickerFactory
{
	public ColourU(){
		type = "U";
		id = 1;
	}
	
	@Override
	public Color draw(int numericValue, DataConfiguration dataConfiguration) {
		Color insersionColor;
		if(numericValue==0){
			insersionColor=new Color(255,231,186);
		}
		else if(numericValue>0 && numericValue<=dataConfiguration.getSegmentSizeDetailedTable()[1]){
			
			insersionColor=new Color(176,226,255);
    	}
		else if(numericValue>dataConfiguration.getSegmentSizeDetailedTable()[1] && numericValue<=2*dataConfiguration.getSegmentSizeDetailedTable()[1]){
			insersionColor=new Color(92,172,238);
		}
		else if(numericValue>2*dataConfiguration.getSegmentSizeDetailedTable()[1] && numericValue<=3*dataConfiguration.getSegmentSizeDetailedTable()[1]){
			
			insersionColor=new Color(28,134,238);
		}
		else{
			insersionColor=new Color(16,78,139);
		}
		return insersionColor;
	}

	
}
