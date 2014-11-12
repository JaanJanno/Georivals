package ee.bmagrupp.georivals.mobile.ui.fragments;

import ee.bmagrupp.aardejaht.R;
import ee.bmagrupp.georivals.mobile.ui.widgets.TabItem;
import android.app.Fragment;

public class MyPlacesFragment extends Fragment implements TabItem {
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
