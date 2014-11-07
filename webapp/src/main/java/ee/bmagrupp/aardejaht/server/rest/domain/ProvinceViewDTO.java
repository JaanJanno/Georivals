package ee.bmagrupp.aardejaht.server.rest.domain;

/**
 * DTO used for populating ProvinceView.
 * 
 * @author TKasekamp
 *
 */
public class ProvinceViewDTO {
	private final double latitude;
	private final double longitude;

	private final ProvinceType type;
	private final String provinceName;
	private final String ownerName;

	private final boolean isAttackable;
	private final boolean underAttack;

	private final int unitCount;
	private final int newUnitCount;

	public ProvinceViewDTO(double latitude, double longitude,
			ProvinceType type, String provinceName, String ownerName,
			boolean isAttackable, boolean underAttack, int unitCount,
			int newUnitCount) {
		super();
		this.latitude = latitude;
		this.longitude = longitude;
		this.type = type;
		this.provinceName = provinceName;
		this.ownerName = ownerName;
		this.isAttackable = isAttackable;
		this.underAttack = underAttack;
		this.unitCount = unitCount;
		this.newUnitCount = newUnitCount;
	}

	public double getLatitude() {
		return latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public ProvinceType getType() {
		return type;
	}

	public String getProvinceName() {
		return provinceName;
	}

	public String getOwnerName() {
		return ownerName;
	}

	public boolean isAttackable() {
		return isAttackable;
	}

	public boolean isUnderAttack() {
		return underAttack;
	}

	public int getUnitCount() {
		return unitCount;
	}

	public int getNewUnitCount() {
		return newUnitCount;
	}

}
