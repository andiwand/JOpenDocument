package xml.reader;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import xml.Attribute;
import xml.Content;
import xml.Document;
import xml.Node;
import xml.RootNode;


// TODO error handling
public class SaxDocumentReader extends XmlDocumentReader {
	
	private InputStream inputStream;
	private SAXParser parser;
	
	
	public SaxDocumentReader(InputStream inputStream) throws ParserConfigurationException, SAXException {
		this.inputStream = inputStream;
		
		SAXParserFactory parserFactory = SAXParserFactory.newInstance();
		parser = parserFactory.newSAXParser();
	}
	
	
	public Document readDocument() throws IOException, SAXException {
		XmlSaxHandler handler = new XmlSaxHandler();
		
		parser.parse(inputStream, handler);
		
		return handler.getDocument();
	}
	
	
	public void close() throws IOException {
		inputStream.close();
	}
	
	
	private static class XmlSaxHandler extends DefaultHandler {
		private Document document;
		private RootNode rootNode;
		private Node activeNode;
		private int depth;
		
		public Document getDocument() {
			return document;
		}
		
		public void endDocument() throws SAXException {
			document = new Document(rootNode);
		}
		
		public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
			Node newNode = new Node(qName);
			
			System.out.println("---");
			System.out.println(uri);
			System.out.println(localName);
			System.out.println(qName != null ? qName : "null");
			System.out.println("---");
			System.out.println();
			
			for (int i = 0; i < attributes.getLength(); i++) {
				Attribute attribute = new Attribute(attributes.getQName(i));
				attribute.setValue(attributes.getValue(i));
				
				newNode.addAttribute(attribute);
			}
			
			if (depth == 0) {
				rootNode = new RootNode(newNode);
				newNode = rootNode;
			} else {
				activeNode.addChild(newNode);
			}
			
			activeNode = newNode;
			
			depth++;
		}
		public void endElement(String uri, String localName, String qName) throws SAXException {
			activeNode = activeNode.getParent();
			
			depth--;
		}
		
		public void characters(char[] ch, int start, int length) throws SAXException {
			String contentString = new String(ch, start, length);
			Content content = new Content(contentString);
			
			activeNode.addChild(content);
		}
		
		
	}
	
}