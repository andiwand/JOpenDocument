package openoffice;


public class MimeTypeNotFoundException extends RuntimeException {
	
	private static final long serialVersionUID = -4197233829159850894L;
	
	
	public MimeTypeNotFoundException() {
		super();
	}
	public MimeTypeNotFoundException(String message) {
		super(message);
	}
	
}