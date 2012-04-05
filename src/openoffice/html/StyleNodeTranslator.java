package openoffice.html;

import java.util.HashMap;
import java.util.Map;

import xml.Attribute;
import xml.Node;


public class StyleNodeTranslator {
	
	private Map<String, StyleTranslator> translators;
	
	public StyleNodeTranslator(StyleSubstitution... substitutions) {
		translators = new HashMap<String, StyleTranslator>();
		
		for (StyleSubstitution substitution : substitutions) {
			addStyleSubstitution(substitution);
		}
	}
	
	public void addStyleTranslator(String source,
			StyleTranslator styleTranslator) {
		translators.put(source, styleTranslator);
	}
	
	public void addStyleSubstitution(StyleSubstitution styleSubstitution) {
		addStyleTranslator(styleSubstitution.getSource(), styleSubstitution);
	}
	
	public String translate(Node source) {
		String result = "";
		
		for (Attribute attribute : source.getAttributes()) {
			if (translators.containsKey(attribute.getName())) {
				StyleTranslator styleTranslator = translators.get(attribute
						.getName());
				
				result += styleTranslator.translate(attribute);
			}
		}
		
		return result;
	}
	
}