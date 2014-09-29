package ee.bmagrupp.aardejaht.core.communications.exceptions;

public class DoubleRequestException extends RuntimeException {

	private static final long serialVersionUID = 5880905737517071378L;

	public DoubleRequestException() {
		super();
	}

	public DoubleRequestException(String detailMessage) {
		super(detailMessage);
	}

}
