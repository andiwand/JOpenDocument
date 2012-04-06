package openoffice;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Set;

import util.ZipUtil;


public class LocatedOpenDocumentFile extends OpenDocumentFile {
	
	private final File file;
	
	public LocatedOpenDocumentFile(File file) {
		this.file = file;
	}
	
	public File getFile() {
		return file;
	}
	
	public InputStream getInputStream() throws FileNotFoundException {
		return new FileInputStream(file);
	}
	
	@Override
	public Set<String> getFiles() throws IOException {
		InputStream inputStream = getInputStream();
		Set<String> result = ZipUtil.getEntries(inputStream);
		inputStream.close();
		return result;
	}
	
	@Override
	public InputStream getFileStream(String path) throws IOException {
		return ZipUtil.getEntry(getInputStream(), path);
	}
	
}