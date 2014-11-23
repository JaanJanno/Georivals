package ee.bmagrupp.georivals.mobile.ui.fragments;

import ee.bmagrupp.georivals.mobile.R;
import ee.bmagrupp.georivals.mobile.ui.MainActivity;
import ee.bmagrupp.georivals.mobile.ui.widgets.TabItem;
import android.app.Fragment;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

@SuppressWarnings("unused")
public class MissionLogFragment extends Fragment implements TabItem {
	// non-static immutable variables (local constants)
	private MainActivity activity;
	private LinearLayout missionLogLayout;
	private final int tabNameId = R.string.mission_log;
	private final int tabIconId = R.drawable.log_icon;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		missionLogLayout = (LinearLayout) inflater.inflate(
				R.layout.mission_log_layout, container, false);
		activity = (MainActivity) getActivity();
		MainActivity.changeFonts(missionLogLayout);
		return missionLogLayout;
	}

	@Override
	public void onDestroyView() {
		activity.cancelToastMessage();
		super.onDestroyView();
	}

	@Override
	public int getTabIconId() {
		return tabIconId;
	}

	@Override
	public int getTabNameId() {
		return tabNameId;
	}

}
