package openoffice.html;

import java.util.Map;

import xml.Attribute;


public class ClassAttributeTranslator implements AttributeTranslator {
	
	private final Map<String, String> parentStyles;
	
	public ClassAttributeTranslator(Map<String, String> parentStyles) {
		this.parentStyles = parentStyles;
	}
	
	public Attribute translate(Attribute source) {
		if (!source.getName().equals("style-name")) return null;
		
		Attribute result = new Attribute("class");
		
		String className = source.getValue();
		className = className.replaceAll("\\.", "_");
		
		String classString = "";
		classString += className;
		String parent = parentStyles.get(className);
		while (parent != null) {
			classString = parent + " " + classString;
			parent = parentStyles.get(parent);
		}
		
		result.setValue(classString);
		
		return result;
	}
	
}