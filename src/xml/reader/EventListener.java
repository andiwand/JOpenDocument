package xml.reader;

import xml.Node;


public interface EventListener {
	
	public abstract boolean matchNode(Node node);
	
}