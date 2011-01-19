package openoffice;

import java.io.IOException;
import java.io.InputStream;


public abstract class OpenDocument {
	
	public static final String CONTENT_PATH = "content.xml";
	
	
	private OpenDocumentFile openDocumentFile;
	
	
	public OpenDocument(OpenDocumentFile openDocumentFile) throws IOException {
		if (!checkMimeType(openDocumentFile.getMimeType()))
			throw new IllegalMimeTypeException();
		
		this.openDocumentFile = openDocumentFile;
	}
	
	protected abstract boolean checkMimeType(String mimeType);
	
	
	public OpenDocumentFile getOpenDocumentFile() {
		return openDocumentFile;
	}
	
	public InputStream getContent() throws IOException {
		return openDocumentFile.getFile(CONTENT_PATH);
	}
	
}