package openOffice.html;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import openOffice.OpenDocumentText;
import xml.Attribute;
import xml.Node;


public class ImageTranslator implements NodeTranslator {
	
	private OpenDocumentText documentText;
	
	
	public ImageTranslator(OpenDocumentText documentText) {
		this.documentText = documentText;
	}
	
	
	public Node translateNode(Node source) {
		try {
			File file = new File(source.findAttribute("href").getValue());
			
			InputStream inputStream = documentText.getElement(file.getPath());
			File tmpFile = new File(new File("/tmp"), file.getName());
			
			FileOutputStream outputStream = new FileOutputStream(tmpFile);
			
			byte[] buffer = new byte[512];
			int read = 0;
			
			while ((read = inputStream.read(buffer)) != -1) {
				outputStream.write(buffer, 0, read);
			}
			
			Node img = new Node("div");
			
			String style = "background-image: url(" + tmpFile.getAbsolutePath() + ");";
			
			Node sourceParent = source.getParent();
			Attribute width = sourceParent.findAttribute("width");
			Attribute height = sourceParent.findAttribute("height");
			
			if (width != null) style += "width: " + width.getValue() + ";";
			if (height != null) style += "height: " + height.getValue() + ";";
			
			img.addAttribute(new Attribute("style", style));
			
			return img;
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
}