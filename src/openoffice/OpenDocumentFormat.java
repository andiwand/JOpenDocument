package openoffice;


import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;


// TODO optimize
// TODO close zip input streams
public class OpenDocumentFormat {
	
	private static final int DEFAULT_BLOCK_SIZE = 512;
	
	private static final String META_FILE = "meta.xml";
	private static final String MIME_TYPE_FILE = "mimetype";
	private static final String CONTENT_FILE = "content.xml";
	
	private static final String MIME_TYPE_PREFIX = "application/vnd.oasis.opendocument";
	
	
	private byte[] buffer;
	private byte[] file;
	
	private final String mimeType;
	
	private boolean closed;
	
	
	public OpenDocumentFormat(InputStream inputStream) throws IOException {
		this(inputStream, DEFAULT_BLOCK_SIZE);
	}
	public OpenDocumentFormat(InputStream inputStream, int blockSize) throws IOException {
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		buffer = new byte[blockSize];
		
		int read;
		while ((read = inputStream.read(buffer)) > 0) {
			byteArrayOutputStream.write(buffer, 0, read);
		}
		
		buffer = null;
		byteArrayOutputStream.close();
		file = byteArrayOutputStream.toByteArray();
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(getElement(MIME_TYPE_FILE)));
		mimeType = reader.readLine();
		reader.close();
		
		if (!checkMimeType(mimeType)) throw new IllegalArgumentException("incorrect mime type");
		
		loadMetadata(getElement(META_FILE));
	}
	public OpenDocumentFormat(File file) throws FileNotFoundException, IOException {
		this(file, DEFAULT_BLOCK_SIZE);
	}
	public OpenDocumentFormat(File file, int blockSize) throws FileNotFoundException, IOException {
		this(new FileInputStream(file), blockSize);
	}
	public OpenDocumentFormat(String file) throws FileNotFoundException, IOException {
		this(new File(file));
	}
	public OpenDocumentFormat(String file, int blockSize) throws FileNotFoundException, IOException {
		this(new File(file), blockSize);
	}
	
	protected boolean checkMimeType(String mimeType) {
		return mimeType.startsWith(MIME_TYPE_PREFIX);
	}
	protected void loadMetadata(InputStream inputStream) throws IOException {
		inputStream.close();
	}
	
	
	public InputStream getInputStream() {
		return new ByteArrayInputStream(file);
	}
	public List<String> getElements() {
		ArrayList<String> elements = new ArrayList<String>();
		
		try {
			ZipInputStream zipInputStream = new ZipInputStream(getInputStream());
			
			ZipEntry entry = zipInputStream.getNextEntry();
			while (entry != null) {
				elements.add(entry.getName());
				
				zipInputStream.closeEntry();
				entry = zipInputStream.getNextEntry();
			}
			
			zipInputStream.close();
		} catch (IOException e) {}
		
		return elements;
	}
	public InputStream getElement(String name) {
		ZipInputStream zipInputStream = new ZipInputStream(getInputStream());
		ZipEntry element = null;
		
		try {
			ZipEntry entry = zipInputStream.getNextEntry();
			while (entry != null) {
				if (entry.getName().equals(name)) {
					element = entry;
					break;
				}
				
				zipInputStream.closeEntry();
				entry = zipInputStream.getNextEntry();
			}
		} catch (IOException e) {}
		
		if (element == null) return null;
		
		return zipInputStream;
	}
	public InputStream getContent() {
		return getElement(CONTENT_FILE);
	}
	public String getMimeType() {
		return mimeType;
	}
	public boolean isClosed() {
		return closed;
	}
	
	public void close() {
		file = null;
		closed = true;
	}
	
}