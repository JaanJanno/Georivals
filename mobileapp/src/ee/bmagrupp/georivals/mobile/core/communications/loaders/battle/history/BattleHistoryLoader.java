package ee.bmagrupp.georivals.mobile.core.communications.loaders.battle.history;

import java.util.List;
import ee.bmagrupp.georivals.mobile.core.communications.Constants;
import ee.bmagrupp.georivals.mobile.core.communications.loaders.GenericListLoader;
import ee.bmagrupp.georivals.mobile.models.battle.history.BattleHistoryDTO;

/**
 * Class for sending a request to get players battle
 * history. <br>
 * 
 * Use this by overriding the
 * handleResponseList() method and calling 
 * retrieveList() method.
 * 
 * @author Jaan Janno
 * 
 */

public abstract class BattleHistoryLoader extends
		GenericListLoader<BattleHistoryDTO> {

	/**
	 * 
	 * @param sid
	 *            Unique ID that identifies the player.
	 */

	public BattleHistoryLoader(String sid) {
		super(BattleHistoryDTO.listType, Constants.BATTLEHISTORY, "sid=" + sid);
	}

	/**
	 * Override this method to define the behavior after an object has been
	 * retrieved. Remember this method doesn't run on the UI thread!
	 */

	@Override
	public abstract void handleResponseList(List<BattleHistoryDTO> responseList);

}
