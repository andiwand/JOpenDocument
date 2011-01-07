package test;

import java.io.File;

import openOffice.OpenDocumentText;
import openOffice.html.ClassAttributeTranslator;
import openOffice.html.ImageCache;
import openOffice.html.ImageTranslator;
import openOffice.html.NodeSubstitution;
import openOffice.html.StaticStyleSubstitution;
import openOffice.html.StyleNodeTranslator;
import openOffice.html.StyleSubstitution;
import openOffice.html.TableStyleNodeTranslator;
import openOffice.html.odt.HtmlPageOdt;
import openOffice.html.odt.TranslatorOdt;


public class TestTranslatorOdt {
	
	public static void main(String[] args) throws Throwable {
		File file = new File("/home/andreas/UE01.5_Protokoll.odt");
		File tmp = new File("/home/andreas/tmp");
		
		
		ImageCache imageCache = new ImageCache(tmp, false);
		
		OpenDocumentText documentText = new OpenDocumentText(file);
		TranslatorOdt translatorOdt = new TranslatorOdt(documentText);
		
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
		
		ImageTranslator imageTranslator = new ImageTranslator(documentText, imageCache);
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