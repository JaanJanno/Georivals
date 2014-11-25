package ee.bmagrupp.georivals.mobile.ui.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

import ee.bmagrupp.georivals.mobile.R;
import ee.bmagrupp.georivals.mobile.core.communications.loaders.registration.RegistrationPhase1Poster;
import ee.bmagrupp.georivals.mobile.core.communications.loaders.registration.RegistrationPhase2Poster;
import ee.bmagrupp.georivals.mobile.models.ServerResponse;
import ee.bmagrupp.georivals.mobile.models.ServerResult;
import ee.bmagrupp.georivals.mobile.models.registration.RegistrationDTO;
import ee.bmagrupp.georivals.mobile.ui.MainActivity;
import ee.bmagrupp.georivals.mobile.ui.widgets.CustomDialog;

@SuppressWarnings("deprecation")
public class RegistrationFragment extends Fragment {
	// non-static immutable variables (local constants)
	private MainActivity activity;
	private RelativeLayout registrationLayout;

	// non-static mutable variables
	private String username;
	private String email;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		registrationLayout = (RelativeLayout) inflater.inflate(
				R.layout.registration_layout, container, false);
		MainActivity.changeFonts(registrationLayout);
		activity = (MainActivity) getActivity();
		setButtonListeners();
		return registrationLayout;
	}

	@Override
	public void onDestroyView() {
		activity.cancelToastMessage();
		super.onDestroyView();
	}

	/**
	 * Sets click listeners for the layout's buttons.
	 */

	private void setButtonListeners() {
		Button startButton = (Button) registrationLayout
				.findViewById(R.id.registration_start);
		TextView existingAccountTextView = (TextView) registrationLayout
				.findViewById(R.id.registration_existing_account);

		startButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				EditText usernameEditText = (EditText) activity
						.findViewById(R.id.registration_username_textbox);
				EditText emailEditText = (EditText) activity
						.findViewById(R.id.registration_email_textbox);
				String usernameString = usernameEditText.getText().toString();
				String emailString = emailEditText.getText().toString();
				if (usernameString.equals("")) {
					activity.showToastMessage(activity
							.getString(R.string.error_username_null));
				} else {
					username = usernameString;
					email = emailString;
					registrationPhase1();
				}
			}
		});

		existingAccountTextView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				activity.changeFragment(MainActivity.LOGIN_FRAGMENT,
						activity.getString(R.string.login));
			}
		});
	}

	/**
	 * Registration phase 1 - send a request to the server to check if the
	 * inserted username is available.
	 */

	private void registrationPhase1() {
		RegistrationPhase1Poster p = new RegistrationPhase1Poster(username) {

			@Override
			public void handleResponse(ServerResponse responseObject) {
				if (responseObject.getResult() == ServerResult.OK)
					showPhase1ConfirmationDialog();
				else if (responseObject.getResult() == ServerResult.USERNAME_IN_USE)
					activity.showToastMessage(activity
							.getString(R.string.error_username_taken));
				else
					activity.showToastMessage(activity
							.getString(R.string.error_unknown));
			}
		};
		p.retrieveResponse();
	}

	/**
	 * Sets up and displays a username confirmation dialog.
	 */

	private void showPhase1ConfirmationDialog() {
		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				final CustomDialog confirmationDialog = new CustomDialog(
						activity);

				confirmationDialog.setMessage(activity
						.getString(R.string.confirmation_username1)
						+ username
						+ activity.getString(R.string.confirmation_username2));
				confirmationDialog.hideInput();

				confirmationDialog.setPositiveButton(new OnClickListener() {
					@Override
					public void onClick(View v) {
						MainActivity.choosingHomeProvince = true;
						if (activity.getActionBar().getSelectedTab().getTag()
								.equals(activity.getString(R.string.map))) {
							activity.changeFragment(MainActivity.MAP_FRAGMENT,
									activity.getString(R.string.map));
						} else {
							activity.setToMapTab();
						}
						confirmationDialog.dismiss();
					}
				});

				confirmationDialog.show();

			}
		});
	}

	/**
	 * Sets up and displays a 'Set Home' confirmation dialog.
	 */

	public void showPhase2ConfirmationDialog(final LatLng provinceLatLng) {
		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				final CustomDialog confirmationDialog = new CustomDialog(
						activity);

				confirmationDialog.setMessage(activity
						.getString(R.string.confirmation_province));
				confirmationDialog.hideInput();

				confirmationDialog.setPositiveButton(new OnClickListener() {
					@Override
					public void onClick(View v) {
						registrationPhase2(provinceLatLng);
						confirmationDialog.dismiss();
					}
				});

				confirmationDialog.show();

			}
		});

	}

	/**
	 * Registration phase 2 - send a request to the server to register a new
	 * user with the inserted name, email and the given home location.
	 * 
	 * @param homeLatLng
	 */

	private void registrationPhase2(LatLng homeLatLng) {
		RegistrationPhase2Poster p2 = new RegistrationPhase2Poster(
				new RegistrationDTO(username, email, homeLatLng.latitude,
						homeLatLng.longitude)) {

			@Override
			public void handleResponse(final ServerResponse responseObject) {
				activity.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						ServerResult result = responseObject.getResult();
						String sid = responseObject.getValue();
						int userId = responseObject.getId();
						if (result == ServerResult.OK) {
							TextView chooseHomeLabel = (TextView) activity
									.findViewById(R.id.choose_home_label);
							chooseHomeLabel.setVisibility(View.INVISIBLE);
							Button setHomeButton = (Button) activity
									.findViewById(R.id.set_home_current);
							setHomeButton.setVisibility(View.INVISIBLE);
							MainActivity.choosingHomeProvince = false;
							activity.setUserData(sid, userId);
							activity.showToastMessage(activity
									.getString(R.string.user_created));
						} else if (result == ServerResult.USERNAME_IN_USE) {
							activity.showToastMessage(activity
									.getString(R.string.error_username_taken));
						} else {
							activity.showToastMessage(activity
									.getString(R.string.error_unknown));
						}
					}
				});

			}
		};
		p2.retrieveResponse();
	}

}
