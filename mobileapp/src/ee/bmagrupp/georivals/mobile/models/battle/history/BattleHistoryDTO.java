package ee.bmagrupp.georivals.mobile.models.battle.history;

import java.lang.reflect.Type;
import java.util.ArrayList;
import com.google.gson.reflect.TypeToken;
import ee.bmagrupp.georivals.mobile.models.battle.BattleType;

/**
 * DTO for battles that have happened in mission log view.
 * 
 * @author TKasekamp
 * 
 */
public class BattleHistoryDTO {

	public static final Type listType = new TypeToken<ArrayList<BattleHistoryDTO>>() {
	}.getType();

	private final int battleId;
	private final String provinceName;
	private final String otherPlayer;
	private final int myUnits;
	private final int otherUnits;
	private final int myLosses;
	private final int otherLosses;
	private final BattleType type;

	public BattleHistoryDTO(int battleId, String provinceName,
			String otherPlayer, int myUnits, int otherUnits, int myLosses,
			int otherLosses, BattleType type) {
		this.battleId = battleId;
		this.provinceName = provinceName;
		this.otherPlayer = otherPlayer;
		this.myUnits = myUnits;
		this.otherUnits = otherUnits;
		this.myLosses = myLosses;
		this.otherLosses = otherLosses;
		this.type = type;
	}

	public int getBattleId() {
		return battleId;
	}

	public String getProvinceName() {
		return provinceName;
	}

	public String getOtherPlayer() {
		return otherPlayer;
	}

	public int getMyUnits() {
		return myUnits;
	}

	public int getOtherUnits() {
		return otherUnits;
	}

	public int getMyLosses() {
		return myLosses;
	}

	public int getOtherLosses() {
		return otherLosses;
	}

	public BattleType getType() {
		return type;
	}
}