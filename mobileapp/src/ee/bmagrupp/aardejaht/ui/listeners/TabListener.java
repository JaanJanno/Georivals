package ee.bmagrupp.aardejaht.ui.listeners;

import ee.bmagrupp.aardejaht.R;
import ee.bmagrupp.aardejaht.ui.MainActivity;
import ee.bmagrupp.aardejaht.ui.fragments.LocalFragment;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Fragment;
import android.app.FragmentTransaction;

public class TabListener implements ActionBar.TabListener {
	private MainActivity activity;
	private LocalFragment fragment;
	private String fragmentTag;

	public TabListener(MainActivity activity, LocalFragment fragment,
			String fragmentTag) {
		this.activity = activity;
		this.fragment = fragment;
		this.fragmentTag = fragmentTag;
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		if (fragmentTag == "ProfileFragment" && activity.userId == 0)
			activity.showRegistrationDialog();
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
