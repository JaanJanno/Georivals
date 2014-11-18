package ee.bmagrupp.georivals.mobile.core.communications.loaders.units.movement;

import java.util.List;
import android.app.Activity;
import ee.bmagrupp.georivals.mobile.models.movement.MovementViewDTO;

/**
 * Class for retrieving MovementViewDTO lists from server. Able to handle
 * response straight on UI. Use this by overriding the list handling methods and
 * calling retrieveList() method.
 * 
 * @author Jaan Janno
 */

public abstract class MovementViewUILoader extends MovementViewLoader {

	private Activity activity;

	/**
	 * 
	 * @param sid
	 *            User identifier sent in cookie.
	 * @param activity
	 *            Android activity that is modified.
	 */

	public MovementViewUILoader(String sid, Activity activity) {
		super(sid);
		this.activity = activity;
	}

	/**
	 * Handles response on both UI and in background if so defined. Usually
	 * called by the object itself after getting a response. Useless to call
	 * manually.
	 */

	@Override
	public void handleResponseList(final List<MovementViewDTO> responseList) {
		activity.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				handleResponseListInUI(responseList);
			}
		});
		handleResponseListInBackground(responseList);
	}

	/**
	 * Override to handle retrieved list in UI.
	 * 
	 * @param responseList
	 */

	abstract public void handleResponseListInUI(
			List<MovementViewDTO> responseList);

	/**
	 * Override to handle retrieved list in background.
	 * 
	 * @param responseList
	 */

	abstract public void handleResponseListInBackground(
			List<MovementViewDTO> responseList);

}
