package ee.bmagrupp.aardejaht.ui;

import ee.bmagrupp.aardejaht.R;
import ee.bmagrupp.aardejaht.ui.fragments.HighScoreFragment;
import ee.bmagrupp.aardejaht.ui.fragments.MapFragment;
import ee.bmagrupp.aardejaht.ui.fragments.ProfileFragment;
import ee.bmagrupp.aardejaht.ui.listeners.TabListener;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ActionBar.Tab;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	public static Toast toast;
	public static final int LOGIN_REQUEST = 1;
	public static final int REGISTRATION_REQUEST = 2;
	public int userId;
	private MapFragment mapFragment;
	private ProfileFragment profileFragment;
	private HighScoreFragment highscoreFragment;
	private Activity activity;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_layout);
		activity = this;
		userId = getUserId();
		createFragments();
	}

	@Override
	public void onStop() {
		if ("google_sdk".equals(Build.PRODUCT)) // if emulator is used
			System.exit(0);
		super.onStop();
	}

	private int getUserId() {
		SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
		return sharedPref.getInt("userId", 0);
	}

	private void createFragments() {
		final ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		mapFragment = new MapFragment();
		Tab mapTab = actionBar
				.newTab()
				.setText("Map")
				.setTabListener(
						new TabListener(this, mapFragment, "MapFragment"));
		actionBar.addTab(mapTab);

		profileFragment = new ProfileFragment();
		Tab profileTab = actionBar
				.newTab()
				.setText("Profile")
				.setTabListener(
						new TabListener(this, profileFragment,
								"ProfileFragment"));
		actionBar.addTab(profileTab);

		highscoreFragment = new HighScoreFragment();
		Tab highscoreTab = actionBar
				.newTab()
				.setText("Highscores")
				.setTabListener(
						new TabListener(this, highscoreFragment,
								"HighscoreFragment"));
		actionBar.addTab(highscoreTab);

		actionBar.setSelectedNavigationItem(0);

	}

	public void showRegistrationDialog() {
		final Dialog registrationDialog = new Dialog(this);
		registrationDialog.setContentView(R.layout.register_layout);
		registrationDialog.setTitle("Registration");
		Button registerButton = (Button) registrationDialog
				.findViewById(R.id.button_register);
		TextView loginTextView = (TextView) registrationDialog
				.findViewById(R.id.existing_account);

		registerButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				EditText usernameEditText = (EditText) registrationDialog
						.findViewById(R.id.register_username);
				EditText emailEditText = (EditText) registrationDialog
						.findViewById(R.id.register_email);
				String username = usernameEditText.getText().toString();
				String email = emailEditText.getText().toString();
				if (username.equals("")) {
					MainActivity.showMessage(activity,
							"Username must be filled!");
				} else {
					registrationRequest(username, email);
				}
			}
		});

		loginTextView.setOnTouchListener(new OnTouchListener() {
			@SuppressLint("ClickableViewAccessibility")
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				registrationDialog.dismiss();
				showLoginDialog();
				return false;
			}
		});

		registrationDialog.show();
	}

	private void showLoginDialog() {
		final Dialog loginDialog = new Dialog(this);
		loginDialog.setContentView(R.layout.login_layout);
		loginDialog.setTitle("Log in");
		Button retrieveKeyButton = (Button) loginDialog
				.findViewById(R.id.button_retrieve_key);
		Button loginButton = (Button) loginDialog
				.findViewById(R.id.button_login_start);
		final EditText emailEditText = (EditText) loginDialog
				.findViewById(R.id.login_email);
		final EditText keyEditText = (EditText) loginDialog
				.findViewById(R.id.login_key);

		retrieveKeyButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String email = emailEditText.getText().toString();
				if (isValidEmail(email)) {
					sendKeyRequest(email);
				} else {
					MainActivity.showMessage(activity, "Invalid email!");
				}
			}
		});
		loginButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String loginKey = keyEditText.getText().toString();
				if (loginKey.length() == 16 || loginKey.equals("test")) {
					loginRequest(loginKey);
					loginDialog.dismiss();
				} else {
					MainActivity.showMessage(activity, "Invalid login key!");
				}
			}
		});
		loginDialog.show();
	}

	private void registrationRequest(String username, String email) {

	}

	private void sendKeyRequest(String email) {

	}

	private void loginRequest(String loginKey) {
		if (loginKey.equals("test")) {
			SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
			SharedPreferences.Editor editor = sharedPref.edit();
			editor.putString("SID", loginKey);
			editor.putInt("userId", 1);
			editor.commit();
			userId = 1;
		}
	}

	private boolean isValidEmail(CharSequence email) {
		if (email == null)
			return false;
		return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
	}

	public static void showMessage(Context context, String message) {
		toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
		toast.show();
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
		} else if (id == R.id.action_logout) {
			new AlertDialog.Builder(this)
					.setTitle("Log Out")
					.setMessage("Are you sure you want to log out?")
					.setPositiveButton("Yes",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
									SharedPreferences.Editor editor = sharedPref
											.edit();
									editor.putString("SID", "");
									editor.putString("userName", "");
									editor.putInt("userId", 0);
									editor.commit();
									userId = 0;
								}
							})
					.setNegativeButton("No",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									// do nothing
								}
							}).setIcon(android.R.drawable.ic_dialog_alert)
					.show();

			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public void sortByUnits(View v) {
		highscoreFragment.sortEntries("averageUnits");
	}

	public void sortByProvinces(View v) {
		highscoreFragment.sortEntries("provincesOwned");
	}

}
