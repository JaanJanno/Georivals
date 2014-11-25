package ee.bmagrupp.georivals.mobile.core.communications.loaders.province;

import android.app.Activity;
import ee.bmagrupp.georivals.mobile.core.communications.uiloaderinterface.UILoadable;
import ee.bmagrupp.georivals.mobile.core.communications.uiloaderinterface.UILoader;
import ee.bmagrupp.georivals.mobile.models.province.ProvinceDTO;

/**
 * Class for retrieving ProvinceViewDTO objects from server. Able to handle
 * response straight on UI. Use this by overriding the object handling methods
 * and calling retrieveResponse() method.
 * 
 * @author Jaan Janno
 */

public abstract class ProvinceViewUILoader extends ProvinceViewLoader implements
		UILoadable<ProvinceDTO> {

	private Activity activity;

	/**
	 * 
	 * @param latitude
	 * @param longitude
	 * @param activity
	 */

	public ProvinceViewUILoader(Double latitude, Double longitude,
			Activity activity) {
		super(latitude, longitude);
		this.activity = activity;
	}

	/**
	 * 
	 * @param sid
	 * @param latitude
	 * @param longitude
	 * @param activity
	 */

	public ProvinceViewUILoader(String sid, Double latitude, Double longitude,
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
	public void handleResponse(ProvinceDTO response) {
		UILoader.load(response, this, activity);
	}

}