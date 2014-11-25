package ee.bmagrupp.georivals.mobile.core.communications.loaders.province.modify;

import android.app.Activity;
import ee.bmagrupp.georivals.mobile.core.communications.uiloaderinterface.UILoadable;
import ee.bmagrupp.georivals.mobile.core.communications.uiloaderinterface.UILoader;
import ee.bmagrupp.georivals.mobile.models.ServerResponse;

/**
 * Class to handle moving a home province straight on UI. Use by overriding the
 * abstract response handling methods to use the response.
 * 
 * @author Jaan Janno
 * 
 */

public abstract class ChangeHomeUILoader extends ChangeHomeLoader implements
		UILoadable<ServerResponse> {

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
	public void handleResponse(ServerResponse response) {
		UILoader.load(response, this, activity);
	}

}
