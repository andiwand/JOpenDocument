package openoffice;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import at.andiwand.library.io.StreamUtil;


public class TemporaryOpenDocumentFile extends OpenDocumentFile {
	
	private final Map<String, File> fileMap = new HashMap<String, File>();
	
	public TemporaryOpenDocumentFile(InputStream inputStream)
			throws IOException {
		ZipInputStream zipInputStream = new ZipInputStream(inputStream);
		
		ZipEntry entry;
		while ((entry = zipInputStream.getNextEntry()) != null) {
			if (entry.isDirectory()) continue;
			String path = entry.getName();
			
			String name = path.replaceAll("/", "_");
			File file = File.createTempFile(name, "");
			
			FileOutputStream outputStream = new FileOutputStream(file);
			StreamUtil.writeStream(zipInputStream, outputStream);
			outputStream.close();
			
			fileMap.put(path, file);
			
			zipInputStream.closeEntry();
		}
	}
	
	@Override
	public Set<String> getFiles() throws IOException {
		return fileMap.keySet();
	}
	
	@Override
	public InputStream getFileStream(String path) throws IOException {
		return new FileInputStream(fileMap.get(path));
	}
	
}