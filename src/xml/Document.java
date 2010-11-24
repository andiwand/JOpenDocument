package xml;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


public class Document extends XmlObject {
	
	final RootNode root;
	
	
	public Document(RootNode root) {
		if (root == null) throw new NullPointerException("root is null");
		
		this.root = root;
	}
	public Document(Document document) {
		root = document.root.clone();
	}
	
	
	public String toString() {
		return root.toString();
	}
	// TODO implement
	public boolean equals(Object object) {
		return false;
	}
	public Document clone() {
		return new Document(this);
	}
	
	
	public RootNode getRoot() {
		return root;
	}
	
	
	public void write(File file) throws IOException {
		FileWriter writer = new FileWriter(file);
		
		writer.append(toString());
		
		writer.close();
	}
	public void write(String file) throws IOException {
		write(new File(file));
	}
	
}