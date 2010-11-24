package openOffice.html;

import xml.Attribute;


public class ClassAttributeTranslator implements AttributeTranslator {
	
	public Attribute translate(Attribute source) {
		if (!source.getName().equals("style-name")) return null;
		
		Attribute result = new Attribute("class");
		
		String className = source.getValue();
		className = className.replaceAll("\\.", "_");
		
		result.setValue(className);
		
		return result;
	}
	
}