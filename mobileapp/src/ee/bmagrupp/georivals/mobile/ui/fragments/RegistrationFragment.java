package ee.bmagrupp.georivals.mobile.ui.fragments;

import ee.bmagrupp.aardejaht.R;
import ee.bmagrupp.georivals.mobile.core.communications.loaders.registration.RegistrationPhase1Poster;
import ee.bmagrupp.georivals.mobile.core.communications.loaders.registration.RegistrationPhase2Poster;
import ee.bmagrupp.georivals.mobile.models.RegistrationDTO;
import ee.bmagrupp.georivals.mobile.models.RegistrationResponse;
import ee.bmagrupp.georivals.mobile.models.ServerResult;
import ee.bmagrupp.georivals.mobile.ui.MainActivity;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class RegistrationFragment extends Fragment {
	private RelativeLayout registrationLayout;
	private MainActivity activity;
	private String username;
	private String email;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		registrationLayout = (RelativeLayout) inflater.inflate(
				R.layout.registration_layout, container, false);
		activity = (MainActivity) getActivity();
		changeFonts();
		setButtonListeners();
		return registrationLayout;
	}

	@Override
	public void onDestroyView() {
		if (MainActivity.toast != null)
			MainActivity.toast.cancel();
		super.onDestroyView();
	}

	private void changeFonts() {
		TextView usernameTextView = (TextView) registrationLayout
				.findViewById(R.id.registration_username_label);
		EditText usernameEditText = (EditText) registrationLayout
				.findViewById(R.id.registration_username_textbox);
		TextView emailTextView = (TextView) registrationLayout
				.findViewById(R.id.registration_email_label);
		EditText emailEditText = (EditText) registrationLayout
				.findViewById(R.id.registration_email_textbox);
		TextView emailInfoTextview = (TextView) registrationLayout
				.findViewById(R.id.registration_email_info);
		Button startButton = (Button) registrationLayout
				.findViewById(R.id.registration_start);
		TextView existingAccountTextView = (TextView) registrationLayout
				.findViewById(R.id.registration_existing_account);

		usernameTextView.setTypeface(MainActivity.GABRIOLA_FONT);
		usernameEditText.setTypeface(MainActivity.GABRIOLA_FONT);
		emailTextView.setTypeface(MainActivity.GABRIOLA_FONT);
		emailEditText.setTypeface(MainActivity.GABRIOLA_FONT);
		emailInfoTextview.setTypeface(MainActivity.GABRIOLA_FONT);
		startButton.setTypeface(MainActivity.GABRIOLA_FONT);
		existingAccountTextView.setTypeface(MainActivity.GABRIOLA_FONT);
	}

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
					activity.showMessage("Username must be filled!");
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
				activity.getFragmentManager()
						.beginTransaction()
						.replace(R.id.fragment_container,
								activity.getLoginFragment(), "Login").commit();
			}
		});
	}

	private void registrationPhase1() {
		RegistrationPhase1Poster p = new RegistrationPhase1Poster(
				new RegistrationDTO(username, email)) {

			@Override
			public void handleResponseObject(RegistrationResponse responseObject) {
				if (responseObject.getResult() == ServerResult.OK)
					showPhase1ConfirmationDialog();
				else if (responseObject.getResult() == ServerResult.USERNAME_IN_USE)
					activity.showMessage("Username is already in use!");
				else
					activity.showMessage("Unknown error!");
			}
		};
		p.retrieveObject();
	}

	private void showPhase1ConfirmationDialog() {
		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				final Dialog confirmationDialog = new Dialog(activity);
				confirmationDialog
						.requestWindowFeature(Window.FEATURE_NO_TITLE);
				confirmationDialog.setContentView(R.layout.dialog_layout);

				TextView questionTextView = (TextView) confirmationDialog
						.findViewById(R.id.dialog_question_label);
				Button yesButton = (Button) confirmationDialog
						.findViewById(R.id.dialog_yes);
				Button noButton = (Button) confirmationDialog
						.findViewById(R.id.dialog_no);

				questionTextView.setText("Are you sure you want to choose '"
						+ username + "' as your name?");
				questionTextView.setTypeface(MainActivity.GABRIOLA_FONT);
				yesButton.setTypeface(MainActivity.GABRIOLA_FONT);
				noButton.setTypeface(MainActivity.GABRIOLA_FONT);

				yesButton.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						activity.choosingHomeProvince = true;
						if (activity.getActionBar().getSelectedTab().getTag()
								.equals("Map")) {
							activity.getFragmentManager()
									.beginTransaction()
									.replace(R.id.fragment_container,
											activity.getMapFragment(), "Map")
									.commit();
						} else {
							activity.getActionBar()
									.setSelectedNavigationItem(0);
						}
						confirmationDialog.dismiss();
					}
				});

				noButton.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						confirmationDialog.dismiss();
					}
				});

				confirmationDialog.show();
			}
		});
	}

	public void showPhase2ConfirmationDialog(final double homeLat,
			final double homeLong) {
		Log.d("DEBUG", "true");
		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				final Dialog confirmationDialog = new Dialog(activity);
				confirmationDialog
						.requestWindowFeature(Window.FEATURE_NO_TITLE);
				confirmationDialog.setContentView(R.layout.dialog_layout);

				TextView questionTextView = (TextView) confirmationDialog
						.findViewById(R.id.dialog_question_label);
				Button yesButton = (Button) confirmationDialog
						.findViewById(R.id.dialog_yes);
				Button noButton = (Button) confirmationDialog
						.findViewById(R.id.dialog_no);

				questionTextView
						.setText("Do you want to make this your home province?");
				questionTextView.setTypeface(MainActivity.GABRIOLA_FONT);
				yesButton.setTypeface(MainActivity.GABRIOLA_FONT);
				noButton.setTypeface(MainActivity.GABRIOLA_FONT);

				yesButton.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						TextView chooseHomeLabel = (TextView) activity
								.findViewById(R.id.choose_home_label);
						chooseHomeLabel.setVisibility(View.INVISIBLE);
						Button setHomeButton = (Button) activity
								.findViewById(R.id.set_home_current);
						setHomeButton.setVisibility(View.INVISIBLE);
						activity.choosingHomeProvince = false;

						// rounds to the current province's center coordinates
						double homeProvinceLat = Math.round(homeLat * 2000) / 2000;
						double homeProvinceLong = Math.round(homeLong * 1000) / 1000;

						registrationPhase2(homeProvinceLat, homeProvinceLong);
						confirmationDialog.dismiss();
					}
				});

				noButton.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						confirmationDialog.dismiss();
					}
				});
				confirmationDialog.show();
			}
		});

	}

	private void registrationPhase2(double homeLat, double homeLong) {
		RegistrationPhase2Poster p2 = new RegistrationPhase2Poster(
				new RegistrationDTO(username, email, homeLat, homeLong)) {

			@Override
			public void handleResponseObject(RegistrationResponse responseObject) {
				ServerResult result = responseObject.getResult();
				String SID = responseObject.getValue();
				int userId = responseObject.getId();
				if (result == ServerResult.OK) {
					SharedPreferences sharedPref = activity
							.getPreferences(Context.MODE_PRIVATE);
					SharedPreferences.Editor editor = sharedPref.edit();
					editor.putString("SID", SID);
					editor.putInt("userId", userId);
					editor.commit();
					activity.updatePlayerInfo();
					activity.showMessage("User created!");
				} else if (result == ServerResult.USERNAME_IN_USE) {
					activity.showMessage("Username is already in use!");
				} else {
					activity.showMessage("Unknown error!");
				}

			}
		};
		p2.retrieveObject();
	}

}
