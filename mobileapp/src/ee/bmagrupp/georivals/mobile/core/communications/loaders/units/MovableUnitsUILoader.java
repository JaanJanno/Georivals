package ee.bmagrupp.georivals.mobile.core.communications.loaders.units;

import java.util.List;

import android.app.Activity;
import ee.bmagrupp.georivals.mobile.core.communications.uiloaderinterface.UILoadable;
import ee.bmagrupp.georivals.mobile.core.communications.uiloaderinterface.UILoader;
import ee.bmagrupp.georivals.mobile.models.movement.MovementSelectionViewDTO;

/**
 * Class to handle possible movements to province in given coordinates response
 * list straight on UI. <br>
 * 
 * Use by overriding the abstract response handling methods to use the response.
 * 
 * @author Jaan Janno
 * 
 */

public abstract class MovableUnitsUILoader extends MovableUnitsLoader implements
		UILoadable<List<MovementSelectionViewDTO>> {

	private Activity activity;

	/**
	 * 
	 * @param sid
	 *            Unique ID that identifies the player.
	 * @param activity
	 *            Android activity that is modified.
	 */

	public MovableUnitsUILoader(String sid, Activity activity) {
		super(sid);
		this.activity = activity;
	}

	/**
	 * 
	 * @param sid
	 *            Unique ID that identifies the player.
	 * @param latitude
	 *            Latitude request parameter.
	 * @param longitude
	 *            Longitude request parameter.
	 * @param activity
	 *            Android activity that is modified.
	 */

	public MovableUnitsUILoader(String sid, double latitude, double longitude,
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
	public void handleResponse(List<MovementSelectionViewDTO> response) {
		UILoader.load(response, this, activity);
	}

}
