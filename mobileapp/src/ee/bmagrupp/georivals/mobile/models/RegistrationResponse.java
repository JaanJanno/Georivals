package ee.bmagrupp.georivals.mobile.models;

public class RegistrationResponse {

	private ServerResult result;
	private String value;
	private int id;

	public RegistrationResponse() {
		super();
	}

	public RegistrationResponse(ServerResult result, String value) {
		this.result = result;
		this.value = value;
	}

	public RegistrationResponse(ServerResult result, String value, int id) {
		this.result = result;
		this.value = value;
		this.id = id;
	}

	public RegistrationResponse(ServerResult result) {
		this.result = result;
	}

	public void setResult(ServerResult result) {
		this.result = result;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public ServerResult getResult() {
		return result;
	}

	public String getValue() {
		return value;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "RegistrationResponse [result=" + result + ", value=" + value
				+ "]";
	}

}
