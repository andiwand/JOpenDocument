package openOffice.html;

import xml.Attribute;


public class AttributeSubstitution implements AttributeTranslator {
	
	private String source;
	private String destination;
	
	
	public AttributeSubstitution(String source, String destination) {
		this.source = source;
		this.destination = destination;
	}
	
	
	public String getSource() {
		return source;
	}
	public String getDestination() {
		return destination;
	}
	
	
	public Attribute translate(Attribute source) {
		if (!source.getName().equals(this.source)) return null;
		
		Attribute result = new Attribute(destination);
		result.setValue(source.getValue());
		
		return result;
	}
	
}