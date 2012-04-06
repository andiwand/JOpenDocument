package xml;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;


public abstract class XmlObject {
	
	@Override
	public String toString() {
		try {
			StringWriter writer = new StringWriter();
			write(writer);
			return writer.toString();
		} catch (IOException e) {
			throw new IllegalStateException("Unreachable section");
		}
	}
	
	@Override
	public abstract boolean equals(Object object);
	
	@Override
	public abstract XmlObject clone();
	
	public abstract void write(Writer writer) throws IOException;
	
}