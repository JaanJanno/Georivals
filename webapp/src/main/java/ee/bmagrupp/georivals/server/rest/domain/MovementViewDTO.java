package ee.bmagrupp.georivals.server.rest.domain;

import java.util.Date;

public class MovementViewDTO {

	private final int movementId;
	private final int unitSize;
	private final String destinationName;
	private final boolean attack;
	private final Date endDate;

	public MovementViewDTO(int movementId, String destinationName,
			int unitSize, boolean attack, Date endDate) {
		this.movementId = movementId;
		this.unitSize = unitSize;
		this.destinationName = destinationName;
		this.attack = attack;
		this.endDate = endDate;
	}

	public int getUnitSize() {
		return unitSize;
	}

	public int getMovementId() {
		return movementId;
	}

	public String getDestinationName() {
		return destinationName;
	}

	public boolean isAttack() {
		return attack;
	}

	public Date getEndDate() {
		return endDate;
	}
}
