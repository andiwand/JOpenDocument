package xml;

import java.io.IOException;
import java.io.Writer;

public class Comment extends Element {
	
	private String comment;
	
	public Comment() {
		this("");
	}
	
	public Comment(String comment) {
		if (comment == null) throw new NullPointerException("comment is null");
		
		this.comment = comment;
	}
	
	public Comment(Comment comment) {
		this(comment.comment);
	}
	
	@Override
	public boolean equals(Object object) {
		if (object == null) return false;
		if (object == this) return true;
		if (!(object instanceof Comment)) return false;
		Comment comment = (Comment) object;
		
		return this.comment.equals(comment.comment);
	}
	
	@Override
	public Element clone() {
		return new Comment(this);
	}
	
	@Override
	public void write(Writer writer) throws IOException {
		writer.append("<!-- ");
		writer.append(comment);
		writer.append(" -->");
	}
	
	public String getComment() {
		return comment;
	}
	
	public void setComment(String comment) {
		this.comment = comment;
	}
	
}