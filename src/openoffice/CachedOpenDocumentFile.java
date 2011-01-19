package openoffice;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Set;

import util.ZipUtil;


public class CachedOpenDocumentFile extends OpenDocumentFile {
	
	public static final int DEFAULT_BUFFER_SIZE = 512;
	
	
	private byte[] buffer;
	private byte[] cache;
	
	
	public CachedOpenDocumentFile(InputStream inputStream) throws IOException {
		this(inputStream, DEFAULT_BUFFER_SIZE);
	}
	public CachedOpenDocumentFile(InputStream inputStream, int bufferSize) throws IOException {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		buffer = new byte[bufferSize];
		int read;
		
		while ((read = inputStream.read(buffer)) != -1) {
			outputStream.write(buffer, 0, read);
		}
		
		buffer = null;
		outputStream.close();
		
		cache = outputStream.toByteArray();
	}
	
	
	public InputStream getInputStream() {
		return new ByteArrayInputStream(cache);
	}
	
	@Override
	public Set<String> getFiles() throws IOException {
		return ZipUtil.getEntries(getInputStream());
	}
	
	@Override
	public InputStream getFile(String path) throws IOException {
		return ZipUtil.getEntry(getInputStream(), path);
	}
	
}