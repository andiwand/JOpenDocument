package openoffice.html.ods;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import openoffice.html.AttributeSubstitution;
import openoffice.html.AttributeTranslator;
import openoffice.html.ClassAttributeTranslator;
import openoffice.html.HtmlDocument;
import openoffice.html.NodeSubstitution;
import openoffice.html.NodeTranslator;
import openoffice.html.StaticStyleSubstitution;
import openoffice.html.StyleNodeTranslator;
import openoffice.html.StyleSubstitution;
import openoffice.html.TableAgent;
import openoffice.html.TableStyleNodeTranslator;

import org.xml.sax.SAXException;

import openoffice.OpenDocumentSpreadsheet;

import xml.Attribute;
import xml.Content;
import xml.Document;
import xml.Element;
import xml.Node;
import xml.RootNode;
import xml.reader.SaxDocumentReader;


public class TranslatorOds {
	
	private OpenDocumentSpreadsheet documentSpreadsheet;
	
	private Document style;
	private Document content;
	
	private Map<String, StyleNodeTranslator> styleNodeTranslators;
	
	private Map<String, NodeTranslator> translators;
	private Map<String, AttributeTranslator> attributeTranslators;
	
	private Map<String, String> parentStyles;
	
	
	public TranslatorOds(OpenDocumentSpreadsheet documentSpreadsheet) throws ParserConfigurationException, SAXException, IOException {
		this.documentSpreadsheet = documentSpreadsheet;
		
		SaxDocumentReader styleReader = new SaxDocumentReader(documentSpreadsheet.getStyles());
		style = styleReader.readDocument();
		SaxDocumentReader contentReader = new SaxDocumentReader(documentSpreadsheet.getContent());
		content = contentReader.readDocument();
		
		styleNodeTranslators = new HashMap<String, StyleNodeTranslator>();
		
		translators = new HashMap<String, NodeTranslator>();
		attributeTranslators = new HashMap<String, AttributeTranslator>();
		
		parentStyles = new HashMap<String, String>();
		
		
		addStyleNodeTranslator("text-properties", new StyleNodeTranslator(
				new StyleSubstitution("font-size", "font-size"),
				new StyleSubstitution("font-weight", "font-weight"),
				new StyleSubstitution("font-style", "font-style"),
				new StaticStyleSubstitution("text-underline-style", "text-decoration", "underline")
		));
		addStyleNodeTranslator("table-properties", new TableStyleNodeTranslator(
				new StyleSubstitution("width", "width")
		));
		addStyleNodeTranslator("table-row-properties", new StyleNodeTranslator(
				new StyleSubstitution("row-height", "height")
		));
		addStyleNodeTranslator("table-column-properties", new StyleNodeTranslator(
				new StyleSubstitution("column-width", "width")
		));
		addStyleNodeTranslator("table-cell-properties", new StyleNodeTranslator(
				new StyleSubstitution("padding", "padding"),
				new StyleSubstitution("border", "border"),
				new StyleSubstitution("border-top", "border-top"),
				new StyleSubstitution("border-right", "border-right"),
				new StyleSubstitution("border-bottom", "border-bottom"),
				new StyleSubstitution("border-left", "border-left")
		));
		
		addNodeSubstitution(new NodeSubstitution("p", "p"));
		addNodeSubstitution(new NodeSubstitution("h", "p"));
		addNodeSubstitution(new TableAgent());
		addNodeSubstitution(new NodeSubstitution("table-row", "tr",
				new AttributeSubstitution("number-rows-repeated", "rowspan")
		));
		addNodeSubstitution(new NodeSubstitution("table-cell", "td",
				new AttributeSubstitution("number-rows-spanned", "rowspan"),
				new AttributeSubstitution("number-columns-spanned", "colspan")
		));
		addNodeSubstitution(new NodeSubstitution("table-column", "colgroup",
				new AttributeSubstitution("number-columns-repeated", "span")
		));
		addNodeSubstitution(new NodeSubstitution("frame", "span"));
		
		addAttributeTranslators("style-name", new ClassAttributeTranslator(parentStyles));
	}
	
	
	public void addStyleNodeTranslator(String source, StyleNodeTranslator styleNodeTranslator) {
		styleNodeTranslators.put(source, styleNodeTranslator);
	}
	
	public void addNodeTranslator(String source, NodeTranslator nodeTranslator) {
		translators.put(source, nodeTranslator);
	}
	public void addNodeSubstitution(NodeSubstitution nodeSubstitution) {
		addNodeTranslator(nodeSubstitution.getSource(), nodeSubstitution);
	}
	
