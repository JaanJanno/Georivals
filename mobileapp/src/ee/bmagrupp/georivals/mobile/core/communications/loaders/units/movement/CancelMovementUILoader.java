package ee.bmagrupp.georivals.mobile.core.communications.loaders.units.movement;

import android.app.Activity;
import ee.bmagrupp.georivals.mobile.core.communications.uiloaderinterface.UILoadable;
import ee.bmagrupp.georivals.mobile.core.communications.uiloaderinterface.UILoader;
import ee.bmagrupp.georivals.mobile.models.movement.MovementViewDTO;

/**
 * Class for canceling a movement from server. Able to handle response straight
 * on UI. Use this by overriding the response handling methods and calling
 * retrieveResponse() method.
 * 
 * @author Jaan Janno
 */

public abstract class CancelMovementUILoader extends CancelMovementLoader
		implements UILoadable<MovementViewDTO> {

	private Activity activity;

	/**
	 * 
	 * @param sid
	 *            User identifier sent in cookie.
	 * @param movementId
	 *            Id of movement to be deleted.
	 * @param activity
	 *            Android activity that is modified.
	 */

	public CancelMovementUILoader(String sid, int movementId, Activity activity) {
		super(sid, movementId);
		this.activity = activity;
	}

	/**
	 * Handles response on both UI and in background if so defined. Usually
	 * called by the object itself after getting a response. Useless to call
	 * manually.
	 */

	@Override
	public void handleResponse(MovementViewDTO responseObject) {
		UILoader.load(responseObject, this, activity);
	}

}
