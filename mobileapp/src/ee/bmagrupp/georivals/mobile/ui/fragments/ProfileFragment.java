package ee.bmagrupp.georivals.mobile.ui.fragments;

import java.util.Map;

import ee.bmagrupp.georivals.mobile.R;
import ee.bmagrupp.georivals.mobile.core.communications.Constants;
import ee.bmagrupp.georivals.mobile.core.communications.highscore.ProfileEntryLoader;
import ee.bmagrupp.georivals.mobile.models.profile.ProfileEntry;
import ee.bmagrupp.georivals.mobile.ui.MainActivity;
import ee.bmagrupp.georivals.mobile.ui.widgets.TabItem;
import android.app.Fragment;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ProfileFragment extends Fragment implements TabItem {
	private final int tabNameId = R.string.profile;
	private final int tabIconId = R.drawable.profile_icon;

	private MainActivity activity;
	private Resources resources;
	private ProfileEntryLoader profileEntryLoader;
	private LinearLayout profileLayout;
	private ProfileEntry profile;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		profileLayout = (LinearLayout) inflater.inflate(
				R.layout.profile_layout, container, false);
		activity = (MainActivity) getActivity();
		resources = activity.getResources();
		requestProfileData();
		MainActivity.changeFonts(profileLayout);
		return profileLayout;
	}

	@Override
	public void onDestroyView() {
		if (MainActivity.toast != null)
			MainActivity.toast.cancel();
		super.onDestroyView();
	}

	private void requestProfileData() {
		profileEntryLoader = new ProfileEntryLoader(Constants.PROFILE,
				MainActivity.userId) {
			@Override
			public void handleResponseObject(final ProfileEntry profileEntry) {
				activity.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						if (profileEntry != null) {
							profile = profileEntry;
							populateLayout();
						} else
							activity.showMessage(resources
									.getString(R.string.error_retrieval_fail));
					}
				});
			}

			@Override
			public void addRequestParameters(Map<String, String> parameters) {

			}
		};
		profileEntryLoader.retrieveProfileEntry();
	}

	private void populateLayout() {
		String username = profile.getUsername();
		String email = profile.getEmail();
		int totalUnits = profile.getTotalUnits();
		int ownedProvinces = profile.getOwnedProvinces();

		TextView usernameTextview = (TextView) profileLayout
				.findViewById(R.id.profile_username);
		TextView emailTextview = (TextView) profileLayout
				.findViewById(R.id.profile_email);
		TextView totalUnitsTextview = (TextView) profileLayout
				.findViewById(R.id.profile_total_units);
		TextView averageUnitsTextview = (TextView) profileLayout
				.findViewById(R.id.profile_average_units);
		TextView provincesTextview = (TextView) profileLayout
				.findViewById(R.id.profile_provinces);

		usernameTextview.setText(resources.getString(R.string.username_colon)
				+ " " + username);
		emailTextview.setText(resources.getString(R.string.email_colon) + " "
				+ email);
		totalUnitsTextview.setText(resources.getString(R.string.units_total)
				+ " " + Integer.toString(totalUnits));
		if (ownedProvinces != 0)
			averageUnitsTextview.setText(resources
					.getString(R.string.units_average)
					+ " "
					+ Integer.toString(totalUnits / ownedProvinces));
		else
			averageUnitsTextview.setText(resources
					.getString(R.string.units_average) + " 0");
		provincesTextview.setText(resources.getString(R.string.provinces_owned)
				+ " " + Integer.toString(ownedProvinces));

	}

	public ProfileEntryLoader getProfileEntryLoader() {
		return profileEntryLoader;
	}

	public LinearLayout getProfileLayout() {
		return profileLayout;
	}

	public ProfileEntry getProfile() {
		return profile;
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