package openoffice.html;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import xml.Attribute;


public class StyleSizeSubstitution extends StyleSubstitution {
	
	public static final double DEFAULT_SMALER_THAN = 0.01;
	public static final Pattern SIZE_PATTERN = Pattern.compile("(\\d+(\\.\\d+)?)(cm|in)");
	
	private double smalerThan;
	
	public StyleSizeSubstitution(String source, String destination) {
		this(source, destination, DEFAULT_SMALER_THAN);
	}
	
	public StyleSizeSubstitution(String source, String destination,
			double smalerThan) {
		super(source, destination);
		
		this.smalerThan = smalerThan;
	}
	
	@Override
	public String translate(Attribute source) {
		String result = super.translate(source);
		
		Matcher matcher = SIZE_PATTERN.matcher(result);
		if (matcher.find()) {
			double size = Double.valueOf(matcher.group(1));
			
			if (size < smalerThan) result = result.substring(0, matcher.start())
					+ "1px" + result.substring(matcher.end());
		}
		
		return result;
	}
	
}