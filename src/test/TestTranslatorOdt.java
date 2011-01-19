package test;

import java.io.File;
import java.io.FileInputStream;

import javax.swing.JFileChooser;

import openoffice.CachedOpenDocumentFile;
import openoffice.OpenDocumentText;
import openoffice.html.ClassAttributeTranslator;
import openoffice.html.ImageCache;
import openoffice.html.ImageTranslator;
import openoffice.html.NodeSubstitution;
import openoffice.html.StaticStyleSubstitution;
import openoffice.html.StyleNodeTranslator;
import openoffice.html.StyleSubstitution;
import openoffice.html.TableStyleNodeTranslator;
import openoffice.html.odt.HtmlPageOdt;
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
		
		translatorOdt.addStyleNodeTranslator("text-properties", new StyleNodeTranslator(
				new StyleSubstitution("font-size", "font-size"),
				new StyleSubstitution("font-weight", "font-weight"),
				new StyleSubstitution("font-style", "font-style"),
				new StaticStyleSubstitution("text-underline-style", "text-decoration", "underline")
		));
		translatorOdt.addStyleNodeTranslator("table-properties", new TableStyleNodeTranslator(
				new StyleSubstitution("width", "width")
		));
		translatorOdt.addStyleNodeTranslator("table-column-properties", new StyleNodeTranslator(
				new StyleSubstitution("column-width", "width")
		));
		translatorOdt.addStyleNodeTranslator("table-cell-properties", new StyleNodeTranslator(
				new StyleSubstitution("padding", "padding"),
				new StyleSubstitution("border", "border"),
				new StyleSubstitution("border-top", "border-top"),
				new StyleSubstitution("border-right", "border-right"),
				new StyleSubstitution("border-bottom", "border-bottom"),
				new StyleSubstitution("border-left", "border-left")
		));
		
		translatorOdt.addNodeSubstitution(new NodeSubstitution("p", "p"));
		translatorOdt.addNodeSubstitution(new NodeSubstitution("h", "p"));
		translatorOdt.addNodeSubstitution(new NodeSubstitution("table", "table"));
		translatorOdt.addNodeSubstitution(new NodeSubstitution("table-row", "tr"));
		translatorOdt.addNodeSubstitution(new NodeSubstitution("table-cell", "td"));
		translatorOdt.addNodeSubstitution(new NodeSubstitution("frame", "span"));
		
		ImageTranslator imageTranslator = new ImageTranslator(text, imageCache);
		//imageTranslator.setUriTranslator(new AndroidImageUriTranslator());
		translatorOdt.addNodeTranslator("image", imageTranslator);
		
		translatorOdt.addAttributeTranslators("style-name", new ClassAttributeTranslator());
		
		HtmlPageOdt pageOdt = translatorOdt.translate(0);
		
		String htmlFileName = file.getName();
		int lastDot = htmlFileName.lastIndexOf(".");
		if (lastDot != -1) htmlFileName = htmlFileName.substring(0, lastDot);
		htmlFileName += ".html";
		pageOdt.save(new File(file.getParentFile(), htmlFileName));
	}
	
}