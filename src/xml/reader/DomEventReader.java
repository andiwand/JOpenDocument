package xml.reader;

import java.util.ArrayList;
import java.util.List;

import xml.Content;
import xml.Document;
import xml.Element;
import xml.Node;
import xml.RootNode;


public class DomEventReader extends XmlEventReader {
	
	private List<DomEventListener> listeners;
	
	
	public DomEventReader() {
		listeners = new ArrayList<DomEventListener>();
	}
	
	
	public void addListener(DomEventListener listener) {
		listeners.add(listener);
	}
	
	
	public void readDocument(Document document) {
		RootNode rootNode = document.getRoot();
		
		readDocumentImpl(rootNode);
	}
	private void readDocumentImpl(Node node) {
		for (DomEventListener listener : listeners) {
			if (listener.matchNode(node)) listener.nodeOccurred(node);
		}
		
		for (Element element : node.getChildren()) {
			if (element instanceof Node) {
				Node childNode = (Node) element;
				
				readDocumentImpl(childNode);
			} else if (element instanceof Content) {
				Content content = (Content) element;
				
				for (DomEventListener listener : listeners) {
					if (listener.matchNode(node)) listener.contentOccurred(content);
				}
			}
		}
	}
	
}