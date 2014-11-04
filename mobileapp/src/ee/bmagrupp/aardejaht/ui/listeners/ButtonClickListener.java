package ee.bmagrupp.aardejaht.ui.listeners;

import android.location.LocationManager;

import com.google.android.gms.maps.GoogleMap.OnMyLocationButtonClickListener;

import ee.bmagrupp.aardejaht.ui.MainActivity;

public class ButtonClickListener implements OnMyLocationButtonClickListener {
	private LocationManager locationManager;
	private MainActivity activity;

	public ButtonClickListener(MainActivity activity,
			LocationManager locationManager) {
		this.activity = activity;
		this.locationManager = locationManager;
	}

	@Override
	public boolean onMyLocationButtonClick() {
		if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			activity.showMessage("GPS is disabled!");
		} else {
			activity.showMessage("Waiting for location...");
			return false;
		}
		return false;
	}

}
