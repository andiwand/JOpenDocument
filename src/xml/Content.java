package xml;


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
	
	
	public String toString() {
		return content;
	}
	public boolean equals(Object object) {
		if (object == null) return false;
		if (object == this) return true;
		if (!(object instanceof Content)) return false;
		Content content = (Content) object;
		
		return this.content.equals(content.content);
	}
	public Element clone() {
		return new Content(this);
	}
	
	
	public String getContent() {
		return content;
	}
	
	
	public void setContent(String content) {
		this.content = content;
	}
	
}