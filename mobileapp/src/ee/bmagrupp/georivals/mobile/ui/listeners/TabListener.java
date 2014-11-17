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
	private TabItem tabItem;

	public TabListener(MainActivity activity, TabItem fragment) {
		this.activity = activity;
		this.tabItem = fragment;
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		String fragmentTag = (String) tab.getTag();
		if (MainActivity.choosingHomeProvince && !fragmentTag.equals("Map"))
			activity.getActionBar().setSelectedNavigationItem(0);
		else if (fragmentTag.equals("Profile") && MainActivity.userId == 0)
			ft.replace(R.id.fragment_container,
					MainActivity.REGISTRATION_FRAGMENT, "Registration");
		else
			ft.replace(R.id.fragment_container, (Fragment) tabItem, fragmentTag);
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {

	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		String fragmentTag = (String) tab.getTag();
		ft.replace(R.id.fragment_container, (Fragment) tabItem, fragmentTag);
	}

}
