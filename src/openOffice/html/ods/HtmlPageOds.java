package openOffice.html.ods;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import xml.Document;
import xml.RootNode;


public class HtmlPageOds {
	
	public final Document htmlDocument;
	
	
	public HtmlPageOds() {
		htmlDocument = new Document(new RootNode("html"));
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