package ee.bmagrupp.aardejaht.ui.fragments;

import java.util.Map;

import ee.bmagrupp.aardejaht.R;
import ee.bmagrupp.aardejaht.core.communications.Constants;
import ee.bmagrupp.aardejaht.core.communications.highscore.ProfileEntryLoader;
import ee.bmagrupp.aardejaht.models.ProfileEntry;
import ee.bmagrupp.aardejaht.ui.MainActivity;
import ee.bmagrupp.aardejaht.ui.widgets.TabItem;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ProfileFragment extends Fragment implements TabItem {
	private String tabName = "Profile";
	private int tabIconId = R.drawable.profile_icon;
	private MainActivity activity;
	private ProfileEntryLoader profileEntryLoader;
	private RelativeLayout profileLayout;
	private ProfileEntry profile;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		profileLayout = (RelativeLayout) inflater.inflate(
				R.layout.profile_layout, container, false);
		return profileLayout;
	}

	@Override
	public void onDestroyView() {
		if (MainActivity.toast != null)
			MainActivity.toast.cancel();
		super.onDestroyView();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (activity == null) {
			activity = (MainActivity) getActivity();
			profileEntryLoader = new ProfileEntryLoader(Constants.PROFILE,
					activity.userId) {
				@Override
				public void handleResponseObject(final ProfileEntry profileEntry) {
					activity.runOnUiThread(new Runnable() {
						@Override
						public void run() {
							if (profileEntry != null) {
								profile = profileEntry;
								populateLayout();
							} else
								activity.showMessage("Failed to retrieve the profile info from the server.");
						}
					});
				}

				@Override
				public void addRequestParameters(Map<String, String> parameters) {
					// TODO Auto-generated method stub

				}
			};
		}
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

		usernameTextview.setText(username);
		emailTextview.setText(email);
		totalUnitsTextview.setText(Integer.toString(totalUnits));
		if (ownedProvinces != 0)
			averageUnitsTextview.setText(Integer.toString(totalUnits
					/ ownedProvinces));
		else
			averageUnitsTextview.setText("0");
		provincesTextview.setText(Integer.toString(ownedProvinces));

	}

	public ProfileEntryLoader getProfileEntryLoader() {
		return profileEntryLoader;
	}

	public RelativeLayout getProfileLayout() {
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
	public String getTabName() {
		return tabName;
	}
}