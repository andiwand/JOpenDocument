package openoffice;

import java.io.IOException;


public class OpenDocumentText extends OpenDocument {
	
	public static final String MIMETYPE = "application/vnd.oasis.opendocument.text";
	
	
	public OpenDocumentText(OpenDocumentFile openDocumentFile) throws IOException {
		super(openDocumentFile);
	}
	
	@Override
	protected boolean checkMimeType(String mimeType) {
		return mimeType.startsWith(MIMETYPE);
	}
	
}