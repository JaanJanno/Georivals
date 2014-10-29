package ee.bmagrupp.aardejaht.server.rest.domain;

import ee.bmagrupp.aardejaht.server.util.ServerResult;

public class ServerResponse {

	private ServerResult result;
	private String value;

	public ServerResponse() {
		super();
	}

	public ServerResponse(ServerResult result, String value) {
		this.result = result;
		this.value = value;
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

	@Override
	public String toString() {
		return "ServerResponse [result=" + result + ", value=" + value + "]";
	}

	public String toJson() {
		return "{\"result\":" + result + ",\"value\":\"" + value + "\"}";
	}
}
