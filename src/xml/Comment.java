package xml;

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
	
	public String toString() {
		StringBuilder builder = new StringBuilder();
		
		builder.append("<!-- ");
		builder.append(comment);
		builder.append(" -->");
		
		return builder.toString();
	}
	
	public boolean equals(Object object) {
		if (object == null) return false;
		if (object == this) return true;
		if (!(object instanceof Comment)) return false;
		Comment comment = (Comment) object;
		
		return this.comment.equals(comment.comment);
	}
	
	public Element clone() {
		return new Comment(this);
	}
	
	public String getComment() {
		return comment;
	}
	
	public void setComment(String comment) {
		this.comment = comment;
	}
	
}