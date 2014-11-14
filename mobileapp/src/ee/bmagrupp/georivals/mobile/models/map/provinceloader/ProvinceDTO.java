package ee.bmagrupp.georivals.mobile.models.map.provinceloader;

import ee.bmagrupp.georivals.mobile.models.province.ProvinceType;

/**
 * DTO used for doing everything with provinces. This includes MapView,
 * ProvinceView and my provinces tab.
 * 
 * @author TKasekamp
 * 
 */
public class ProvinceDTO {
	private final double latitude;
	private final double longitude;

	private final ProvinceType type;
	private final String provinceName;
	private final String ownerName;

	private final boolean isAttackable;
	private final boolean underAttack;

	private final int unitSize;
	private final int newUnitSize;

	public ProvinceDTO(double latitude, double longitude, ProvinceType type,
			String provinceName, String ownerName, boolean isAttackable,
			boolean underAttack, int unitSize, int newUnitSize) {
		super();
		this.latitude = latitude;
		this.longitude = longitude;
		this.type = type;
		this.provinceName = provinceName;
		this.ownerName = ownerName;
		this.isAttackable = isAttackable;
		this.underAttack = underAttack;
		this.unitSize = unitSize;
		this.newUnitSize = newUnitSize;
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

	public int getUnitSize() {
		return unitSize;
	}

	public int getNewUnitSize() {
		return newUnitSize;
	}

}