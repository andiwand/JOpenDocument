package openoffice;

import java.io.IOException;
import java.io.InputStream;


public abstract class OpenDocument {
	
	public static final String META_PATH = "meta.xml";
	public static final String STYLE_PATH = "styles.xml";
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
	
	public InputStream getMeta() throws IOException {
		return openDocumentFile.getFileStream(META_PATH);
	}
	public InputStream getStyles() throws IOException {
		return openDocumentFile.getFileStream(STYLE_PATH);
	}
	public InputStream getContent() throws IOException {
		return openDocumentFile.getFileStream(CONTENT_PATH);
	}
	
}