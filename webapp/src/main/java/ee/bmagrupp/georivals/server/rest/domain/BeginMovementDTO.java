package ee.bmagrupp.georivals.server.rest.domain;

/**
 * For making units move. Sent from MovementSelectionView.
 * 
 * @author TKasekamp
 *
 */
public class BeginMovementDTO {
	private int unitId;
	private int unitSize;

	public BeginMovementDTO() {
	}

	public BeginMovementDTO(int unitId, int unitSize) {
		this.unitId = unitId;
		this.unitSize = unitSize;
	}

	public int getUnitId() {
		return unitId;
	}

	public void setUnitId(int unitId) {
		this.unitId = unitId;
	}

	public int getUnitSize() {
		return unitSize;
	}

	public void setUnitSize(int unitSize) {
		this.unitSize = unitSize;
	}

	/**
	 * JSON representation of this object. Handwritten with love.
	 * 
	 * @return JSON string
	 */
	public String toJson() {
		return "{ \"unitId\":" + unitId + ",\"unitSize\":" + unitSize + " }";
	}

	@Override
	public String toString() {
		return "BeginMovementDTO [unitId=" + unitId + ", unitSize=" + unitSize
				+ "]";
	}
}
