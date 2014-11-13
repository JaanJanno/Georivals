package ee.bmagrupp.georivals.mobile.ui.fragments;

import ee.bmagrupp.georivals.mobile.R;
import ee.bmagrupp.georivals.mobile.ui.MainActivity;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class LoginFragment extends Fragment {
	private RelativeLayout loginLayout;
	private MainActivity activity;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		loginLayout = (RelativeLayout) inflater.inflate(R.layout.login_layout,
				container, false);
		activity = (MainActivity) getActivity();
		changeFonts();
		setButtonListeners();
		return loginLayout;
	}

	@Override
	public void onDestroyView() {
		if (MainActivity.toast != null)
			MainActivity.toast.cancel();
		super.onDestroyView();
	}

	private void changeFonts() {
		TextView keyTextView = (TextView) loginLayout
				.findViewById(R.id.login_key_label);
		EditText keyEditText = (EditText) loginLayout
				.findViewById(R.id.login_key_textbox);
		Button startButton = (Button) loginLayout
				.findViewById(R.id.login_start);
		TextView emailTextView = (TextView) loginLayout
				.findViewById(R.id.login_email_label);
		EditText emailEditText = (EditText) loginLayout
				.findViewById(R.id.login_email_textbox);
		Button sendKeyButton = (Button) loginLayout
				.findViewById(R.id.login_send_key);

		keyTextView.setTypeface(MainActivity.GABRIOLA_FONT);
		keyEditText.setTypeface(MainActivity.GABRIOLA_FONT);
		startButton.setTypeface(MainActivity.GABRIOLA_FONT);
		emailTextView.setTypeface(MainActivity.GABRIOLA_FONT);
		emailEditText.setTypeface(MainActivity.GABRIOLA_FONT);
		sendKeyButton.setTypeface(MainActivity.GABRIOLA_FONT);
	}

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
				if (loginKey.length() == 16 || loginKey.equals("test")) {
					loginRequest(loginKey);
				} else {
					activity.showMessage("Invalid login key!");
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
					sendKeyRequest(email);
				} else {
					activity.showMessage("Invalid email!");
				}
			}
		});
	}

	private void loginRequest(String loginKey) {
		if (loginKey.equals("test")) {
			SharedPreferences sharedPref = activity
					.getPreferences(Context.MODE_PRIVATE);
			SharedPreferences.Editor editor = sharedPref.edit();
			editor.putString("sid", loginKey);
			editor.putInt("userId", 1);
			editor.commit();
			activity.updatePlayerInfo();
			activity.getActionBar().setSelectedNavigationItem(0);
		}
	}

	private void sendKeyRequest(String email) {

	}

	private boolean isValidEmail(CharSequence email) {
		if (email == null)
			return false;
		return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
	}

}