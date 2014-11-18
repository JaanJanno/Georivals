package ee.bmagrupp.georivals.mobile.ui.fragments;

import com.google.android.gms.maps.model.LatLng;

import ee.bmagrupp.georivals.mobile.R;
import ee.bmagrupp.georivals.mobile.core.communications.loaders.registration.RegistrationPhase1Poster;
import ee.bmagrupp.georivals.mobile.core.communications.loaders.registration.RegistrationPhase2Poster;
import ee.bmagrupp.georivals.mobile.models.ServerResponse;
import ee.bmagrupp.georivals.mobile.models.ServerResult;
import ee.bmagrupp.georivals.mobile.models.registration.RegistrationDTO;
import ee.bmagrupp.georivals.mobile.ui.MainActivity;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

@SuppressWarnings("deprecation")
public class RegistrationFragment extends Fragment {
	private RelativeLayout registrationLayout;
	private MainActivity activity;
	private Resources resources;
	private String username;
	private String email;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		registrationLayout = (RelativeLayout) inflater.inflate(
				R.layout.registration_layout, container, false);
		MainActivity.changeFonts(registrationLayout);
		activity = (MainActivity) getActivity();
		resources = activity.getResources();
		setButtonListeners();
		return registrationLayout;
	}

	@Override
	public void onDestroyView() {
		if (MainActivity.toast != null)
			MainActivity.toast.cancel();
		super.onDestroyView();
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
					activity.showMessage(resources
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
				activity.getFragmentManager()
						.beginTransaction()
						.replace(R.id.fragment_container,
								MainActivity.LOGIN_FRAGMENT, "Login").commit();
			}
		});
	}

	private void registrationPhase1() {
		RegistrationPhase1Poster p = new RegistrationPhase1Poster(
				new RegistrationDTO(username, email)) {

			@Override
			public void handleResponseObject(ServerResponse responseObject) {
				if (responseObject.getResult() == ServerResult.OK)
					showPhase1ConfirmationDialog();
				else if (responseObject.getResult() == ServerResult.USERNAME_IN_USE)
					activity.showMessage(resources
							.getString(R.string.error_username_taken));
				else
					activity.showMessage(resources
							.getString(R.string.error_unknown));
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

				questionTextView.setText(resources
						.getString(R.string.confirmation_username1)
						+ username
						+ resources.getString(R.string.confirmation_username2));
				questionTextView.setTypeface(MainActivity.GABRIOLA_FONT);
				yesButton.setTypeface(MainActivity.GABRIOLA_FONT);
				noButton.setTypeface(MainActivity.GABRIOLA_FONT);

				yesButton.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						MainActivity.choosingHomeProvince = true;
						if (activity.getActionBar().getSelectedTab().getTag()
								.equals("Map")) {
							activity.getFragmentManager()
									.beginTransaction()
									.replace(R.id.fragment_container,
											MainActivity.MAP_FRAGMENT, "Map")
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

	public void showPhase2ConfirmationDialog(final LatLng provinceLatLng) {
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

				questionTextView.setText(resources
						.getString(R.string.confirmation_province));
				questionTextView.setTypeface(MainActivity.GABRIOLA_FONT);
				yesButton.setTypeface(MainActivity.GABRIOLA_FONT);
				noButton.setTypeface(MainActivity.GABRIOLA_FONT);

				yesButton.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						registrationPhase2(provinceLatLng);
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

	private void registrationPhase2(LatLng homeLatLng) {
		RegistrationPhase2Poster p2 = new RegistrationPhase2Poster(
				new RegistrationDTO(username, email, homeLatLng.latitude,
						homeLatLng.longitude)) {

			@Override
			public void handleResponseObject(ServerResponse responseObject) {
				ServerResult result = responseObject.getResult();
				String SID = responseObject.getValue();
				int userId = responseObject.getId();
				if (result == ServerResult.OK) {
					TextView chooseHomeLabel = (TextView) activity
							.findViewById(R.id.choose_home_label);
					chooseHomeLabel.setVisibility(View.INVISIBLE);
					Button setHomeButton = (Button) activity
							.findViewById(R.id.set_home_current);
					setHomeButton.setVisibility(View.INVISIBLE);
					MainActivity.choosingHomeProvince = false;

					SharedPreferences sharedPref = activity
							.getPreferences(Context.MODE_PRIVATE);
					SharedPreferences.Editor editor = sharedPref.edit();
					editor.putString("sid", SID);
					editor.putInt("userId", userId);
					editor.commit();
					activity.updatePlayerInfo();
					activity.showMessage(resources
							.getString(R.string.user_created));
				} else if (result == ServerResult.USERNAME_IN_USE) {
					activity.showMessage(resources
							.getString(R.string.error_username_taken));
				} else {
					activity.showMessage(resources
							.getString(R.string.error_unknown));
				}

			}
		};
		p2.retrieveObject();
	}

}
