package openOffice;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;


public class OpenDocumentSpreadsheet extends OpenDocumentFormat {
	
	public static final String MIME_TYPE = "application/vnd.oasis.opendocument.spreadsheet";
	
	
	public OpenDocumentSpreadsheet(InputStream inputStream) throws IOException {
		super(inputStream);
	}
	public OpenDocumentSpreadsheet(InputStream inputStream, int blockSize) throws IOException {
		super(inputStream, blockSize);
	}
	public OpenDocumentSpreadsheet(File file) throws FileNotFoundException, IOException {
		super(file);
	}
	public OpenDocumentSpreadsheet(File file, int blockSize) throws FileNotFoundException, IOException {
		super(file, blockSize);
	}
	public OpenDocumentSpreadsheet(String file) throws FileNotFoundException, IOException {
		super(file);
	}
	public OpenDocumentSpreadsheet(String file, int blockSize) throws FileNotFoundException, IOException {
		super(file, blockSize);
	}
	
	protected boolean checkMimeType(String mimeType) {
		return mimeType.equals(MIME_TYPE);
	}
	
}