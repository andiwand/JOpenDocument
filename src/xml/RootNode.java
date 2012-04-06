package xml;

public class RootNode extends Node {
	
	public RootNode(String name) {
		super(name);
	}
	
	public RootNode(String namespace, String name) {
		super(namespace, name);
	}
	
	public RootNode(Node node) {
		super(node);
	}
	
	public RootNode(RootNode rootNode) {
		super(rootNode);
	}
	
	@Override
	public RootNode clone() {
		return new RootNode(this);
	}
	
	@Override
	public boolean setParent(Node newParent) {
		return false;
	}
	
}