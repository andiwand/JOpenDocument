package openoffice.html;

import xml.Content;
import xml.Node;


public class TextNodeSubstitution extends NodeSubstitution {
	
	public TextNodeSubstitution(String source, String destination,
			AttributeSubstitution... substitutions) {
		super(source, destination, substitutions);
	}
	
	@Override
	public Node translateNode(Node source) {
		Node result = super.translateNode(source);
		
		if (source.isEmpty()) result.addChild(new Content("&nbsp;"));
		
		return result;
	}
	
}