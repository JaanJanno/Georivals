package ee.bmagrupp.georivals.mobile.core.communications.loaders.units;

import android.app.Activity;
import ee.bmagrupp.georivals.mobile.core.communications.uiloaderinterface.UILoadable;
import ee.bmagrupp.georivals.mobile.core.communications.uiloaderinterface.UILoader;
import ee.bmagrupp.georivals.mobile.models.ServerResponse;

/**
 * Class for claiming units. Able to handle response straight on UI. Use this by
 * overriding the response handling methods and calling retrieveResponse()
 * method.
 * 
 * @author Jaan Janno
 */

public abstract class UnitClaimUILoader extends UnitClaimLoader implements
		UILoadable<ServerResponse> {

	private Activity activity;

	/**
	 * 
	 * @param sid
	 *            Unique secret ID of the player.
	 * @param latitude
	 *            Latitude of the province moved to.
	 * @param longitude
	 *            Longitude of the province moved to.
	 * @param activity
	 *            Android activity that is modified.
	 */

	public UnitClaimUILoader(String sid, double latitude, double longitude,
			Activity activity) {
		super(sid, latitude, longitude);
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
