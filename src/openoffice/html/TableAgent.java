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
		int maxColumns = 0;
		int rows = 0;
		
		int totalRows = 0;
		
		for (Node childNode : table) {
			if (childNode.getName().equals("table-row")) {
				List<Node> row = childNode.getChildNodes();
				int totalColumns = 0;
				int columns = 0;
				
				for (Node cell : row) {
					int columnsRepeated = 1;
					Attribute columnsRepeatedAttribute = cell
							.findAttribute("number-columns-repeated");
					if (columnsRepeatedAttribute != null)
						columnsRepeated = Integer
								.valueOf(columnsRepeatedAttribute.getValue());
					
					totalColumns += columnsRepeated;
					
					if (!cell.isEmpty()) columns = totalColumns;
				}
				
				if (columns > maxColumns) maxColumns = columns;
				
				int rowsRepeated = 1;
				Attribute rowsRepeatedAttribute = childNode
						.findAttribute("number-rows-repeated");
				if (rowsRepeatedAttribute != null)
					rowsRepeated = Integer.valueOf(rowsRepeatedAttribute
							.getValue());
				
				totalRows += rowsRepeated;
				if (columns != 0) rows = totalRows;
			}
		}
		
		int styleColumn = 0;
		int tableRow = 0;
		
		tableLoop:
		for (Node childNode : table) {
			if (childNode.getName().equals("table-column")) {
				Attribute defaultStyleAttribute = childNode
						.findAttribute("default-cell-style-name");
				if (defaultStyleAttribute == null) continue;
				
				int columnsRepeated = 1;
				Attribute columnsRepeatedAttribute = childNode
						.findAttribute("number-columns-repeated");
				if (columnsRepeatedAttribute != null)
					columnsRepeated = Integer.valueOf(columnsRepeatedAttribute
							.getValue());
				
				for (int i = 0; i < columnsRepeated; i++) {
					if (styleColumn >= maxColumns) continue tableLoop;
					
					defaultStyles.add(defaultStyleAttribute.getValue());
					styleColumn++;
				}
				
				source.addChild(childNode);
			} else if (childNode.getName().equals("table-row")) {
				List<Node> row = childNode.getChildNodes();
				childNode.clearChildren();
				
				int cellIndex = 0;
				tableRowLoop:
				for (Node cell : row) {
					if (!cell.hasAttribute("style-name")
							&& (cellIndex < defaultStyles.size()))
						cell.addAttribute(new Attribute("table:style-name",
								defaultStyles.get(cellIndex)));
					
					int columnsRepeated = 1;
					Attribute columnsRepeatedAttribute = cell
							.findAttribute("number-columns-repeated");
					if (columnsRepeatedAttribute != null)
						columnsRepeated = Integer
								.valueOf(columnsRepeatedAttribute.getValue());
					
					for (int i = 0; i < columnsRepeated; i++) {
						if (cellIndex >= maxColumns) continue tableRowLoop;
						
						childNode.addChild(new Node(cell));
						cellIndex++;
					}
				}
				
				int rowsRepeated = 1;
				Attribute rowsRepeatedAttribute = childNode
						.findAttribute("number-rows-repeated");
				if (rowsRepeatedAttribute != null)
					rowsRepeated = Integer.valueOf(rowsRepeatedAttribute
							.getValue());
				
				for (int i = 0; i < rowsRepeated; i++) {
					if (tableRow >= rows) continue tableLoop;
					
					source.addChild(new Node(childNode));
					tableRow++;
				}
			}
		}
		
		return result;
	}
	
}