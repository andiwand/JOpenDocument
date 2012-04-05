package openoffice.html;

import java.util.HashMap;
import java.util.Map;

import xml.Attribute;
import xml.Node;


public class NodeSubstitution implements NodeTranslator {
	
	private String source;
	private String destination;
	
	private Map<String, AttributeTranslator> translators;
	
	public NodeSubstitution(String source, String destination,
			AttributeSubstitution... substitutions) {
		this.source = source;
		this.destination = destination;
		
		translators = new HashMap<String, AttributeTranslator>();
		
		for (AttributeSubstitution substitution : substitutions) {
			translators.put(substitution.getSource(), substitution);
		}
	}
	
	public String getSource() {
		return source;
	}
	
	public String getDestination() {
		return destination;
	}
	
	public void addAttributeTranslator(String source,
			AttributeTranslator attributeTranslator) {
		translators.put(source, attributeTranslator);
	}
	
	public void addAttributeSubstitution(
			AttributeSubstitution attributeSubstitution) {
		translators.put(attributeSubstitution.getSource(),
				attributeSubstitution);
	}
	
	public Node translateNode(Node source) {
		if (!source.getName().equals(this.source)) return null;
		
		Node result = new Node(destination);
		
		for (Attribute sourceAttribute : source.getAttributes()) {
			if (translators.containsKey(sourceAttribute.getName())) {
				AttributeTranslator translator = translators.get(sourceAttribute.getName());
				
				Attribute newAttribute = translator.translate(sourceAttribute);
				
				result.addAttribute(newAttribute);
			}
		}
		
		return result;
	}
	
}