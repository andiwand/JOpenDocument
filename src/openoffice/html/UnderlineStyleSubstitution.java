package openoffice.html;

import xml.Attribute;


public class UnderlineStyleSubstitution extends StyleSubstitution {
	
	public UnderlineStyleSubstitution() {
		super("text-underline-style", "text-decoration");
	}
	
	@Override
	public String translate(Attribute source) {
		if (!source.getName().equals("text-underline-style")) return null;
		
		String style = "text-decoration: ";
		
		if (source.getValue().equals("solid")) style += "underline";
		else style += "none";
		
		style += ";";
		
		return style;
	}
	
}