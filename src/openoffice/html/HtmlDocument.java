package openoffice.html;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import xml.Document;
import xml.RootNode;


public class HtmlDocument {
	
	private static final String PREFIX = "htmldocument";
	
	public final Document document;
	
	public HtmlDocument() {
		document = new Document(new RootNode("html"));
	}
	
	public Document getHtmlDocument() {
		return document;
	}
	
	public RootNode getHtmlNode() {
		return document.getRoot();
	}
	
	public void save(File file) throws IOException {
		FileWriter writer = new FileWriter(file);
		
		writer.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		document.write(writer);
		
		writer.flush();
		writer.close();
	}
	
	public void save(String file) throws IOException {
		save(new File(file));
	}
	
	public File save() throws IOException {
		File file = File.createTempFile(PREFIX, "");
		save(file);
		return file;
	}
	
}