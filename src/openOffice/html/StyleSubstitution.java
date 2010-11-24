package openOffice.html;

import xml.Attribute;


public class StyleSubstitution implements StyleTranslator {
	
	private String source;
	private String destination;
	
	
	public StyleSubstitution(String source, String destination) {
		this.source = source;
		this.destination = destination;
	}
	
	
	public String getSource() {
		return source;
	}
	public String getDestination() {
		return destination;
	}
	
	
	public String translate(Attribute source) {
		if (!source.getName().equals(this.source)) return null;
		
		String style = destination + ": " + source.getValue() + ";";
		
		return style;
	}
	
}