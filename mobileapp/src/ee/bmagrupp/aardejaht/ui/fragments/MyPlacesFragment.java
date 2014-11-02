package ee.bmagrupp.aardejaht.ui.fragments;

import ee.bmagrupp.aardejaht.R;
import android.app.Fragment;

public class MyPlacesFragment extends Fragment implements LocalFragment {
	private String tabName = "My places";
	private int tabIconId = R.drawable.places_icon;

	@Override
	public int getTabIconId() {
		return tabIconId;
	}

	@Override
	public String getTabName() {
		return tabName;
	}

}
