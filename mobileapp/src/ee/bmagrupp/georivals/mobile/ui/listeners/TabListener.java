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

	public TabListener(MainActivity activity, TabItem tabItem) {
		this.activity = activity;
		this.fragment = (Fragment) tabItem;
		mapString = activity.getResources().getString(R.string.map);

	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		String fragmentTag = (String) tab.getTag();
		if (!fragmentTag.equals(mapString) && MainActivity.userId == 0) {
			if (MainActivity.choosingHomeProvince)
				activity.getActionBar().setSelectedNavigationItem(0);
			else if (!MainActivity.REGISTRATION_FRAGMENT.isVisible())
				ft.replace(R.id.fragment_container,
						MainActivity.REGISTRATION_FRAGMENT, "Registration");
		} else
			ft.replace(R.id.fragment_container, fragment, fragmentTag);
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {

	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		String fragmentTag = (String) tab.getTag();
		if (!fragment.isVisible()
				&& (MainActivity.userId != 0 || fragmentTag.equals(mapString))) {
			ft.replace(R.id.fragment_container, fragment, fragmentTag);
		} else if (fragment.isVisible() && !fragmentTag.equals(mapString)) {
			ft.detach(fragment).attach(fragment);
		}
	}
}
