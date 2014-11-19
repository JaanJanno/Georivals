package ee.bmagrupp.georivals.mobile.models.movement;

import java.lang.reflect.Type;
import java.util.ArrayList;

import com.google.gson.reflect.TypeToken;

import ee.bmagrupp.georivals.mobile.models.province.ProvinceType;

/**
 * Basically carries information about all the units the player currently has
 * that aren't moving.
 * 
 * @author TKasekamp
 *
 */
public class MovementSelectionViewDTO {
	
	public static final Type listType = new TypeToken<ArrayList<MovementSelectionViewDTO>>() {
	}.getType();

	private final String provinceName;
	private final int unitId;
	private final int unitSize;

	/**
	 * If this is {@link ProvinceType#HOME} then the amount of units can be 0.
	 */
	private final ProvinceType type;

	public MovementSelectionViewDTO(String provinceName, int unitId,
			int unitSize, ProvinceType type) {
		this.provinceName = provinceName;
		this.unitId = unitId;
		this.unitSize = unitSize;
		this.type = type;
	}

	/**
	 * Default constructor when it's just a normal player owned province.
	 * 
	 * @param provinceName
	 * @param unitId
	 * @param unitSize
	 */
	public MovementSelectionViewDTO(String provinceName, int unitId,
			int unitSize) {
		this.provinceName = provinceName;
		this.unitId = unitId;
		this.unitSize = unitSize;
		this.type = ProvinceType.PLAYER;
	}

	public String getProvinceName() {
		return provinceName;
	}

	public int getUnitId() {
		return unitId;
	}

	public int getUnitSize() {
		return unitSize;
	}

	public ProvinceType getType() {
		return type;
	}

	@Override
	public String toString() {
		return "MovementSelectionViewDTO [provinceName=" + provinceName
				+ ", unitId=" + unitId + ", unitSize=" + unitSize + ", type="
				+ type + "]";
	}
	
}