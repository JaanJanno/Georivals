package ee.bmagrupp.aardejaht.ui.fragments;

import ee.bmagrupp.aardejaht.R;
import ee.bmagrupp.aardejaht.ui.widgets.TabItem;
import android.app.Fragment;

public class MissionLogFragment extends Fragment implements TabItem {
	private String tabName = "Mission log";
	private int tabIconId = R.drawable.log_icon;

	@Override
	public int getTabIconId() {
		return tabIconId;
	}

	@Override
	public String getTabName() {
		return tabName;
	}

}
