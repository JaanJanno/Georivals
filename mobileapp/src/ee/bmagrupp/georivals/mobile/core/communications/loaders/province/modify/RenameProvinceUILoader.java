package ee.bmagrupp.georivals.mobile.core.communications.loaders.province.modify;

import android.app.Activity;
import ee.bmagrupp.georivals.mobile.core.communications.uiloaderinterface.UILoadable;
import ee.bmagrupp.georivals.mobile.core.communications.uiloaderinterface.UILoader;
import ee.bmagrupp.georivals.mobile.models.ServerResponse;

/**
 * Class to handle renaming a province straight on UI. Use by overriding the
 * response handling methods to use the response.
 * 
 * @author Jaan Janno
 * 
 */

public abstract class RenameProvinceUILoader extends RenameProvinceLoader
		implements UILoadable<ServerResponse> {

	private Activity activity;

	/**
	 * 
	 * @param latitude
	 *            Latitude of the renamed province.
	 * @param longitude
	 *            Longitude of the renamed province.
	 * @param newname
	 *            New name for the province.
	 * @param sid
	 *            Unique secret ID of the player.
	 * @param activity
	 *            Android activity that is modified.
	 */

	public RenameProvinceUILoader(double latitude, double longitude,
			String newname, String sid, Activity activity) {
		super(latitude, longitude, newname, sid);
		this.activity = activity;
	}

	/**
	 * Handles response on both UI and in background if so defined. Usually
	 * called by the object itself after getting a response. Useless to call
	 * manually.
	 */

	@Override
	public void handleResponse(ServerResponse response) {
		UILoader.load(response, this, activity);
	}

}
