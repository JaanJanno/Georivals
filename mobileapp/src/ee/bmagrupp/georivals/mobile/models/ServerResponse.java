package ee.bmagrupp.georivals.mobile.models;

public class ServerResponse {

	private ServerResult result;
	private String value;
	private int id;

	public ServerResponse() {
		super();
	}

	public ServerResponse(ServerResult result, String value) {
		this.result = result;
		this.value = value;
	}

	public ServerResponse(ServerResult result, String value, int id) {
		this.result = result;
		this.value = value;
		this.id = id;
	}

	public ServerResponse(ServerResult result) {
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
		return "ServerResponse [result=" + result + ", value=" + value
				+ ", id=" + id + "]";
	}

}
