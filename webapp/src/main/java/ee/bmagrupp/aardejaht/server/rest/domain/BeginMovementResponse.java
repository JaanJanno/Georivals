package ee.bmagrupp.aardejaht.server.rest.domain;

import java.util.Date;

import ee.bmagrupp.aardejaht.server.util.ServerResult;

public class BeginMovementResponse {
	private Date arrivalTime;
	private ServerResult result;

	public BeginMovementResponse() {
	}

	public BeginMovementResponse(Date arrivalTime, ServerResult result) {
		this.arrivalTime = arrivalTime;
		this.result = result;
	}

	public Date getArrivalTime() {
		return arrivalTime;
	}

	public void setArrivalTime(Date arrivalTime) {
		this.arrivalTime = arrivalTime;
	}

	public ServerResult getResult() {
		return result;
	}

	public void setResult(ServerResult result) {
		this.result = result;
	}

}
