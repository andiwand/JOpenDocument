package openoffice.html;

import xml.Node;


public class TableStyleNodeTranslator extends StyleNodeTranslator {
	
	public TableStyleNodeTranslator(StyleSubstitution... substitutions) {
		super(substitutions);
	}
	
	public String translate(Node source) {
		String result = super.translate(source);
		
		result += "border-collapse: collapse;";
		
		return result;
	}
	
}