package test;

import java.io.File;
import java.io.FileInputStream;

import javax.swing.JFileChooser;

import openoffice.CachedOpenDocumentFile;
import openoffice.OpenDocumentSpreadsheet;
import openoffice.html.AttributeSubstitution;
import openoffice.html.ClassAttributeTranslator;
import openoffice.html.NodeSubstitution;
import openoffice.html.StaticStyleSubstitution;
import openoffice.html.StyleNodeTranslator;
import openoffice.html.StyleSubstitution;
import openoffice.html.TableStyleNodeTranslator;
import openoffice.html.ods.HtmlPageOds;
import openoffice.html.ods.TranslatorOds;


public class TestTranslatorOds {
	
	public static void main(String[] args) throws Throwable {
		JFileChooser fileChooser = new JFileChooser();
		int option = fileChooser.showOpenDialog(null);
		
		if (option == JFileChooser.CANCEL_OPTION) return;
		
		File file = fileChooser.getSelectedFile();
		
		
		FileInputStream inputStream = new FileInputStream(file);
		CachedOpenDocumentFile documentFile = new CachedOpenDocumentFile(inputStream);
		OpenDocumentSpreadsheet spreadsheet = new OpenDocumentSpreadsheet(documentFile);
		TranslatorOds translatorOds = new TranslatorOds(spreadsheet);
		
		translatorOds.addStyleNodeTranslator("text-properties", new StyleNodeTranslator(
				new StyleSubstitution("font-size", "font-size"),
				new StyleSubstitution("font-weight", "font-weight"),
				new StyleSubstitution("font-style", "font-style"),
				new StaticStyleSubstitution("text-underline-style", "text-decoration", "underline")
		));
		translatorOds.addStyleNodeTranslator("table-properties", new TableStyleNodeTranslator(
				new StyleSubstitution("width", "width")
		));
		translatorOds.addStyleNodeTranslator("table-row-properties", new StyleNodeTranslator(
				new StyleSubstitution("row-height", "height")
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
		translatorOds.addNodeSubstitution(new NodeSubstitution("table-row", "tr",
				new AttributeSubstitution("number-rows-repeated", "rowspan")
		));
		translatorOds.addNodeSubstitution(new NodeSubstitution("table-cell", "td",
				new AttributeSubstitution("number-rows-repeated", "rowspan"),
				new AttributeSubstitution("number-columns-repeated", "colspan")
		));
		translatorOds.addNodeSubstitution(new NodeSubstitution("table-column", "colgroup",
				new AttributeSubstitution("number-columns-repeated", "span")
		));
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