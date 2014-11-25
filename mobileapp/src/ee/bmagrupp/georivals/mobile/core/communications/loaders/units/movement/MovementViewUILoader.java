package ee.bmagrupp.georivals.mobile.core.communications.loaders.units.movement;

import java.util.List;

import android.app.Activity;
import ee.bmagrupp.georivals.mobile.core.communications.uiloaderinterface.UILoadable;
import ee.bmagrupp.georivals.mobile.core.communications.uiloaderinterface.UILoader;
import ee.bmagrupp.georivals.mobile.models.movement.MovementViewDTO;

/**
 * Class for retrieving MovementViewDTO lists from server. Able to handle
 * response straight on UI. Use this by overriding the list handling methods and
 * calling retrieveResponse() method.
 * 
 * @author Jaan Janno
 */

public abstract class MovementViewUILoader extends MovementViewLoader implements
		UILoadable<List<MovementViewDTO>> {

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
	public void handleResponse(List<MovementViewDTO> response) {
		UILoader.load(response, this, activity);
	}

}