	public void addAttributeTranslators(String source, AttributeTranslator attributeTranslator) {
		attributeTranslators.put(source, attributeTranslator);
	}
	public void addAttributeSubstitution(AttributeSubstitution attributeSubstitution) {
		addAttributeTranslators(attributeSubstitution.getSource(), attributeSubstitution);
	}
	
	
	public HtmlDocument translate() {
		return translate(0, documentSpreadsheet.getTableCount());
	}
	public HtmlDocument translate(int table) {
		return translate(table, 1);
	}
	public HtmlDocument translate(int startTable, int countTable) {
		if (countTable <= 0) throw new IndexOutOfBoundsException("countTable must be greater than 0!");
		if ((startTable + countTable) > documentSpreadsheet.getTableCount())
			throw new IndexOutOfBoundsException("startTable out of range!");
		
		HtmlDocument result = new HtmlDocument();
		RootNode htmlRoot = result.getHtmlNode();
		
		String cssString = "";
		
		RootNode styleRoot = style.getRoot();
		Node stylesNode = styleRoot.findChildNode("styles");
		cssString += handleStyle(stylesNode);
		
		RootNode contentRoot = content.getRoot();
		Node contentStylesNode = contentRoot.findChildNode("automatic-styles");
		cssString += handleStyle(contentStylesNode);
		Node content = contentRoot.findChildNode("body").findChildNode("spreadsheet");
		Node newContent = new Node(content.getQName());
		for (Node contentTable : content.getChildNodes().subList(startTable, startTable + countTable))
			newContent.addChild(new Node(contentTable));
		Node htmlBodyNode = handleContent(newContent);
		
		cssString = cssString.replaceAll("%", "&#37;");
		
		
		Node htmlHeadNode = new Node("head");
		
		/*Node meta = new Node("meta");
		meta.addAttribute(new Attribute("http-equiv", "Content-Type"));
		meta.addAttribute(new Attribute("content", "text/html; charset=utf-8"));
		
		head.addChild(meta);*/
		
		Node htmlTitleNode = new Node("title");
		htmlTitleNode.addChild(new Content("_title_"));
		htmlHeadNode.addChild(htmlTitleNode);
		
		Node htmlStyleNode = new Node("style");
		htmlStyleNode.addAttribute(new Attribute("type", "text/css"));
		htmlStyleNode.addAttribute(new Attribute("media", "screen"));
		htmlStyleNode.addChild(new Content(cssString));
		htmlHeadNode.addChild(htmlStyleNode);
		
		htmlRoot.addChild(htmlHeadNode);
		htmlRoot.addChild(htmlBodyNode);
		
		return result;
	}
	
	
	
	private String handleStyle(Node style) {
		StringBuilder cssStringBuilder = new StringBuilder();
		
		for (Node styleNode : style.getChildNodes()) {
			if (!styleNode.getName().equals("style")) continue;
			
			Attribute parentAttribute = styleNode.findAttribute("parent-style-name");
			if (parentAttribute != null) {
				String name = styleNode.findAttribute("name").getValue().replaceAll("\\.", "_");
				String parentName = parentAttribute.getValue().replaceAll("\\.", "_");
				
				parentStyles.put(name, parentName);
			}
			
			String name = styleNode.findAttribute("name").getValue();
			name = name.replaceAll("\\.", "_");
			name = "." + name;
			
			cssStringBuilder.append(name);
			cssStringBuilder.append("{");
			
			for (Node childStyle : styleNode.getChildNodes()) {
				if (styleNodeTranslators.containsKey(childStyle.getName())) {
					StyleNodeTranslator translator = styleNodeTranslators.get(childStyle.getName());
					
					cssStringBuilder.append(translator.translate(childStyle));
				}
			}
			
			cssStringBuilder.append("}");
		}
		
		return cssStringBuilder.toString();
	}
	
	private Node handleContent(Node content) {
		Node body = new Node("body");
		
		handleContentImpl(content, body);
		
		return body;
	}
	private void handleContentImpl(Node content, Node parent) {
		for (Element childElement : content.getChildren()) {
			if (childElement instanceof Node) {
				Node activeParent = parent;
				Node childNode = (Node) childElement;
				
				if (translators.containsKey(childNode.getName())) {
					NodeTranslator translator = translators.get(childNode.getName());
					
					Node node = translator.translateNode(childNode);
					Node newNode = new Node(node);
					newNode.clearAttributes();
					
					for (Attribute attribute : childNode.getAttributes()) {
						Attribute newAttribute = attribute;
						
						if (attributeTranslators.containsKey(attribute.getName())) {
							AttributeTranslator attributeTranslator = attributeTranslators.get(attribute.getName());
							newAttribute = attributeTranslator.translate(attribute);
						}
						
						if (node.hasAttribute(newAttribute.getName())) break;
						
						newNode.addAttribute(newAttribute);
					}
					
					for (Attribute attribute : node.getAttributes()) {
						Attribute newAttribute = new Attribute(attribute);
						
						newNode.addAttribute(newAttribute);
					}
					
					activeParent = newNode;
					parent.addChild(activeParent);
				}
				
				handleContentImpl(childNode, activeParent);
			} else if (childElement instanceof Content) {
				Content childContent = (Content) childElement;
				
				String newContent = childContent.getContent();
				newContent = newContent.replaceAll("ä", "&auml;");
				newContent = newContent.replaceAll("ö", "&ouml;");
				newContent = newContent.replaceAll("ü", "&uuml;");
				newContent = newContent.replaceAll("Ä", "&Auml;");
				newContent = newContent.replaceAll("Ö", "&Ouml;");
				newContent = newContent.replaceAll("Ü", "&Uuml;");
				newContent = newContent.replaceAll("ß", "&szlig;");
				newContent = newContent.replaceAll("<", "&lt;");
				newContent = newContent.replaceAll(">", "&gt;");
				newContent = newContent.replaceAll("%", "&#37;");
				
				parent.addChild(new Content(newContent));
			}
		}
	}
	
}