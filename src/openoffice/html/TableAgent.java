package openoffice.html;

import java.util.ArrayList;
import java.util.List;

import xml.Attribute;
import xml.Node;


public class TableAgent extends NodeSubstitution {
	
	public TableAgent(AttributeSubstitution... substitutions) {
		super("table", "table", substitutions);
	}
	
	
	@Override
	public Node translateNode(Node source) {
		Node result = super.translateNode(source);
		
		List<String> defaultStyles = new ArrayList<String>();
		
		for (Node childNode : source.getChildNodes()) {
			if (childNode.getName().equals("table-column")) {
				Attribute defaultStyleAttribute = childNode.findAttribute("default-cell-style-name");
				if (defaultStyleAttribute == null) continue;
				
				int columnsRepeated = 1;
				Attribute columnsRepeatedAttribute = childNode.findAttribute("number-columns-repeated");
				if (columnsRepeatedAttribute != null)
					columnsRepeated = Integer.valueOf(columnsRepeatedAttribute.getValue());
				
				for (int i = 0; i < columnsRepeated; i++)
					defaultStyles.add(defaultStyleAttribute.getValue());
			} else if (childNode.getName().equals("table-row")) {
				int cellIndex = 0;
				for (Node cell : childNode.getChildNodes()) {
					if (!cell.hasAttribute("style-name") && (cellIndex < defaultStyles.size()))
						cell.addAttribute(new Attribute("table:style-name", defaultStyles.get(cellIndex)));
					cellIndex++;
				}
			}
		}
		
		return result;
	}
	
}