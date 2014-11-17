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
			activity.getFragmentManager()
					.beginTransaction()
					.replace(R.id.fragment_container,
							MainActivity.REGISTRATION_FRAGMENT, "Registration")
					.commit();
		} else {
			ProvinceFragment.provinceLatLng = point;
			activity.getFragmentManager()
					.beginTransaction()
					.replace(R.id.fragment_container,
							MainActivity.PROVINCE_FRAGMENT, "Province")
					.commit();
		}
	}

}
