package openoffice;

import java.io.IOException;


public class OpenDocumentSpreadsheet extends OpenDocument {
	
	public static final String MIMETYPE = "application/vnd.oasis.opendocument.spreadsheet";
	
	
	public OpenDocumentSpreadsheet(OpenDocumentFile openDocumentFile) throws IOException {
		super(openDocumentFile);
	}
	
	@Override
	protected boolean checkMimeType(String mimeType) {
		return mimeType.startsWith(MIMETYPE);
	}
	
}