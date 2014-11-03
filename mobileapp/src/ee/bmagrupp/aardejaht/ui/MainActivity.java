package ee.bmagrupp.aardejaht.ui;

import ee.bmagrupp.aardejaht.R;
import ee.bmagrupp.aardejaht.ui.fragments.HighScoreFragment;
import ee.bmagrupp.aardejaht.ui.fragments.LocalFragment;
import ee.bmagrupp.aardejaht.ui.fragments.MapFragment;
import ee.bmagrupp.aardejaht.ui.fragments.MissionLogFragment;
import ee.bmagrupp.aardejaht.ui.fragments.MyPlacesFragment;
import ee.bmagrupp.aardejaht.ui.fragments.ProfileFragment;
import ee.bmagrupp.aardejaht.ui.listeners.TabListener;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ActionBar.Tab;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("InflateParams")
public class MainActivity extends Activity {
	public static Toast toast;
	public static final int LOGIN_REQUEST = 1;
	public static final int REGISTRATION_REQUEST = 2;
	public int userId;
	private MapFragment mapFragment;
	private MissionLogFragment missionLogFragment;
	private ProfileFragment profileFragment;
	private HighScoreFragment highscoreFragment;
	private MyPlacesFragment myPlacesFragment;
	private Activity activity;
	private Dialog registrationDialog;
	private Dialog loginDialog;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_layout);
		activity = this;
		userId = getUserId();
		createFragmentsAndTabs();
		addActionBarRibbon();
		getActionBar().setDisplayShowHomeEnabled(false);
		getActionBar().setDisplayShowTitleEnabled(false);
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

	private void createFragmentsAndTabs() {
		mapFragment = new MapFragment();
		missionLogFragment = new MissionLogFragment();
		profileFragment = new ProfileFragment();
		highscoreFragment = new HighScoreFragment();
		myPlacesFragment = new MyPlacesFragment();

		LocalFragment[] fragmentArray = new LocalFragment[] { mapFragment,
				missionLogFragment, profileFragment, highscoreFragment,
				myPlacesFragment };

		final ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		Typeface font = Typeface.createFromAsset(getAssets(),
				"fonts/Gabriola.ttf");

		for (LocalFragment fragment : fragmentArray) {
			String tabName = fragment.getTabName();
			int tabIconId = fragment.getTabIconId();

			RelativeLayout tabLayout = (RelativeLayout) LayoutInflater.from(
					this).inflate(R.layout.tab_item, null);

			TextView tabText = (TextView) tabLayout
					.findViewById(R.id.tab_item_text);

			tabText.setTypeface(font);
			tabText.setAllCaps(true);
			tabText.setText(tabName);

			ImageView tabIcon = (ImageView) tabLayout
					.findViewById(R.id.tab_item_icon);
			tabIcon.setImageResource(tabIconId);

			Tab tab = actionBar.newTab().setCustomView(tabLayout)
					.setTag(tabName)
					.setTabListener(new TabListener(this, fragment));
			actionBar.addTab(tab);
		}

		actionBar.setSelectedNavigationItem(0);
	}

	private void addActionBarRibbon() {
		int actionBarContainerId = getResources().getIdentifier(
				"action_bar_container", "id", "android");
		FrameLayout actionBarContainer = (FrameLayout) findViewById(actionBarContainerId);
		LayoutInflater inflater = getLayoutInflater();
		View ribbonView = inflater.inflate(R.layout.ribbon_layout, null);
		actionBarContainer.addView(ribbonView);
	}

	public void showRegistrationDialog() {
		registrationDialog = new Dialog(this);
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

		loginTextView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				registrationDialog.dismiss();
				showLoginDialog();
			}
		});

		registrationDialog.show();
	}

	private void showLoginDialog() {
		loginDialog = new Dialog(this);
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
			getActionBar().setSelectedNavigationItem(0);
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

	public void logout(View v) {
		SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPref.edit();
		editor.putString("SID", "");
		editor.putString("userName", "");
		editor.putInt("userId", 0);
		editor.commit();
		userId = 0;
		getActionBar().setSelectedNavigationItem(0);
	}

	public void sortByUnits(View v) {
		highscoreFragment.sortEntries("averageUnits");
	}

	public void sortByProvinces(View v) {
		highscoreFragment.sortEntries("provincesOwned");
	}

	public Dialog getRegistrationDialog() {
		return registrationDialog;
	}

	public Dialog getLoginDialog() {
		return loginDialog;
	}

}
