package ee.bmagrupp.georivals.server.rest.domain;

import ee.bmagrupp.georivals.server.util.Constants;

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

	private final int unitSize;
	private final int newUnitSize;

	public ProvinceViewDTO(double latitude, double longitude,
			ProvinceType type, String provinceName, String ownerName,
			boolean isAttackable, boolean underAttack, int unitSize,
			int newUnitSize) {
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

	/**
	 * Constructor used when the province is owned by the BOT.
	 * 
	 * @param latitude
	 * @param longitude
	 * @param provinceName
	 * @param unitSize
	 */
	public ProvinceViewDTO(double latitude, double longitude,
			String provinceName, int unitSize) {
		this.latitude = latitude;
		this.longitude = longitude;
		this.type = ProvinceType.BOT;
		this.provinceName = provinceName;
		this.ownerName = Constants.BOT_NAME;
		this.isAttackable = true;
		this.underAttack = false;
		this.unitSize = unitSize;
		this.newUnitSize = 0;
	}

	/**
	 * Constructor used when the province is the player's home province.
	 * 
	 * @param latitude
	 * @param longitude
	 * @param ownerName
	 * @param provinceName
	 * @param unitSize
	 * @param newUnitSize
	 */
	public ProvinceViewDTO(double latitude, double longitude, String ownerName,
			String provinceName, int unitSize, int newUnitSize) {
		this.latitude = latitude;
		this.longitude = longitude;
		this.type = ProvinceType.HOME;
		this.provinceName = provinceName;
		this.ownerName = ownerName;
		this.isAttackable = false;
		this.underAttack = false;
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
