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
import ee.bmagrupp.georivals.mobile.R;
import ee.bmagrupp.georivals.mobile.ui.MainActivity;

public class LoginFragment extends Fragment {
	// non-static immutable variables (local constants)
	private MainActivity activity;
	private RelativeLayout loginLayout;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		loginLayout = (RelativeLayout) inflater.inflate(R.layout.login_layout,
				container, false);
		activity = (MainActivity) getActivity();
		MainActivity.changeFonts(loginLayout);
		setButtonListeners();
		return loginLayout;
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
		Button startButton = (Button) loginLayout
				.findViewById(R.id.login_start);
		Button sendKeyButton = (Button) loginLayout
				.findViewById(R.id.login_send_key);

		startButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				EditText keyEditText = (EditText) loginLayout
						.findViewById(R.id.login_key_textbox);
				String loginKey = keyEditText.getText().toString();
				if (loginKey.length() == 16 || loginKey.equals("test")
						|| loginKey.equals("johnny")) {
					requestLogin(loginKey);
				} else {
					activity.showToastMessage(activity
							.getString(R.string.error_invalid_key));
				}
			}
		});

		sendKeyButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				EditText emailEditText = (EditText) loginLayout
						.findViewById(R.id.login_email_textbox);
				String email = emailEditText.getText().toString();
				if (isValidEmail(email)) {
					requestSendKey(email);
				} else {
					activity.showToastMessage(activity
							.getString(R.string.error_invalid_email));
				}
			}
		});
	}

	/**
	 * Does a login request with the given login key.
	 * 
	 * @param loginKey
	 */

	private void requestLogin(String loginKey) {
		// test users
		if (loginKey.equals("test"))
			activity.setUserData("BPUYYOU62flwiWJe", 1);
		else if (loginKey.equals("johnny"))
			activity.setUserData("UJ86IpW5xK8ZZH7t", 5);
		activity.setToMapTab();
	}

	/**
	 * Does a 'send login key' request with the given email.
	 * 
	 * @param email
	 */

	private void requestSendKey(String email) {

	}

	/**
	 * Checks if the given email has a valid email pattern.
	 * 
	 * @param email
	 */

	private boolean isValidEmail(CharSequence email) {
		if (email == null)
			return false;
		return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
	}

}