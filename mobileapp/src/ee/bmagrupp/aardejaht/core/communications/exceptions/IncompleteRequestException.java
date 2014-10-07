package ee.bmagrupp.aardejaht.core.communications.exceptions;

public class IncompleteRequestException extends RuntimeException {

	private static final long serialVersionUID = -625572004745174940L;

	public IncompleteRequestException() {
		super();
	}

	public IncompleteRequestException(String detailMessage) {
		super(detailMessage);
	}

}
