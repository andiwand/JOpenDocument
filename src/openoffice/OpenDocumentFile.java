package openoffice;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import xml.Document;
import xml.reader.SaxDocumentReader;



public abstract class OpenDocumentFile {
	
	public static final String MIMETYPE_PATH = "mimetype";
	public static final String MANIFEST_PATH = "META-INF/manifest.xml";
	
	
	public abstract Set<String> getFiles() throws IOException;
	public abstract InputStream getRawFileStream(String path) throws IOException;
	public abstract InputStream getFileStream(String path) throws IOException;
	
	public String getMimeType() throws IOException {
		InputStream inputStream = getRawFileStream(MIMETYPE_PATH);
		if (inputStream == null) throw new MimeTypeNotFoundException();
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
		
		String mimeType = reader.readLine();
		
		reader.close();
		inputStream.close();
		
		return mimeType;
	}
	
	public Document getManifest() throws ParserConfigurationException, SAXException, IOException {
		InputStream inputStream = getRawFileStream(MANIFEST_PATH);
		SaxDocumentReader documentReader = new SaxDocumentReader(inputStream);
		
		return documentReader.readDocument();
	}
	
}