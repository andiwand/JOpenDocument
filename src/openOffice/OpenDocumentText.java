package openOffice;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import xml.Attribute;
import xml.Document;
import xml.Node;
import xml.reader.SaxDocumentReader;


public class OpenDocumentText extends OpenDocumentFormat {
	
	public static final String MIME_TYPE = "application/vnd.oasis.opendocument.text";
	
	
	private int pageCount;
	
	
	public OpenDocumentText(InputStream inputStream) throws IOException {
		super(inputStream);
	}
	public OpenDocumentText(InputStream inputStream, int blockSize) throws IOException {
		super(inputStream, blockSize);
	}
	public OpenDocumentText(File file) throws FileNotFoundException, IOException {
		super(file);
	}
	public OpenDocumentText(File file, int blockSize) throws FileNotFoundException, IOException {
		super(file, blockSize);
	}
	public OpenDocumentText(String file) throws FileNotFoundException, IOException {
		super(file);
	}
	public OpenDocumentText(String file, int blockSize) throws FileNotFoundException, IOException {
		super(file, blockSize);
	}
	
	protected boolean checkMimeType(String mimeType) {
		return mimeType.equals(MIME_TYPE);
	}
	protected void loadMetadata(InputStream inputStream) throws IOException {
		try {
			SaxDocumentReader saxReader = new SaxDocumentReader(inputStream);
			
			Document document = saxReader.readDocument();
			Node metaNode = document.getRoot().findChildNode("meta");
			Node documentStatisticNode = metaNode.findChildNode("document-statistic");
			Attribute pageCountAttribute = documentStatisticNode.findAttribute("page-count");
			pageCount = Integer.parseInt(pageCountAttribute.getValue());
			
			saxReader.close();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		}
		
		inputStream.close();
	}
	
	
	public int getPageCount() {
		return pageCount;
	}
	
}