package xml;

import java.io.IOException;
import java.io.Writer;

public class Content extends Element {
	
	private String content;
	
	public Content() {
		this("");
	}
	
	public Content(String content) {
		this.content = content;
	}
	
	public Content(Content content) {
		this(content.content);
	}
	
	@Override
	public String toString() {
		return content;
	}
	
	@Override
	public boolean equals(Object object) {
		if (object == null) return false;
		if (object == this) return true;
		if (!(object instanceof Content)) return false;
		Content content = (Content) object;
		
		return this.content.equals(content.content);
	}
	
	@Override
	public Element clone() {
		return new Content(this);
	}
	
	@Override
	public void write(Writer writer) throws IOException {
		writer.append(content);
	}
	
	public String getContent() {
		return content;
	}
	
	public void setContent(String content) {
		this.content = content;
	}
	
}