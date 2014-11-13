package ee.bmagrupp.georivals.mobile.ui.listeners;

import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.model.LatLng;

import ee.bmagrupp.georivals.mobile.R;
import ee.bmagrupp.georivals.mobile.ui.MainActivity;

public class MapClickListener implements OnMapClickListener {
	private MainActivity activity;

	public MapClickListener(MainActivity activity) {
		this.activity = activity;
	}

	@Override
	public void onMapClick(LatLng point) {
		if (MainActivity.choosingHomeProvince)
			MainActivity.REGISTRATION_FRAGMENT.showPhase2ConfirmationDialog(
					point.latitude, point.longitude);
		else if (MainActivity.userId == 0)
			activity.getFragmentManager()
					.beginTransaction()
					.replace(R.id.fragment_container,
							MainActivity.REGISTRATION_FRAGMENT, "Registration")
					.commit();

	}

}
