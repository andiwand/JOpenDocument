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
		
		List<Node> table = source.getChildNodes();
		source.clearChildren();
		for (Node childNode : table) {
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
				List<Node> row = childNode.getChildNodes();
				childNode.clearChildren();
				
				int cellIndex = 0;
				for (Node cell : row) {
					if (!cell.hasAttribute("style-name") && (cellIndex < defaultStyles.size()))
						cell.addAttribute(new Attribute("table:style-name", defaultStyles.get(cellIndex)));
					
					int columnsRepeated = 1;
					Attribute columnsRepeatedAttribute = cell.findAttribute("number-columns-repeated");
					if (columnsRepeatedAttribute != null)
						columnsRepeated = Integer.valueOf(columnsRepeatedAttribute.getValue());
					
					for (int i = 0; i < columnsRepeated; i++) {
						childNode.addChild(new Node(cell));
						cellIndex++;
					}
				}
				
				Attribute rowsRepeatedAttribute = childNode.findAttribute("number-rows-repeated");
				if (rowsRepeatedAttribute != null) {
					int columnsRepeated = Integer.valueOf(rowsRepeatedAttribute.getValue());
					
					for (int i = 1; i < columnsRepeated; i++)
						source.addChild(new Node(childNode));
				}
			}
			
			source.addChild(childNode);
		}
		
		return result;
	}
	
}