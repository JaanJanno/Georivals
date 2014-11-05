package ee.bmagrupp.aardejaht.ui.listeners;

import ee.bmagrupp.aardejaht.R;
import ee.bmagrupp.aardejaht.ui.MainActivity;
import ee.bmagrupp.aardejaht.ui.widgets.TabItem;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Fragment;
import android.app.FragmentTransaction;

public class TabListener implements ActionBar.TabListener {
	private MainActivity activity;
	private TabItem fragment;

	public TabListener(MainActivity activity, TabItem fragment) {
		this.activity = activity;
		this.fragment = fragment;
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		String fragmentTag = (String) tab.getTag();
		if (activity.choosingHomeProvince && !fragmentTag.equals("Map"))
			activity.getActionBar().setSelectedNavigationItem(0);
		else if (fragmentTag.equals("Profile") && activity.userId == 0)
			ft.replace(R.id.fragment_container,
					activity.getRegistrationFragment(), "Registration");
		else
			ft.replace(R.id.fragment_container, (Fragment) fragment,
					fragmentTag);
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {

	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {

	}

}
