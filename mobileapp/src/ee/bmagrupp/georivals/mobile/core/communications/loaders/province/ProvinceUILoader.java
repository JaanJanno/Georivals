package ee.bmagrupp.georivals.mobile.core.communications.loaders.province;

import java.util.List;

import android.app.Activity;

import ee.bmagrupp.georivals.mobile.models.map.CameraFOV;
import ee.bmagrupp.georivals.mobile.models.province.ProvinceDTO;

/**
 * Class for retrieving ProvinceDTO lists from server. Able 
 * to handle response straight on UI. Use this by overriding the 
 * list handling methods and calling retrieveList() method.
 * @author	Jaan Janno
 */

public abstract class ProvinceUILoader extends ProvinceLoader {
	
	private Activity activity;
	
	/**
	 * 
	 * @param sid
	 * @param fov
	 * @param activity
	 */

	public ProvinceUILoader(String sid, CameraFOV fov, Activity activity) {
		super(sid, fov);
		this.activity = activity;
	}
	
	/**
	 * Handles response on both UI and in background if so defined.
	 * Usually called by the object itself after getting a response. 
	 * Useless to call manually.
	 */

	@Override
	public void handleResponseList(final List<ProvinceDTO> responseList) {
		activity.runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				handleResponseListInUI(responseList);
			}
		});		
		handleResponseListInBackground(responseList);
	}
	
	/**
	 * Override to handle retrieved list in UI.
	 * @param responseList
	 */
	
	abstract public void handleResponseListInUI(List<ProvinceDTO> responseList);
	
	/**
	 * Override to handle retrieved list in background.
	 * @param responseList
	 */
	
	abstract public void handleResponseListInBackground(List<ProvinceDTO> responseList);

}
