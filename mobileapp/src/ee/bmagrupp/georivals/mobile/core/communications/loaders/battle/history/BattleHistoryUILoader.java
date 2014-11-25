package ee.bmagrupp.georivals.mobile.core.communications.loaders.battle.history;

import java.util.List;

import android.app.Activity;
import ee.bmagrupp.georivals.mobile.core.communications.uiloaderinterface.UILoadable;
import ee.bmagrupp.georivals.mobile.core.communications.uiloaderinterface.UILoader;
import ee.bmagrupp.georivals.mobile.models.battle.history.BattleHistoryDTO;

/**
 * Class to handle battle history response list straight on UI. <br>
 * 
 * Use by overriding the abstract response handling methods to use the response.
 * 
 * @author Jaan Janno
 * 
 */

public abstract class BattleHistoryUILoader extends BattleHistoryLoader
		implements UILoadable<List<BattleHistoryDTO>> {

	private Activity activity;

	/**
	 * 
	 * @param sid
	 *            Unique ID that identifies the player.
	 * @param activity
	 *            Android activity that is modified.
	 */

	public BattleHistoryUILoader(String sid, Activity activity) {
		super(sid);
		this.activity = activity;
	}

	/**
	 * Handles response on both UI and in background if so defined. Usually
	 * called by the object itself after getting a response. Useless to call
	 * manually.
	 */

	@Override
	public void handleResponse(List<BattleHistoryDTO> response) {
		UILoader.load(response, this, activity);
	}

}
