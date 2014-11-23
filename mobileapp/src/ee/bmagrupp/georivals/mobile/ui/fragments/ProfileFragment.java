package ee.bmagrupp.georivals.mobile.ui.fragments;

import ee.bmagrupp.georivals.mobile.R;
import ee.bmagrupp.georivals.mobile.core.communications.loaders.profile.ProfileEntryLoader;
import ee.bmagrupp.georivals.mobile.models.profile.ProfileEntry;
import ee.bmagrupp.georivals.mobile.ui.MainActivity;
import ee.bmagrupp.georivals.mobile.ui.widgets.TabItem;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

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
		ProfileEntryLoader profileEntryLoader = new ProfileEntryLoader(MainActivity.userId) {
			@Override
			public void handleResponseObject(final ProfileEntry profileEntry) {
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
		profileEntryLoader.retrieveObject();
	}

	/**
	 * Populates the layout.
	 */

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

		usernameTextview.setText(activity.getString(R.string.username_colon)
				+ " " + username);
		emailTextview.setText(activity.getString(R.string.email_colon) + " "
				+ email);
		totalUnitsTextview.setText(activity.getString(R.string.units_total)
				+ " " + Integer.toString(totalUnits));
		if (ownedProvinces != 0)
			averageUnitsTextview.setText(activity
					.getString(R.string.units_average)
					+ " "
					+ Integer.toString(totalUnits / ownedProvinces));
		else
			averageUnitsTextview.setText(activity
					.getString(R.string.units_average) + " 0");
		provincesTextview.setText(activity.getString(R.string.provinces_owned)
				+ " " + Integer.toString(ownedProvinces));

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