package openoffice;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Set;


public abstract class OpenDocumentFile {
	
	public static final String MIMETYPE_PATH = "mimetype";
	
	
	public abstract Set<String> getFiles() throws IOException;
	public abstract InputStream getFile(String path) throws IOException;
	
	public String getMimeType() throws IOException {
		InputStream inputStream = getFile(MIMETYPE_PATH);
		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
		
		String mimeType = reader.readLine();
		
		reader.close();
		inputStream.close();
		
		return mimeType;
	}
	
}