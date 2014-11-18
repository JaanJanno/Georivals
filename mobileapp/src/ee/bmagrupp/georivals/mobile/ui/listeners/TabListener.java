package ee.bmagrupp.georivals.mobile.ui.listeners;

import ee.bmagrupp.georivals.mobile.R;
import ee.bmagrupp.georivals.mobile.ui.MainActivity;
import ee.bmagrupp.georivals.mobile.ui.widgets.TabItem;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.res.Resources;

@SuppressWarnings("deprecation")
public class TabListener implements ActionBar.TabListener {
	private MainActivity activity;
	private Fragment fragment;
	private Resources resources;

	public TabListener(MainActivity activity, TabItem tabItem) {
		this.activity = activity;
		this.fragment = (Fragment) tabItem;
		this.resources = activity.getResources();
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		String fragmentTag = (String) tab.getTag();
		if (!fragmentTag.equals(resources.getString(R.string.map))) {
			if (MainActivity.choosingHomeProvince)
				activity.getActionBar().setSelectedNavigationItem(0);
			else if (MainActivity.userId == 0
					&& !MainActivity.REGISTRATION_FRAGMENT.isVisible())
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
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
			String fragmentTag = (String) tab.getTag();
			ft.replace(R.id.fragment_container, fragment, fragmentTag);
		}
	}

}
