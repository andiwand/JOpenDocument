package openOffice.html;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;

import openOffice.OpenDocumentText;
import xml.Attribute;
import xml.Node;


public class ImageTranslator implements NodeTranslator {
	
	private OpenDocumentText documentText;
	
	private ImageCache imageCache;
	
	private URITranslator uriTranslator;
	
	
	public ImageTranslator(OpenDocumentText documentText, ImageCache imageCache) {
		this.documentText = documentText;
		
		this.imageCache = imageCache;
	}
	
	
	public URITranslator getUriTranslator() {
		return uriTranslator;
	}
	
	public void setUriTranslator(URITranslator uriTranslator) {
		this.uriTranslator = uriTranslator;
	}
	
	
	public Node translateNode(Node source) {
		try {
			File imageFile = new File(source.findAttribute("href").getValue());
			
			InputStream inputStream = documentText.getElement(imageFile.getPath());
			
			File tmpFile = imageCache.newImage(imageFile.getName());
			FileOutputStream outputStream = new FileOutputStream(tmpFile);
			
			pipeImage(inputStream, outputStream);
			
			
			URI tmpFileUri = tmpFile.getAbsoluteFile().toURI();
			if (uriTranslator != null) tmpFileUri = uriTranslator.translate(tmpFileUri);
			
			Node img = new Node("img");
			img.addAttribute(new Attribute("src", tmpFileUri.toString()));
			img.addAttribute(new Attribute("alt", tmpFile.getAbsolutePath()));
			
			String style = "";
			
			Node sourceParent = source.getParent();
			Attribute width = sourceParent.findAttribute("width");
			Attribute height = sourceParent.findAttribute("height");
			
			style += "width: " + width.getValue() + ";";
			style += "height: " + height.getValue() + ";";
			
			img.addAttribute(new Attribute("style", style));
			
			return img;
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	
	private static void pipeImage(InputStream inputStream, OutputStream outputStream) throws IOException {
		byte[] buffer = new byte[512];
		int read = 0;
		
		while ((read = inputStream.read(buffer)) != -1) {
			outputStream.write(buffer, 0, read);
		}
	}
	
}