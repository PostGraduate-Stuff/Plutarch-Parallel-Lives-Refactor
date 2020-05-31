package gui.colorPicker;

import java.awt.Color;

import gui.configurations.DataConfiguration;

public class ColourI extends ColourPickerFactory
{
	public ColourI(){
		type = "I";
		id = 0;
	}
	
	@Override
	public Color draw(int numericValue, final DataConfiguration dataConfiguration) {
		Color insersionColor;
		if(numericValue==0){
			insersionColor=new Color(255,231,186);
		}
		else if(numericValue>0 && numericValue<=dataConfiguration.getSegmentSizeDetailedTable()[0]){
			
			insersionColor=new Color(193,255,193);
    	}
		else if(numericValue>dataConfiguration.getSegmentSizeDetailedTable()[0] && numericValue<=2*dataConfiguration.getSegmentSizeDetailedTable()[0]){
			insersionColor=new Color(84,255,159);
		}
		else if(numericValue>2*dataConfiguration.getSegmentSizeDetailedTable()[0] && numericValue<=3*dataConfiguration.getSegmentSizeDetailedTable()[0]){
			
			insersionColor=new Color(0,201,87);
		}
		else{
			insersionColor=new Color(0,100,0);
		}
		return insersionColor;
	}

}
