package ee.bmagrupp.georivals.mobile.core.communications.loaders.province.modify;

import android.app.Activity;
import ee.bmagrupp.georivals.mobile.models.ServerResponse;

/**
 * Class to handle moving a home province straight on UI. Use by overriding the
 * abstract response handling methods to use the response.
 * 
 * @author Jaan Janno
 * 
 */

public abstract class ChangeHomeUILoader extends ChangeHomeLoader {

	private Activity activity;

	/**
	 * 
	 * @param latitude
	 *            Latitude of the new home province.
	 * @param longitude
	 *            Longitude of the new home province.
	 * @param sid
	 *            Unique ID that identifies the player.
	 * @param activity
	 *            Android activity that is modified.
	 */

	public ChangeHomeUILoader(double latitude, double longitude, String sid,
			Activity activity) {
		super(latitude, longitude, sid);
		this.activity = activity;
	}

	/**
	 * Handles response on both UI and in background if so defined. Usually
	 * called by the object itself after getting a response. Useless to call
	 * manually.
	 */

	@Override
	public void handleResponseObject(final ServerResponse responseObject) {
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
	 * @param responseList
	 */

	abstract public void handleResponseObjectInUI(ServerResponse responseObject);

	/**
	 * Override to handle retrieved response in background.
	 * 
	 * @param responseList
	 */

	abstract public void handleResponseObjectInBackground(
			ServerResponse responseObject);

}
