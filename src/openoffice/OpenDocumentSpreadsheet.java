package openoffice;

import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import xml.Attribute;
import xml.Document;
import xml.Node;
import xml.reader.SaxDocumentReader;


public class OpenDocumentSpreadsheet extends OpenDocument {
	
	public static final String MIMETYPE = "application/vnd.oasis.opendocument.spreadsheet";
	
	private final int tableCount;
	
	public OpenDocumentSpreadsheet(OpenDocumentFile openDocumentFile)
			throws Exception {
		super(openDocumentFile);
		
		SaxDocumentReader metaReader = new SaxDocumentReader(getMeta());
		Document metaDocument = metaReader.readDocument();
		Node metaDocumentStatistics = metaDocument.getRoot().findChildNode(
				"meta").findChildNode("document-statistic");
		Attribute metaTableCount = metaDocumentStatistics
				.findAttribute("table-count");
		tableCount = Integer.valueOf(metaTableCount.getValue());
	}
	
	@Override
	protected boolean checkMimeType(String mimeType) {
		return mimeType.startsWith(MIMETYPE);
	}
	
	public int getTableCount() {
		return tableCount;
	}
	
	public List<String> getTableNames() throws Exception {
		SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
		TableNamesHandler handler = new TableNamesHandler();
		parser.parse(getContent(), handler);
		return handler.getTableNames();
	}
	
	private static class TableNamesHandler extends DefaultHandler {
		private List<String> tableNames = new ArrayList<String>();
		
		public List<String> getTableNames() {
			return tableNames;
		}
		
		public void startElement(String uri, String localName, String qName,
				Attributes attributes) throws SAXException {
			if (((qName != null) && (qName.length() == 0))
					&& (localName.length() != 0)) qName = localName;
			
			if (!qName.equals("table:table")) return;
			String name = attributes.getValue("table:name");
			if (name == null) tableNames.add("");
			else tableNames.add(name);
		}
	}
	
}