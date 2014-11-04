package ee.bmagrupp.aardejaht.ui;

import ee.bmagrupp.aardejaht.R;
import ee.bmagrupp.aardejaht.ui.fragments.HighScoreFragment;
import ee.bmagrupp.aardejaht.ui.fragments.LoginFragment;
import ee.bmagrupp.aardejaht.ui.fragments.MapFragment;
import ee.bmagrupp.aardejaht.ui.fragments.MissionLogFragment;
import ee.bmagrupp.aardejaht.ui.fragments.MyPlacesFragment;
import ee.bmagrupp.aardejaht.ui.fragments.ProfileFragment;
import ee.bmagrupp.aardejaht.ui.fragments.RegistrationFragment;
import ee.bmagrupp.aardejaht.ui.listeners.TabListener;
import ee.bmagrupp.aardejaht.ui.widgets.TabItem;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ActionBar.Tab;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
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
	private RegistrationFragment registrationFragment;
	private LoginFragment loginFragment;
	private final Activity activity = this;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_layout);
		userId = getUserIdFromPrefs();
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

	private int getUserIdFromPrefs() {
		SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
		return sharedPref.getInt("userId", 0);
	}

	private void createFragmentsAndTabs() {
		mapFragment = new MapFragment();
		missionLogFragment = new MissionLogFragment();
		profileFragment = new ProfileFragment();
		highscoreFragment = new HighScoreFragment();
		myPlacesFragment = new MyPlacesFragment();
		registrationFragment = new RegistrationFragment();
		loginFragment = new LoginFragment();

		TabItem[] fragmentArray = new TabItem[] { mapFragment,
				missionLogFragment, profileFragment, highscoreFragment,
				myPlacesFragment };

		final ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		Typeface font = Typeface.createFromAsset(getAssets(),
				"fonts/Gabriola.ttf");

		for (TabItem fragment : fragmentArray) {
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

	public void showMessage(final String message) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				toast = Toast.makeText(activity, message, Toast.LENGTH_LONG);
				toast.show();
			}
		});
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

	public RegistrationFragment getRegistrationFragment() {
		return registrationFragment;
	}

	public LoginFragment getLoginFragment() {
		return loginFragment;
	}

}
