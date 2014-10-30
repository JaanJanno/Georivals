package ee.bmagrupp.aardejaht.ui;

import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.model.LatLng;

public class MapClickListener implements OnMapClickListener {
	private MainActivity activity;

	MapClickListener(MainActivity activity) {
		this.activity = activity;
	}

	@Override
	public void onMapClick(LatLng point) {
		if (activity.userId == 0)
			activity.showLoginPrompt();
	}

}
