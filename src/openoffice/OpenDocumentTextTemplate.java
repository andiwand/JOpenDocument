package openoffice;

import java.io.IOException;


public class OpenDocumentTextTemplate extends OpenDocumentText {
	
	public static final String MIMETYPE = "application/vnd.oasis.opendocument.text-template";
	
	public OpenDocumentTextTemplate(OpenDocumentFile openDocumentFile)
			throws IOException {
		super(openDocumentFile);
	}
	
	@Override
	protected boolean checkMimeType(String mimeType) {
		return MIMETYPE.equals(mimeType);
	}
	
}