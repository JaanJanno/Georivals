package ee.bmagrupp.georivals.mobile.core.communications.loaders.battle.history;

import java.util.List;
import ee.bmagrupp.georivals.mobile.core.communications.Constants;
import ee.bmagrupp.georivals.mobile.core.communications.loaders.GenericLoader;
import ee.bmagrupp.georivals.mobile.models.battle.history.BattleHistoryDTO;

/**
 * Class for sending a request to get players battle history. <br>
 * 
 * Use this by overriding the handleResponse() method and calling
 * retrieveResponse() method.
 * 
 * @author Jaan Janno
 * 
 */

public abstract class BattleHistoryLoader extends
		GenericLoader<List<BattleHistoryDTO>> {

	/**
	 * 
	 * @param sid
	 *            Unique ID that identifies the player.
	 */

	public BattleHistoryLoader(String sid) {
		super(BattleHistoryDTO.listType, Constants.BATTLEHISTORY, "sid=" + sid);
	}

}
