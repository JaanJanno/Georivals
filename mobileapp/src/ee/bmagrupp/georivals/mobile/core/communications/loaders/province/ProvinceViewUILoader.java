package ee.bmagrupp.georivals.mobile.core.communications.loaders.province;

import ee.bmagrupp.georivals.mobile.models.map.provinceloader.ProvinceDTO;
import android.app.Activity;

/**
 * Class for retrieving ProvinceViewDTO objects from server. Able 
 * to handle response straight on UI. Use this by overriding the 
 * object handling methods and calling retrieveObject() method.
 * @author	Jaan Janno
 */

public abstract class ProvinceViewUILoader extends ProvinceViewLoader {
	
	private Activity activity;

	/**
	 * 
	 * @param longitude
	 * @param latitude
	 * @param activity
	 */
	
	public ProvinceViewUILoader(Double longitude, Double latitude, Activity activity) {
		super(longitude, latitude);
		this.activity = activity;
	}
	
	/**
	 * 
	 * @param sid
	 * @param longitude
	 * @param latitude
	 * @param activity
	 */

	public ProvinceViewUILoader(String sid, Double longitude, Double latitude, Activity activity) {
		super(sid, longitude, latitude);
		this.activity = activity;
	}
	
	/**
	 * Handles response on both UI and in background if so defined.
	 * Usually called by the object itself after getting a response. 
	 * Useless to call manually.
	 */

	@Override
	public void handleResponseObject(final ProvinceDTO responseObject) {
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
	 * @param responseObject
	 */
	
	abstract public void handleResponseObjectInUI(ProvinceDTO responseObject);
	
	/**
	 * Override to handle retrieved object in background.
	 * @param responseObject
	 */
	
	abstract public void handleResponseObjectInBackground(ProvinceDTO responseObject);

}