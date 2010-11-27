package openOffice.html;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import xml.Document;
import xml.RootNode;


public class HtmlPageOdt {
	
	public final int page;
	
	public final Document htmlDocument;
	
	
	public HtmlPageOdt(int page) {
		this.page = page;
		
		htmlDocument = new Document(new RootNode("html"));
	}
	
	
	public int getPage() {
		return page;
	}
	public Document getHtmlDocument() {
		return htmlDocument;
	}
	public RootNode getHtmlNode() {
		return htmlDocument.getRoot();
	}
	
	
	public void save(File file) throws IOException {
		FileWriter writer = new FileWriter(file);
		
		writer.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		writer.append(htmlDocument.toString());
		
		writer.flush();
		writer.close();
	}
	public void save(String file) throws IOException {
		save(new File(file));
	}
	
}