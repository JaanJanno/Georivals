package ee.bmagrupp.georivals.mobile.core.communications.loaders.province.modify;

import android.app.Activity;
import ee.bmagrupp.georivals.mobile.models.ServerResponse;

/**
 * Class to handle renaming a province straight on UI. Use by overriding the
 * response handling methods to use the response.
 * 
 * @author Jaan Janno
 * 
 */

public abstract class RenameProvinceUILoader extends RenameProvinceLoader {

	private Activity activity;

	/**
	 * 
	 * @param longitude
	 *            Longitude of the renamed province.
	 * @param latitude
	 *            Latitude of the renamed province.
	 * @param newname
	 *            New name for the province.
	 * @param sid
	 *            Unique secret ID of the player.
	 * @param activity
	 *            Android activity that is modified.
	 */

	public RenameProvinceUILoader(double longitude, double latitude,
			String newname, String sid, Activity activity) {
		super(longitude, latitude, newname, sid);
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
	 * @param responseObject
	 *            Response retrieved from server.
	 */

	abstract public void handleResponseObjectInUI(ServerResponse responseObject);

	/**
	 * Override to handle retrieved response in background.
	 * 
	 * @param responseObject
	 *            Response retrieved from server.
	 */

	abstract public void handleResponseObjectInBackground(
			ServerResponse responseObject);

}
