package test;

import openOffice.OpenDocumentText;
import xml.Content;
import xml.Document;
import xml.Node;
import xml.reader.DomEventListener;
import xml.reader.DomEventReader;
import xml.reader.SaxDocumentReader;


public class TestDomEventReader {
	
	public static void main(String[] args) throws Throwable {
		OpenDocumentText documentText = new OpenDocumentText("/home/andreas/test.odt");
		
		SaxDocumentReader documentReader = new SaxDocumentReader(documentText.getContent());
		
		Document document = documentReader.readDocument();
		
		DomEventReader domEventReader = new DomEventReader();
		domEventReader.addListener(new DefaultListener());
		domEventReader.readDocument(document);
	}
	
	
	private static class DefaultListener implements DomEventListener {
		public boolean matchNode(Node node) {
			return true;
		}
		
		public void nodeOccurred(Node node) {
			System.out.println(node.getName());
		}
		public void contentOccurred(Content content) {
			System.out.println("	" + content.getContent());
		}
	}
	
}