package xml;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;


// TODO error handling
// TODO using setter in constructor
public class Attribute extends XmlObject {
	
	public static List<Attribute> convert(Attributes attributes) {
		List<Attribute> result = new ArrayList<Attribute>();
		
		for (int i = 0; i < attributes.getLength(); i++) {
			Attribute attribute = new Attribute(attributes.getQName(i));
			attribute.setValue(attributes.getValue(i));
			
			result.add(attribute);
		}
		
		return result;
	}
	
	Node node;
	
	private String namespace;
	private String name;
	private String value;
	
	public Attribute(String qName) {
		if (qName == null) throw new NullPointerException("qName is null");
		
		this.node = null;
		
		String[] parts = qName.split(":");
		
		if (parts.length <= 1) {
			this.name = parts[0];
		} else {
			this.namespace = parts[0];
			this.name = parts[1];
		}
		
		this.value = "";
	}
	
	public Attribute(String qName, String value) {
		this(qName);
		
		this.value = value;
	}
	
	public Attribute(Attribute attribute) {
		this(attribute.getQName(), attribute.value);
	}
	
	public String toString() {
		StringBuilder builder = new StringBuilder();
		
		if (namespace != null) {
			builder.append(namespace);
			builder.append(":");
		}
		
		builder.append(name);
		builder.append("=\"");
		builder.append(value);
		builder.append("\"");
		
		return builder.toString();
	}
	
	public boolean equals(Object object) {
		if (object == null) return false;
		if (object == this) return true;
		if (!(object instanceof Attribute)) return false;
		Attribute attribute = (Attribute) object;
		
		return name.equals(attribute.name) && value.equals(attribute.value);
	}
	
	public Attribute clone() {
		return new Attribute(this);
	}
	
	public Node getNode() {
		return node;
	}
	
	public String getNamespace() {
		return namespace;
	}
	
	public String getName() {
		return name;
	}
	
	public String getQName() {
		if (namespace == null) return name;
		else return namespace + ":" + name;
	}
	
	public String getValue() {
		return value;
	}
	
	public void setNode(Node newNode) {
		if (node != null) node.attributes.remove(this);
		if (newNode == null) return;
		
		newNode.attributes.add(this);
		node = newNode;
		
		return;
	}
	
	public void setNamespace(String namespace) {
		if (namespace.length() == 0) throw new IllegalArgumentException(
				"namespace is empty");
		
		this.namespace = namespace;
	}
	
	public void setName(String name) {
		if (name == null) throw new NullPointerException("name is null");
		if (name.length() == 0) throw new IllegalArgumentException(
				"name is empty");
		
		this.name = name;
	}
	
	public void setQName(String qName) {
		if (qName == null) throw new NullPointerException("qName is null");
		
		String[] parts = qName.split(":");
		
		if (parts.length <= 0) {
			this.name = parts[0];
		} else {
			this.namespace = parts[0];
			this.name = parts[1];
		}
	}
	
	public void setValue(String value) {
		if (value == null) throw new NullPointerException("value is null");
		
		this.value = value;
	}
	
}