package openoffice.html.odt;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import openoffice.OpenDocumentText;
import openoffice.html.AttributeSubstitution;
import openoffice.html.AttributeTranslator;
import openoffice.html.ClassAttributeTranslator;
import openoffice.html.NodeSubstitution;
import openoffice.html.NodeTranslator;
import openoffice.html.StaticStyleSubstitution;
import openoffice.html.StyleNodeTranslator;
import openoffice.html.StyleSubstitution;
import openoffice.html.TableStyleNodeTranslator;

import org.xml.sax.SAXException;

import xml.Attribute;
import xml.Content;
import xml.Document;
import xml.Element;
import xml.Node;
import xml.RootNode;
import xml.reader.SaxDocumentReader;


public class TranslatorOdt {
	
	private Document document;
	
	private Map<String, StyleNodeTranslator> styleNodeTranslators;
	
	private Map<String, NodeTranslator> translators;
	private Map<String, AttributeTranslator> attributeTranslators;
	
	
	public TranslatorOdt(OpenDocumentText documentText) throws ParserConfigurationException, SAXException, IOException {
		SaxDocumentReader documentReader = new SaxDocumentReader(documentText.getContent());
		document = documentReader.readDocument();
		
		styleNodeTranslators = new HashMap<String, StyleNodeTranslator>();
		
		translators = new HashMap<String, NodeTranslator>();
		attributeTranslators = new HashMap<String, AttributeTranslator>();
		
		addStyleNodeTranslator("text-properties", new StyleNodeTranslator(
				new StyleSubstitution("font-size", "font-size"),
				new StyleSubstitution("font-weight", "font-weight"),
				new StyleSubstitution("font-style", "font-style"),
				new StaticStyleSubstitution("text-underline-style", "text-decoration", "underline")
		));
		addStyleNodeTranslator("table-properties", new TableStyleNodeTranslator(
				new StyleSubstitution("width", "width")
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
		addNodeSubstitution(new NodeSubstitution("table", "table"));
		addNodeSubstitution(new NodeSubstitution("table-row", "tr"));
		addNodeSubstitution(new NodeSubstitution("table-cell", "td"));
		addNodeSubstitution(new NodeSubstitution("frame", "span"));
		
		addAttributeTranslators("style-name", new ClassAttributeTranslator());
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
	
	
	public HtmlPageOdt translate(int page) {
		HtmlPageOdt result = new HtmlPageOdt(page);
		
		RootNode documentRoot = document.getRoot();
		RootNode htmlRoot = result.getHtmlNode();
		
		for (Node node : documentRoot.getChildNodes()) {
			if (node.getName().equals("automatic-styles")) htmlRoot.addChild(handleStyle(node));
			else if (node.getName().equals("body")) htmlRoot.addChild(handleContent(node));
		}
		
		return result;
	}
	
	
	
	private Node handleStyle(Node style) {
		Node head = new Node("head");
		
		/*Node meta = new Node("meta");
		meta.addAttribute(new Attribute("http-equiv", "Content-Type"));
		meta.addAttribute(new Attribute("content", "text/html; charset=utf-8"));
		
		head.addChild(meta);*/
		
		Node title = new Node("title");
		title.addChild(new Content("Odt Translator"));
		head.addChild(title);
		
		Node css = new Node("style");
		css.addAttribute(new Attribute("type", "text/css"));
		css.addAttribute(new Attribute("media", "screen"));
		
		for (Node styleNode : style.getChildNodes()) {
			String name = styleNode.findAttribute("name").getValue();
			name = name.replaceAll("\\.", "_");
			name = "." + name;
			
			String cssString = name + "{";
			
			for (Node childStyle : styleNode.getChildNodes()) {
				if (styleNodeTranslators.containsKey(childStyle.getName())) {
					StyleNodeTranslator translator = styleNodeTranslators.get(childStyle.getName());
					
					cssString += translator.translate(childStyle);
				}
			}
			
			cssString += "}";
			
			css.addChild(new Content(cssString));
		}
		
		head.addChild(css);
		
		return head;
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
				
				parent.addChild(new Content(newContent));
			}
		}
	}
	
}