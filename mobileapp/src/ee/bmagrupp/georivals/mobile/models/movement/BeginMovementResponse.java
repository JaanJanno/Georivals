package ee.bmagrupp.georivals.mobile.models.movement;

import java.util.Date;

import ee.bmagrupp.georivals.mobile.models.ServerResult;

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

	@Override
	public String toString() {
		return "BeginMovementResponse [arrivalTime=" + arrivalTime
				+ ", result=" + result + "]";
	}

}