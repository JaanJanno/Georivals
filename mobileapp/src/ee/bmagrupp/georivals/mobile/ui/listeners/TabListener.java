package ee.bmagrupp.georivals.mobile.ui.listeners;

import ee.bmagrupp.georivals.mobile.R;
import ee.bmagrupp.georivals.mobile.ui.MainActivity;
import ee.bmagrupp.georivals.mobile.ui.widgets.TabItem;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Fragment;
import android.app.FragmentTransaction;

@SuppressWarnings("deprecation")
public class TabListener implements ActionBar.TabListener {
	private MainActivity activity;
	private Fragment fragment;
	private String mapString;

	public TabListener(MainActivity activity, TabItem TabItem) {
		this.activity = activity;
		this.fragment = (Fragment) TabItem;
		this.mapString = activity.getString(R.string.map);

	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		String fragmentTag = (String) tab.getTag();
		if (!fragmentTag.equals(mapString) && MainActivity.userId == 0) {
			if (MainActivity.choosingHomeProvince)
				activity.setToMapTab();
			else if (activity.getCurrentFragment() != MainActivity.REGISTRATION_FRAGMENT)
				activity.changeFragment(MainActivity.REGISTRATION_FRAGMENT,
						activity.getString(R.string.registration));
		} else
			activity.changeFragment(fragment, fragmentTag);
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {

	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		String fragmentTag = (String) tab.getTag();
		if (activity.getCurrentFragment() != fragment
				&& (MainActivity.userId != 0 || fragmentTag.equals(mapString))) {
			activity.changeFragment(fragment, fragmentTag);
		} else if (activity.getCurrentFragment() == fragment
				&& !fragmentTag.equals(mapString)) {
			activity.refreshCurrentFragment();
		}
	}
}
