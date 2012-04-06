package xml;

public abstract class Element extends XmlObject {
	
	Node parent;
	
	public Element() {}
	
	@Override
	public abstract boolean equals(Object object);
	
	@Override
	public abstract Element clone();
	
	public boolean hasParent() {
		return parent != null;
	}
	
	public Node getParent() {
		return parent;
	}
	
	public boolean setParent(Node newParent) {
		if (parent != null) parent.children.remove(this);
		parent = newParent;
		
		if (newParent == null) return true;
		newParent.children.add(this);
		
		return true;
	}
	
}