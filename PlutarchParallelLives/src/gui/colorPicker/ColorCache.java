package gui.colorPicker;

import java.util.Hashtable;

public class ColorCache {
	 private static Hashtable<String, ColourPickerFactory> colourMap  = new Hashtable<String, ColourPickerFactory>();

	   public static ColourPickerFactory getColour(String type) {
		   ColourPickerFactory cachedColour = colourMap.get(type);
	      return (ColourPickerFactory) cachedColour.clone();
	   }

	   public static void loadCache() {
	      ColourD colourD = new ColourD();
	      colourMap.put(colourD.getType(), colourD);

	      ColourI colourI = new ColourI();
	      colourMap.put(colourI.getType(), colourI);

	      ColourU colourU = new ColourU();
	      colourMap.put(colourU.getType(), colourU);
	   }
}
