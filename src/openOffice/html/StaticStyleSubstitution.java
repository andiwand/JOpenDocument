package openOffice.html;

import xml.Attribute;


public class StaticStyleSubstitution extends StyleSubstitution {
	
	private String destinationValue;
	
	
	public StaticStyleSubstitution(String source, String destinationName, String destinationValue) {
		super(source, destinationName);
		
		this.destinationValue = destinationValue;
	}
	
	
	public String getDestinationValue() {
		return destinationValue;
	}
	
	
	public String translate(Attribute source) {
		if (!source.getName().equals(getSource())) return null;
		
		String style = getDestination() + ": " + destinationValue + ";";
		
		return style;
	}
	
}