package ee.bmagrupp.aardejaht.ui;

import ee.bmagrupp.aardejaht.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class LoginActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_layout);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public void login(View v) {
		EditText usernameEditText = (EditText) findViewById(R.id.login_username);
		EditText keyEditText = (EditText) findViewById(R.id.login_key);
		String username = usernameEditText.getText().toString();
		String loginKey = keyEditText.getText().toString();
		if (username.equals("") || loginKey.equals("")) {
			MainActivity.showMessage(this, "All fields must be filled!");
		} else {
			try {
				loginRequest(username, loginKey);
			} catch (NumberFormatException ex) {
				MainActivity.showMessage(this,
						"The key must contain only numbers.");
			}
		}
	}

	public void sendKey(View v) {
		LinearLayout recoveryLayout = new LinearLayout(this);
		recoveryLayout.setOrientation(LinearLayout.VERTICAL);

		final EditText usernameEditText = new EditText(this);
		final EditText emailEditText = new EditText(this);

		RelativeLayout.LayoutParams parameters = new RelativeLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		parameters.setMargins(50, 50, 50, 50);

		recoveryLayout.setLayoutParams(parameters);
		usernameEditText.setLayoutParams(parameters);
		emailEditText.setLayoutParams(parameters);

		usernameEditText.setHint("Username");
		emailEditText.setHint("Email");

		recoveryLayout.addView(usernameEditText);
		recoveryLayout.addView(emailEditText);

		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		alert.setTitle("Retrieve login key");
		alert.setView(recoveryLayout);

		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				String username = usernameEditText.getText().toString();
				String email = emailEditText.getText().toString();
				sendKeyRequest(username, email);
			}
		});

		alert.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						// do nothing
					}
				});

		alert.show();
	}

	private void sendKeyRequest(String username, String email) {

	}

	private void loginRequest(String username, String loginKey) {
		if (username.equals("test") && loginKey.equals("test")) {
			SharedPreferences sharedPref = getSharedPreferences("prefs",
					Context.MODE_PRIVATE);
			SharedPreferences.Editor editor = sharedPref.edit();
			editor.putString("userName", username);
			editor.putInt("userId", 1);
			editor.commit();
			setResult(RESULT_OK, getIntent());
			finish();
		}
	}
}