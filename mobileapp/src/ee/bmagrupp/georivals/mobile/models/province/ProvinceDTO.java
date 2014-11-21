package ee.bmagrupp.georivals.mobile.models.province;

import java.lang.reflect.Type;
import java.util.ArrayList;

import com.google.android.gms.maps.model.GroundOverlay;
import com.google.gson.reflect.TypeToken;

/**
 * DTO used for doing everything with provinces. This includes MapView,
 * ProvinceView and my provinces tab.
 * 
 * @author TKasekamp
 * 
 */
public class ProvinceDTO {

	public static final Type listType = new TypeToken<ArrayList<ProvinceDTO>>() {
	}.getType();

	private final double latitude;
	private final double longitude;

	private final ProvinceType type;
	private final String provinceName;
	private final String ownerName;

	private final boolean attackable;
	private final boolean underAttack;

	private final int unitSize;
	private final int newUnitSize;

	private GroundOverlay groundOverlay;

	public ProvinceDTO(double latitude, double longitude, ProvinceType type,
			String provinceName, String ownerName, boolean attackable,
			boolean underAttack, int unitSize, int newUnitSize) {
		super();
		this.latitude = latitude;
		this.longitude = longitude;
		this.type = type;
		this.provinceName = provinceName;
		this.ownerName = ownerName;
		this.attackable = attackable;
		this.underAttack = underAttack;
		this.unitSize = unitSize;
		this.newUnitSize = newUnitSize;
	}

	public static Type getListtype() {
		return listType;
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
		return attackable;
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

	public GroundOverlay getGroundOverlay() {
		return groundOverlay;
	}

	public void setGroundOverlay(GroundOverlay groundOverlay) {
		this.groundOverlay = groundOverlay;
	}

	@Override
	public String toString() {
		return "ProvinceDTO [latitude=" + latitude + ", longitude=" + longitude
				+ ", type=" + type + ", provinceName=" + provinceName
				+ ", ownerName=" + ownerName + ", attackable=" + attackable
				+ ", underAttack=" + underAttack + ", unitSize=" + unitSize
				+ ", newUnitSize=" + newUnitSize + "]";
	}

}