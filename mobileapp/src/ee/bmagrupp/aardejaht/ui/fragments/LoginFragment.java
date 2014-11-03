package ee.bmagrupp.aardejaht.ui.fragments;

import ee.bmagrupp.aardejaht.R;
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

public class LoginFragment extends Fragment implements OnClickListener {
	private RelativeLayout loginLayout;
	private MainActivity activity;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		loginLayout = (RelativeLayout) inflater.inflate(R.layout.login_layout,
				container, false);
		activity = (MainActivity) getActivity();
		changeFonts();
		return loginLayout;
	}

	@Override
	public void onDestroyView() {
		if (MainActivity.toast != null)
			MainActivity.toast.cancel();
		super.onDestroyView();
	}

	@Override
	public void onClick(View v) {
		String buttonText = ((Button) v).getText().toString();
		if (buttonText
				.equals(activity.getResources().getString(R.string.start))) {
			EditText keyEditText = (EditText) loginLayout
					.findViewById(R.id.login_key_textbox);
			String loginKey = keyEditText.getText().toString();
			if (loginKey.length() == 16 || loginKey.equals("test")) {
				loginRequest(loginKey);
			} else {
				MainActivity.showMessage(activity, "Invalid login key!");
			}
		}

		else if (buttonText.equals(activity.getResources().getString(
				R.string.login_send_key))) {
			EditText emailEditText = (EditText) loginLayout
					.findViewById(R.id.login_email_textbox);
			String email = emailEditText.getText().toString();
			if (isValidEmail(email)) {
				sendKeyRequest(email);
			} else {
				MainActivity.showMessage(activity, "Invalid email!");
			}
		}

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

		Typeface font = Typeface.createFromAsset(activity.getAssets(),
				"fonts/Gabriola.ttf");

		keyTextView.setTypeface(font);
		keyEditText.setTypeface(font);
		startButton.setTypeface(font);
		emailTextView.setTypeface(font);
		emailEditText.setTypeface(font);
		sendKeyButton.setTypeface(font);

		startButton.setOnClickListener(this);
		sendKeyButton.setOnClickListener(this);
	}

	private void loginRequest(String loginKey) {
		if (loginKey.equals("test")) {
			SharedPreferences sharedPref = activity
					.getPreferences(Context.MODE_PRIVATE);
			SharedPreferences.Editor editor = sharedPref.edit();
			editor.putString("SID", loginKey);
			editor.putInt("userId", 1);
			editor.commit();
			activity.userId = 1;
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