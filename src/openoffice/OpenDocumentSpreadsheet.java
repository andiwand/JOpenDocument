package openoffice;

import xml.Attribute;
import xml.Document;
import xml.Node;
import xml.reader.SaxDocumentReader;


public class OpenDocumentSpreadsheet extends OpenDocument {
	
	public static final String MIMETYPE = "application/vnd.oasis.opendocument.spreadsheet";
	
	private final int tableCount;
	
	
	public OpenDocumentSpreadsheet(OpenDocumentFile openDocumentFile) throws Exception {
		super(openDocumentFile);
		
		SaxDocumentReader metaReader = new SaxDocumentReader(getMeta());
		Document metaDocument = metaReader.readDocument();
		Node metaDocumentStatistics = metaDocument.getRoot().findChildNode("meta").findChildNode("document-statistic");
		Attribute metaTableCount = metaDocumentStatistics.findAttribute("table-count");
		tableCount = Integer.valueOf(metaTableCount.getValue());
	}
	
	
	@Override
	protected boolean checkMimeType(String mimeType) {
		return mimeType.startsWith(MIMETYPE);
	}
	
	public int getTableCount() {
		return tableCount;
	}
	
}