package openoffice;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Set;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.NoSuchPaddingException;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import util.ZipUtil;
import xml.Document;
import xml.Node;
import xml.RootNode;


public class CachedOpenDocumentFile extends OpenDocumentFile {
	
	public static final int DEFAULT_BUFFER_SIZE = 512;
	
	private byte[] buffer;
	private byte[] cache;
	
	public CachedOpenDocumentFile(InputStream inputStream) throws IOException {
		this(inputStream, DEFAULT_BUFFER_SIZE);
	}
	
	public CachedOpenDocumentFile(InputStream inputStream, int bufferSize)
			throws IOException {
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
	public InputStream getRawFileStream(String path) throws IOException {
		return ZipUtil.getEntry(getInputStream(), path);
	}
	
	@Override
	public InputStream getFileStream(String path) throws IOException {
		InputStream inputStream = getRawFileStream(path);
		
		try {
			Document manifestDocument = getManifest();
			Cipher cipher = getFileCipher(manifestDocument, path);
			
			if (cipher != null) {
				inputStream = new CipherInputStream(inputStream, cipher);
			}
		} catch (ParserConfigurationException e) {} catch (SAXException e) {} catch (NoSuchAlgorithmException e) {} catch (NoSuchPaddingException e) {} catch (InvalidKeyException e) {}
		
		return inputStream;
	}
	
	private Cipher getFileCipher(Document manifestDocument, String path)
			throws NoSuchAlgorithmException, NoSuchPaddingException,
			InvalidKeyException {
		RootNode manifestRoot = manifestDocument.getRoot();
		
		for (Node fileEntry : manifestRoot.getChildNodes()) {
			if (fileEntry.findAttribute("full-path").getValue().equals(path)) {
				Node encryptionData = fileEntry.findChildNode("encryption-data");
				
				if (encryptionData == null) {
					return null;
				} else {
					Node algorithmNode = encryptionData.findChildNode("algorithm");
					String[] algorithmName = algorithmNode.findAttribute(
							"algorithm-name").getValue().split(" ");
					String algorithm = algorithmName[0];
					String mode = algorithmName[1];
					String padding = "NoPadding";
					
					Cipher cipher = Cipher.getInstance(algorithm + "/" + mode
							+ "/" + padding);
					cipher.init(Cipher.DECRYPT_MODE, new SimpleKey("asdf",
							algorithm));
					return cipher;
				}
			}
		}
		
		return null;
	}
	
	private static class SimpleKey implements Key {
		private static final long serialVersionUID = 5072250635817534419L;
		
		private String key;
		private String algorithm;
		
		public SimpleKey(String key, String algorithm) {
			this.key = key;
			this.algorithm = algorithm;
		}
		
		public String getAlgorithm() {
			return algorithm;
		}
		
		public byte[] getEncoded() {
			return key.getBytes();
		}
		
		public String getFormat() {
			return null;
		}
	}
	
}