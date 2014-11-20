package ee.bmagrupp.georivals.mobile.core.communications.loaders.units.movement;

import java.util.List;
import android.app.Activity;
import ee.bmagrupp.georivals.mobile.models.movement.BeginMovementDTO;
import ee.bmagrupp.georivals.mobile.models.movement.BeginMovementResponse;

/**
 * Class for handling creating new movements. Able to handle response straight
 * on UI. Use this by overriding the response handling methods and calling
 * retrieveObject() method.
 * 
 * @author Jaan Janno
 */

public abstract class CreateMovementUILoader extends CreateMovementLoader {

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
	public void handleResponseObject(final BeginMovementResponse responseObject) {
		activity.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				handleResponseObjectInUI(responseObject);
			}
		});
		handleResponseObjectInBackground(responseObject);
	}

	/**
	 * Override to handle retrieved object in UI.
	 * 
	 * @param responseObject
	 */

	abstract public void handleResponseObjectInUI(
			BeginMovementResponse responseObject);

	/**
	 * Override to handle retrieved object in background.
	 * 
	 * @param responseObject
	 */

	abstract public void handleResponseObjectInBackground(
			BeginMovementResponse responseObject);

}
