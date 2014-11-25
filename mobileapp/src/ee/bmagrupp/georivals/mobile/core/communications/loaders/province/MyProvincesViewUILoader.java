package ee.bmagrupp.georivals.mobile.core.communications.loaders.province;

import java.util.List;

import android.app.Activity;
import ee.bmagrupp.georivals.mobile.core.communications.uiloaderinterface.UILoadable;
import ee.bmagrupp.georivals.mobile.core.communications.uiloaderinterface.UILoader;
import ee.bmagrupp.georivals.mobile.models.province.ProvinceDTO;

/**
 * Class used for loading a list of provinces owned by the player and handling
 * it straight on UI thread. Use by overriding the handleResponseListInUI() and
 * handleResponseListInBackground() methods.
 * 
 * @author Jaan Janno
 * 
 */

public abstract class MyProvincesViewUILoader extends MyProvincesViewLoader
		implements UILoadable<List<ProvinceDTO>> {

	private Activity activity;

	/**
	 * 
	 * @param sid
	 *            Unique secret ID of the player.
	 * @param activity
	 *            Android activity that is modified.
	 */

	public MyProvincesViewUILoader(String sid, Activity activity) {
		super(sid);
		this.activity = activity;
	}

	/**
	 * Handles response on both UI and in background if so defined. Usually
	 * called by the object itself after getting a response. Useless to call
	 * manually.
	 */

	@Override
	public void handleResponse(List<ProvinceDTO> response) {
		UILoader.load(response, this, activity);
	}

}
