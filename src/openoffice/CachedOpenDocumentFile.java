package openoffice;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Set;

import util.ZipUtil;
import at.andiwand.library.io.StreamUtil;


public class CachedOpenDocumentFile extends OpenDocumentFile {
	
	private byte[] cache;
	
	public CachedOpenDocumentFile(InputStream inputStream) throws IOException {
		cache = StreamUtil.readBytes(inputStream);
	}
	
	public InputStream getInputStream() {
		return new ByteArrayInputStream(cache);
	}
	
	@Override
	public Set<String> getFiles() throws IOException {
		return ZipUtil.getEntries(getInputStream());
	}
	
	@Override
	public InputStream getFileStream(String path) throws IOException {
		return ZipUtil.getEntry(getInputStream(), path);
	}
	
}