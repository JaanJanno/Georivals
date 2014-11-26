package ee.bmagrupp.georivals.mobile.ui.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import ee.bmagrupp.georivals.mobile.R;
import ee.bmagrupp.georivals.mobile.core.communications.loaders.profile.ProfileEntryLoader;
import ee.bmagrupp.georivals.mobile.models.profile.ProfileEntry;
import ee.bmagrupp.georivals.mobile.ui.MainActivity;
import ee.bmagrupp.georivals.mobile.ui.widgets.TabItem;

public class ProfileFragment extends Fragment implements TabItem {
	// non-static immutable variables (local constants)
	private MainActivity activity;
	private final int tabNameId = R.string.profile;
	private final int tabIconId = R.drawable.profile_icon;
	private LinearLayout profileLayout;

	// non-static mutable variables
	private ProfileEntry profile;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		profileLayout = (LinearLayout) inflater.inflate(
				R.layout.profile_layout, container, false);
		activity = (MainActivity) getActivity();
		requestProfileData();
		MainActivity.changeFonts(profileLayout);
		return profileLayout;
	}

	@Override
	public void onDestroyView() {
		activity.cancelToastMessage();
		super.onDestroyView();
	}

	/**
	 * Requests the player's profile data from the server. If successful, it
	 * populates the layout.
	 */

	private void requestProfileData() {
		ProfileEntryLoader profileEntryLoader = new ProfileEntryLoader(
				MainActivity.userId) {
			@Override
			public void handleResponse(final ProfileEntry profileEntry) {
				activity.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						if (profileEntry != null) {
							profile = profileEntry;
							populateLayout();
						} else
							activity.showToastMessage(activity
									.getString(R.string.error_retrieval_fail));
					}
				});
			}
		};
		profileEntryLoader.retrieveResponse();
	}

	/**
	 * Populates the layout.
	 */

	private void populateLayout() {
		String username = profile.getUsername();
		String email = profile.getEmail();
		if (email.equals(""))
			email = "-";
		int totalUnits = profile.getTotalUnits();
		int ownedProvinces = profile.getOwnedProvinces();

		TextView generalInfo = (TextView) profileLayout
				.findViewById(R.id.profile_general_info);
		TextView strategicalInfo = (TextView) profileLayout
				.findViewById(R.id.profile_strategical_info);

		generalInfo.setText(activity.getString(R.string.username_colon)
				+ username + "\n" + activity.getString(R.string.email_colon)
				+ email);

		double averageUnits = 0;
		if (ownedProvinces != 0)
			averageUnits = (double) Math.round((double) totalUnits
					/ ownedProvinces * 10) / 10;

		strategicalInfo.setText(activity.getString(R.string.units_total_number)
				+ totalUnits + "\n"
				+ activity.getString(R.string.units_average) + averageUnits
				+ "\n" + activity.getString(R.string.provinces_owned)
				+ ownedProvinces);

		setRunInBackgroundCheckBox();
	}

	/**
	 * Sets up the "Run Georivals in the background" checkbox, including its
	 * listener.
	 */

	private void setRunInBackgroundCheckBox() {
		CheckBox runInBackground = (CheckBox) profileLayout
				.findViewById(R.id.setting_run_in_background);
		runInBackground.setChecked(activity.isLocationServiceEnabled());

		runInBackground
				.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						boolean locationServiceEnabled = activity
								.isLocationServiceEnabled();
						activity.setLocationServiceEnabled(!locationServiceEnabled);
						if (locationServiceEnabled)
							activity.showToastMessage(activity
									.getString(R.string.service_disabled));
						else
							activity.showToastMessage(activity
									.getString(R.string.service_enabled));
					}
				});
	}

	/**
	 * @return The layout of the profile fragment.
	 */

	public LinearLayout getProfileLayout() {
		return profileLayout;
	}

	/**
	 * @return The player's profile entry object.
	 */

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