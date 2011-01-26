package openoffice.html;

import xml.Attribute;
import xml.Content;
import xml.Node;


public class TabNodeSubstitution extends NodeSubstitution {
	
	public TabNodeSubstitution() {
		super("tab", "span");
	}
	
	
	@Override
	public Node translateNode(Node source) {
		Node result = super.translateNode(source);
		
		result.addAttribute(new Attribute("style", "white-space: pre;"));
		result.addChild(new Content("\t"));
		
		return result;
	}
	
}