package xml.reader;

import xml.Content;
import xml.Node;


public interface DomEventListener extends EventListener {
	
	public abstract void nodeOccurred(Node node);
	
	public abstract void contentOccurred(Content content);
	
}