package openoffice;

import java.io.IOException;


public class OpenDocumentSpreadsheetTemplate extends OpenDocumentSpreadsheet {
	
	public static final String MIMETYPE = "application/vnd.oasis.opendocument.spreadsheet-template";
	
	
	public OpenDocumentSpreadsheetTemplate(OpenDocumentFile openDocumentFile) throws IOException {
		super(openDocumentFile);
	}
	
	@Override
	protected boolean checkMimeType(String mimeType) {
		return MIMETYPE.equals(mimeType);
	}
	
}