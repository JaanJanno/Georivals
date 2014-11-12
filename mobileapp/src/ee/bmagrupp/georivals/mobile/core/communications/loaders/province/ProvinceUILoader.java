package ee.bmagrupp.georivals.mobile.core.communications.loaders.province;

import java.util.List;

import android.app.Activity;

import ee.bmagrupp.georivals.mobile.models.CameraFOV;
import ee.bmagrupp.georivals.mobile.models.ProvinceDTO;

public abstract class ProvinceUILoader extends ProvinceLoader {
	
	private Activity activity;

	public ProvinceUILoader(String sid, CameraFOV fov, Activity activity) {
		super(sid, fov);
		this.activity = activity;
	}

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
	
	abstract public void handleResponseListInUI(List<ProvinceDTO> responseList);
	
	abstract public void handleResponseListInBackground(List<ProvinceDTO> responseList);

}
