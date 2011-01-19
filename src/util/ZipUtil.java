package util;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;


public class ZipUtil {
	
	private ZipUtil() {}
	
	
	public static Set<String> getEntries(InputStream inputStream) throws IOException {
		HashSet<String> pathSet = new HashSet<String>();
		
		ZipInputStream zipInputStream = new ZipInputStream(inputStream);
		
		ZipEntry entry = zipInputStream.getNextEntry();
		while (entry != null) {
			pathSet.add(entry.getName());
			
			zipInputStream.closeEntry();
			entry = zipInputStream.getNextEntry();
		}
		
		zipInputStream.close();
		
		return pathSet;
	}
	
	public static InputStream getEntry(InputStream inputStream, String path) throws IOException {
		ZipInputStream zipInputStream = new ZipInputStream(inputStream);
		
		ZipEntry entry = zipInputStream.getNextEntry();
		while (entry != null) {
			if (entry.getName().equals(path))
				return zipInputStream;
			
			zipInputStream.closeEntry();
			entry = zipInputStream.getNextEntry();
		}
		
		return null;
	}
	
}