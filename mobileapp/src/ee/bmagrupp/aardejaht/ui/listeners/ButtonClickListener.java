package ee.bmagrupp.aardejaht.ui.listeners;

import android.app.Activity;
import android.location.LocationManager;

import com.google.android.gms.maps.GoogleMap.OnMyLocationButtonClickListener;

import ee.bmagrupp.aardejaht.ui.MainActivity;

public class ButtonClickListener implements OnMyLocationButtonClickListener {
	private LocationManager locationManager;
	private Activity activity;
	
	public ButtonClickListener(Activity activity, LocationManager locationManager) {
		this.activity = activity;
		this.locationManager = locationManager;
	}

	@Override
	public boolean onMyLocationButtonClick() {
		if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			MainActivity.showMessage(activity, "GPS is disabled!");
		} else {
			MainActivity.showMessage(activity, "Waiting for location...");
			return false;
		}
		return false;
	}

}
