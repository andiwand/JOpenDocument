package xml.reader;

import xml.Document;


public abstract class XmlDocumentReader extends XmlReader {
	
	public abstract Document readDocument() throws Exception;
	
}