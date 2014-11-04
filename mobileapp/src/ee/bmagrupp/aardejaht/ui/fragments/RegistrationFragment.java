package ee.bmagrupp.aardejaht.ui.fragments;

import ee.bmagrupp.aardejaht.R;
import ee.bmagrupp.aardejaht.core.communications.loaders.registration.RegistrationPhase1Poster;
import ee.bmagrupp.aardejaht.core.communications.loaders.registration.RegistrationPhase2Poster;
import ee.bmagrupp.aardejaht.models.RegistrationDTO;
import ee.bmagrupp.aardejaht.models.RegistrationResponse;
import ee.bmagrupp.aardejaht.models.ServerResult;
import ee.bmagrupp.aardejaht.ui.MainActivity;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class RegistrationFragment extends Fragment implements OnClickListener {
	private RelativeLayout registrationLayout;
	private MainActivity activity;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		registrationLayout = (RelativeLayout) inflater.inflate(
				R.layout.registration_layout, container, false);
		activity = (MainActivity) getActivity();
		changeFonts();
		return registrationLayout;
	}

	@Override
	public void onDestroyView() {
		if (MainActivity.toast != null)
			MainActivity.toast.cancel();
		super.onDestroyView();
	}

	@Override
	public void onClick(View v) {
		if (v instanceof Button) {
			EditText usernameEditText = (EditText) activity
					.findViewById(R.id.registration_username_textbox);
			EditText emailEditText = (EditText) activity
					.findViewById(R.id.registration_email_textbox);
			String username = usernameEditText.getText().toString();
			String email = emailEditText.getText().toString();
			if (username.equals("")) {
				MainActivity.showMessage(activity, "Username must be filled!");
			} else {
				registrationRequest(username, email);
			}
		} else if (v instanceof TextView) {
			activity.getFragmentManager()
					.beginTransaction()
					.replace(R.id.fragment_container,
							activity.getLoginFragment(), "Login").commit();
		}
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

		Typeface font = Typeface.createFromAsset(activity.getAssets(),
				"fonts/Gabriola.ttf");

		usernameTextView.setTypeface(font);
		usernameEditText.setTypeface(font);
		emailTextView.setTypeface(font);
		emailEditText.setTypeface(font);
		emailInfoTextview.setTypeface(font);
		startButton.setTypeface(font);
		existingAccountTextView.setTypeface(font);

		startButton.setOnClickListener(this);
		existingAccountTextView.setOnClickListener(this);
	}

	private void registrationRequest(final String username, final String email) {
		RegistrationPhase1Poster p = new RegistrationPhase1Poster(
				new RegistrationDTO(username, email)) {

			@Override
			public void handleResponseObject(RegistrationResponse responseObject) {
				System.out.println(responseObject);

				RegistrationPhase2Poster p2 = new RegistrationPhase2Poster(
						new RegistrationDTO(username, email, 10, 10)) {

					@Override
					public void handleResponseObject(
							RegistrationResponse responseObject) {
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
							activity.userId = userId;
							activity.getActionBar()
									.setSelectedNavigationItem(0);
						} else if (result == ServerResult.USERNAME_IN_USE) {
							MainActivity.showMessage(activity,
									"Username is already in use!");
						} else {
							MainActivity
									.showMessage(activity, "Unknown error!");
						}

					}
				};
				if (responseObject.getResult() == ServerResult.OK)
					p2.retrieveObject();
				else if (responseObject.getResult() == ServerResult.USERNAME_IN_USE) {
					MainActivity.showMessage(activity,
							"Username is already in use!");
				} else {
					MainActivity.showMessage(activity, "Unknown error!");
				}
			}
		};
		p.retrieveObject();
	}

}
