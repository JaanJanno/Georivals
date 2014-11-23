package ee.bmagrupp.georivals.mobile.ui.listeners;

import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.model.LatLng;

import ee.bmagrupp.georivals.mobile.R;
import ee.bmagrupp.georivals.mobile.ui.MainActivity;
import ee.bmagrupp.georivals.mobile.ui.fragments.ProvinceFragment;

public class MapClickListener implements OnMapClickListener {
	private MainActivity activity;

	public MapClickListener(MainActivity activity) {
		this.activity = activity;
	}

	@Override
	public void onMapClick(LatLng point) {
		if (MainActivity.choosingHomeProvince) {
			MainActivity.REGISTRATION_FRAGMENT
					.showPhase2ConfirmationDialog(point);
		} else if (MainActivity.userId == 0) {
			activity.changeFragment(MainActivity.REGISTRATION_FRAGMENT,
					activity.getString(R.string.registration));
		} else {
			ProvinceFragment.provinceLatLng = point;
			activity.changeFragment(MainActivity.PROVINCE_FRAGMENT,
					activity.getString(R.string.province));
		}
	}

}
