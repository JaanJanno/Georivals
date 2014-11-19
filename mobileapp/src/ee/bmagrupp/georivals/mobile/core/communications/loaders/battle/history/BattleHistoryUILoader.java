package ee.bmagrupp.georivals.mobile.core.communications.loaders.battle.history;

import java.util.List;
import android.app.Activity;
import ee.bmagrupp.georivals.mobile.models.battle.history.BattleHistoryDTO;

/**
 * Class to handle battle history response list straight on UI. <br>
 * 
 * Use by overriding the abstract response handling methods to use the response.
 * 
 * @author Jaan Janno
 * 
 */

public abstract class BattleHistoryUILoader extends BattleHistoryLoader {

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
	public void handleResponseList(final List<BattleHistoryDTO> responseObject) {
		activity.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				handleResponseObjectInUI(responseObject);
			}
		});
		handleResponseObjectInBackground(responseObject);
	}

	/**
	 * Override to handle retrieved response in UI.
	 * 
	 * @param responseObject
	 */

	abstract public void handleResponseObjectInUI(
			List<BattleHistoryDTO> responseObject);

	/**
	 * Override to handle retrieved response in background.
	 * 
	 * @param responseObject
	 */

	abstract public void handleResponseObjectInBackground(
			List<BattleHistoryDTO> responseObject);

}
