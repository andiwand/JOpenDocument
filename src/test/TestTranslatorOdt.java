package test;

import java.io.File;
import java.io.FileInputStream;

import javax.swing.JFileChooser;

import openoffice.CachedOpenDocumentFile;
import openoffice.OpenDocumentText;
import openoffice.html.HtmlDocument;
import openoffice.html.ImageCache;
import openoffice.html.ImageTranslator;
import openoffice.html.odt.TranslatorOdt;


public class TestTranslatorOdt {
	
	public static void main(String[] args) throws Throwable {
		JFileChooser fileChooser = new JFileChooser();
		int option = fileChooser.showOpenDialog(null);
		
		if (option == JFileChooser.CANCEL_OPTION) return;
		
		File file = fileChooser.getSelectedFile();
		File tmp = new File("/home/andreas/tmp");
		
		
		ImageCache imageCache = new ImageCache(tmp, false);
		
		FileInputStream inputStream = new FileInputStream(file);
		CachedOpenDocumentFile documentFile = new CachedOpenDocumentFile(inputStream);
		OpenDocumentText text = new OpenDocumentText(documentFile);
		TranslatorOdt translatorOdt = new TranslatorOdt(text);
		
		ImageTranslator imageTranslator = new ImageTranslator(text, imageCache);
		//imageTranslator.setUriTranslator(new AndroidImageUriTranslator());
		translatorOdt.addNodeTranslator("image", imageTranslator);
		
		HtmlDocument pageOdt = translatorOdt.translate();
		
		String htmlFileName = file.getName();
		int lastDot = htmlFileName.lastIndexOf(".");
		if (lastDot != -1) htmlFileName = htmlFileName.substring(0, lastDot);
		htmlFileName += ".html";
		pageOdt.save(new File(file.getParentFile(), htmlFileName));
	}
	
}