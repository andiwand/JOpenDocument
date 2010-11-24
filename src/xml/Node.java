package xml;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;


public class Node extends Element {
	
	List<Element> children;
	
	private String namespace;
	private String name;
	List<Attribute> attributes;
	
	
	public Node(String qName) {
		if (qName == null)
			throw new NullPointerException("The argument 'qName' must not be null!");
		
		this.children = new ArrayList<Element>();
		
		String[] parts = qName.split(":");
		
		if (parts.length <= 1) {
			this.name = parts[0];
		} else {
			this.namespace = parts[0];
			this.name = parts[1];
		}
		
		this.attributes = new ArrayList<Attribute>();
	}
	public Node(String namespace, String name) {
		if (name == null)
			throw new NullPointerException("The argument 'name' must not be null!");
		
		this.children = new ArrayList<Element>();
		
		this.namespace = namespace;
		this.name = name;
		this.attributes = new ArrayList<Attribute>();
	}
	public Node(Node node) {
		this(node.namespace, node.name);
		
		for (Element child : children) {
			addChild(child.clone());
		}
		
		for (Attribute attribute : attributes) {
			addAttribute(new Attribute(attribute));
		}
	}
	
	
	public String toString() {
		StringBuilder builder = new StringBuilder();
		
		builder.append("<");
		
		if (namespace != null) {
			builder.append(namespace);
			builder.append(":");
		}
		
		builder.append(name);
		
		for (Attribute attribute : attributes) {
			builder.append(" ");
			builder.append(attribute);
		}
		
		builder.append(">");
		
		
		for (Element child : children) {
			builder.append(child.toString());
		}
		
		
		builder.append("</");
		
		if (namespace != null) {
			builder.append(namespace);
			builder.append(":");
		}
		
		builder.append(name);
		builder.append(">");
		
		return builder.toString();
	}
	public boolean equals(Object object) {
		if (object == null) return false;
		if (object == this) return true;
		if (!(object instanceof Node)) return false;
		Node node = (Node) object;
		
		return name.equals(node.name);
	}
	public Element clone() {
		return new Node(this);
	}
	
	
	public boolean hasAttribute(String name) {
		for (Attribute attribute : attributes) {
			if (attribute.getName().equals(name)) return true;
		}
		
		return false;
	}
	
	public List<Element> getChildren() {
		return Collections.unmodifiableList(children);
	}
	public List<Node> getChildNodes() {
		List<Node> result = new ArrayList<Node>();
		
		for (Element child : children) {
			if (child instanceof Node) result.add((Node) child);
		}
		
		return result;
	}
	public List<Content> getChildContent() {
		List<Content> result = new ArrayList<Content>();
		
		for (Element child : children) {
			if (child instanceof Content) result.add((Content) child);
		}
		
		return result;
	}
	public String getContent() {
		StringBuilder builder = new StringBuilder();
		
		for (Element child : children) {
			if (child instanceof Content) builder.append(((Content) child).getContent());
		}
		
		return builder.toString();
	}
	
	public boolean hasNamespace() {
		return namespace != null;
	}
	public String getNamespace() {
		return namespace;
	}
	public String getName() {
		return name;
	}
	public String getQName() {
		return namespace + ":" + name;
	}
	public List<Attribute> getAttributes() {
		return Collections.unmodifiableList(attributes);
	}
	
	
	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}
	public void setName(String name) {
		if (name == null)
			throw new NullPointerException("The argument 'name' must not be null!");
		
		this.name = name;
	}
	public void getQName(String qName) {
		if (qName == null)
			throw new NullPointerException("The argument 'qName' must not be null!");
		
		String[] parts = qName.split(":");
		
		if (parts.length <= 0) {
			this.name = parts[0];
		} else {
			this.namespace = parts[0];
			this.name = parts[1];
		}
	}
	
	
	public boolean addChild(Element child) {
		if (child == this) return false;
		if (child.parent != null) return false;
		if (child instanceof RootNode) return false;
		
		child.parent = this;
		children.add(child);
		
		return true;
	}
	public boolean removeChild(Element child) {
		if (child == this) return false;
		if (parent != this) return false;
		
		child.parent = null;
		children.remove(child);
		
		return true;
	}
	
	public boolean addAttribute(Attribute attribute) {
		if (attribute.node != null) return false;
		if (attributes.contains(attribute)) return false;
		
		attribute.node = this;
		attributes.add(attribute);
		
		return true;
	}
	public boolean addAttributes(Collection<Attribute> attributes) {
		boolean result = false;
		
		for (Attribute attribute : attributes) {
			result |= addAttribute(attribute);
		}
		
		return result;
	}
	public boolean removeAttribute(Attribute attribute) {
		if (attribute.node != this) return false;
		
		attribute.node = null;
		attributes.remove(attribute);
		
		return true;
	}
	
	
	public void clearAttributes() {
		attributes.clear();
	}
	
	
	public Node findChildNode(String name) {
		Node result = null;
		
		for (Element child : children) {
			if (child instanceof Node) {
				Node node = (Node) child;
				
				if (node.getName().equals(name)) {
					result = node;
					break;
				}
			}
		}
		
		return result;
	}
	public Attribute findAttribute(String name) {
		Attribute result = null;
		
		for (Attribute attribute : attributes) {
			if (attribute.getName().equals(name)) {
				result = attribute;
				break;
			}
		}
		
		return result;
	}
	
}