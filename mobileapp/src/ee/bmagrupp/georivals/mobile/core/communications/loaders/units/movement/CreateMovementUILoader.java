package ee.bmagrupp.georivals.mobile.core.communications.loaders.units.movement;

import java.util.List;

import android.app.Activity;
import ee.bmagrupp.georivals.mobile.core.communications.uiloaderinterface.UILoadable;
import ee.bmagrupp.georivals.mobile.core.communications.uiloaderinterface.UILoader;
import ee.bmagrupp.georivals.mobile.models.movement.BeginMovementDTO;
import ee.bmagrupp.georivals.mobile.models.movement.BeginMovementResponse;

/**
 * Class for handling creating new movements. Able to handle response straight
 * on UI. Use this by overriding the response handling methods and calling
 * retrieveResponse() method.
 * 
 * @author Jaan Janno
 */

public abstract class CreateMovementUILoader extends CreateMovementLoader
		implements UILoadable<BeginMovementResponse> {

	private Activity activity;

	/**
	 * 
	 * @param post
	 *            List of movements the user wishes to do.
	 * @param sid
	 *            Unique secret ID of the player.
	 * @param latitude
	 *            Latitude of the province moved to.
	 * @param longitude
	 *            Longitude of the province moved to.
	 * @param activity
	 *            Android activity that is modified.
	 */

	public CreateMovementUILoader(List<BeginMovementDTO> post, String sid,
			double latitude, double longitude, Activity activity) {
		super(post, sid, latitude, longitude);
		this.activity = activity;
	}

	/**
	 * Handles response on both UI and in background if so defined. Usually
	 * called by the object itself after getting a response. Useless to call
	 * manually.
	 */

	@Override
	public void handleResponse(BeginMovementResponse response) {
		UILoader.load(response, this, activity);
	}

}
