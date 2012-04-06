package xml;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;


public class Document extends XmlObject {
	
	final RootNode root;
	
	public Document(RootNode root) {
		if (root == null) throw new NullPointerException("root is null");
		
		this.root = root;
	}
	
	public Document(Document document) {
		root = document.root.clone();
	}
	
	// TODO implement
	@Override
	public boolean equals(Object object) {
		return false;
	}
	
	@Override
	public Document clone() {
		return new Document(this);
	}
	
	@Override
	public void write(Writer writer) throws IOException {
		root.write(writer);
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