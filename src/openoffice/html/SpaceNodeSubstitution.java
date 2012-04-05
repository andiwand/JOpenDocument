package openoffice.html;

import xml.Attribute;
import xml.Content;
import xml.Node;


public class SpaceNodeSubstitution extends NodeSubstitution {
	
	public SpaceNodeSubstitution() {
		super("s", "span");
	}
	
	@Override
	public Node translateNode(Node source) {
		Node result = super.translateNode(source);
		
		int spaceCount = 1;
		Attribute countAttribute = source.findAttribute("c");
		if (countAttribute != null) spaceCount = Integer.valueOf(countAttribute.getValue());
		
		result.addAttribute(new Attribute("style", "white-space: pre;"));
		String space = "";
		for (int i = 0; i < spaceCount; i++)
			space += " ";
		result.addChild(new Content(space));
		
		return result;
	}
	
}