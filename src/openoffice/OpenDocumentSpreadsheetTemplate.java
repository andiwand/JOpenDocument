package openoffice;



public class OpenDocumentSpreadsheetTemplate extends OpenDocumentSpreadsheet {
	
	public static final String MIMETYPE = "application/vnd.oasis.opendocument.spreadsheet-template";
	
	
	public OpenDocumentSpreadsheetTemplate(OpenDocumentFile openDocumentFile) throws Exception {
		super(openDocumentFile);
	}
	
	@Override
	protected boolean checkMimeType(String mimeType) {
		return MIMETYPE.equals(mimeType);
	}
	
}