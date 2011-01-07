package test;

import java.io.File;

import openOffice.OpenDocumentSpreadsheet;
import openOffice.html.ClassAttributeTranslator;
import openOffice.html.NodeSubstitution;
import openOffice.html.StaticStyleSubstitution;
import openOffice.html.StyleNodeTranslator;
import openOffice.html.StyleSubstitution;
import openOffice.html.TableStyleNodeTranslator;
import openOffice.html.ods.HtmlPageOds;
import openOffice.html.ods.TranslatorOds;


public class TestTranslatorOds {
	
	public static void main(String[] args) throws Throwable {
		File file = new File("/home/andreas/test.ods");
		
		
		OpenDocumentSpreadsheet documentSpreadsheet = new OpenDocumentSpreadsheet(file);
		TranslatorOds translatorOds = new TranslatorOds(documentSpreadsheet);
		
		translatorOds.addStyleNodeTranslator("text-properties", new StyleNodeTranslator(
				new StyleSubstitution("font-size", "font-size"),
				new StyleSubstitution("font-weight", "font-weight"),
				new StyleSubstitution("font-style", "font-style"),
				new StaticStyleSubstitution("text-underline-style", "text-decoration", "underline")
		));
		translatorOds.addStyleNodeTranslator("table-properties", new TableStyleNodeTranslator(
				new StyleSubstitution("width", "width")
		));
		translatorOds.addStyleNodeTranslator("table-column-properties", new StyleNodeTranslator(
				new StyleSubstitution("column-width", "width")
		));
		translatorOds.addStyleNodeTranslator("table-cell-properties", new StyleNodeTranslator(
				new StyleSubstitution("padding", "padding"),
				new StyleSubstitution("border", "border"),
				new StyleSubstitution("border-top", "border-top"),
				new StyleSubstitution("border-right", "border-right"),
				new StyleSubstitution("border-bottom", "border-bottom"),
				new StyleSubstitution("border-left", "border-left")
		));
		
		translatorOds.addNodeSubstitution(new NodeSubstitution("p", "p"));
		translatorOds.addNodeSubstitution(new NodeSubstitution("h", "p"));
		translatorOds.addNodeSubstitution(new NodeSubstitution("table", "table"));
		translatorOds.addNodeSubstitution(new NodeSubstitution("table-row", "tr"));
		translatorOds.addNodeSubstitution(new NodeSubstitution("table-cell", "td"));
		translatorOds.addNodeSubstitution(new NodeSubstitution("frame", "span"));
		
		translatorOds.addAttributeTranslators("style-name", new ClassAttributeTranslator());
		
		HtmlPageOds pageOds = translatorOds.translate();
		
		String htmlFileName = file.getName();
		int lastDot = htmlFileName.lastIndexOf(".");
		if (lastDot != -1) htmlFileName = htmlFileName.substring(0, lastDot);
		htmlFileName += ".html";
		pageOds.save(new File(file.getParentFile(), htmlFileName));
	}
	
}