package ee.bmagrupp.georivals.mobile.ui.fragments;

import ee.bmagrupp.georivals.mobile.ui.widgets.TabItem;
import android.app.Fragment;

public class MyPlacesFragment extends Fragment implements TabItem {
	private final String tabName;
	private final int tabIconId;

	public MyPlacesFragment(String tabName, int tabIconId) {
		this.tabName = tabName;
		this.tabIconId = tabIconId;
	}

	@Override
	public int getTabIconId() {
		return tabIconId;
	}

	@Override
	public String getTabName() {
		return tabName;
	}

}
