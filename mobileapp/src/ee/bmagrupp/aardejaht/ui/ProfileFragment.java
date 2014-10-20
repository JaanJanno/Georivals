package ee.bmagrupp.aardejaht.ui;

import ee.bmagrupp.aardejaht.R;
import ee.bmagrupp.aardejaht.core.communications.Constants;
import ee.bmagrupp.aardejaht.core.communications.highscore.ProfileEntryLoader;
import ee.bmagrupp.aardejaht.models.ProfileEntry;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ProfileFragment extends Fragment {
	private Activity activity;
	private ProfileEntryLoader profileEntryLoader;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.profile_layout, container, false);
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
			activity = getActivity();
			profileEntryLoader = new ProfileEntryLoader(Constants.PROFILE, 1) {
				@Override
				public void handleResponseObject(final ProfileEntry profile) {
					activity.runOnUiThread(new Runnable() {
						@Override
						public void run() {
							if (profile != null)
								populateLayout(profile);
							else
								MainActivity
										.showMessage(activity,
												"Failed to retrieve the profile info from the server.");
						}
					});
				}
			};
		}
		profileEntryLoader.retrieveProfileEntry();
	}

	private void populateLayout(ProfileEntry profile) {
		String username = profile.getUsername();
		String email = profile.getEmail();
		int totalUnits = profile.getTotalUnits();
		int ownedProvinces = profile.getOwnedProvinces();

		TextView usernameTextview = (TextView) getActivity().findViewById(
				R.id.profile_username);
		TextView emailTextview = (TextView) activity
				.findViewById(R.id.profile_email);
		TextView overallTimeTextview = (TextView) activity
				.findViewById(R.id.profile_overall_time);
		TextView totalUnitsTextview = (TextView) activity
				.findViewById(R.id.profile_total_units);
		TextView averageUnitsTextview = (TextView) activity
				.findViewById(R.id.profile_average_units);
		TextView provincesTextview = (TextView) activity
				.findViewById(R.id.profile_provinces);

		usernameTextview.setText(username);
		emailTextview.setText(email);
		overallTimeTextview.setText("5 day(s), 1 hour(s)");
		totalUnitsTextview.setText(Integer.toString(totalUnits));
		averageUnitsTextview.setText(Integer.toString(totalUnits
				/ ownedProvinces));
		provincesTextview.setText(Integer.toString(ownedProvinces));

	}
}
