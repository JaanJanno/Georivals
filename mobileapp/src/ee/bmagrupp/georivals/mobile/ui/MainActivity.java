package ee.bmagrupp.georivals.mobile.ui;

import ee.bmagrupp.georivals.mobile.R;
import ee.bmagrupp.georivals.mobile.core.location.LocationChangeUIHandler;
import ee.bmagrupp.georivals.mobile.core.location.service.LocationService;
import ee.bmagrupp.georivals.mobile.ui.fragments.HighScoreFragment;
import ee.bmagrupp.georivals.mobile.ui.fragments.LoginFragment;
import ee.bmagrupp.georivals.mobile.ui.fragments.MapFragment;
import ee.bmagrupp.georivals.mobile.ui.fragments.MissionLogFragment;
import ee.bmagrupp.georivals.mobile.ui.fragments.MovementSelectionFragment;
import ee.bmagrupp.georivals.mobile.ui.fragments.MyProvincesFragment;
import ee.bmagrupp.georivals.mobile.ui.fragments.ProfileFragment;
import ee.bmagrupp.georivals.mobile.ui.fragments.ProvinceFragment;
import ee.bmagrupp.georivals.mobile.ui.fragments.RegistrationFragment;
import ee.bmagrupp.georivals.mobile.ui.listeners.TabListener;
import ee.bmagrupp.georivals.mobile.ui.widgets.CustomDialog;
import ee.bmagrupp.georivals.mobile.ui.widgets.TabItem;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ActionBar.Tab;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

@SuppressWarnings("deprecation")
@SuppressLint("InflateParams")
public class MainActivity extends Activity {
	// static immutable variables (global constants)
	public static final MovementSelectionFragment MOVEMENT_SELECTION_FRAGMENT = new MovementSelectionFragment();
	public static final ProvinceFragment PROVINCE_FRAGMENT = new ProvinceFragment();
	public static final RegistrationFragment REGISTRATION_FRAGMENT = new RegistrationFragment();
	public static final LoginFragment LOGIN_FRAGMENT = new LoginFragment();
	public static final MapFragment MAP_FRAGMENT = new MapFragment();
	public static final MissionLogFragment MISSION_LOG_FRAGMENT = new MissionLogFragment();
	public static final ProfileFragment PROFILE_FRAGMENT = new ProfileFragment();
	public static final HighScoreFragment HIGH_SCORE_FRAGMENT = new HighScoreFragment();
	public static final MyProvincesFragment MY_PROVINCES_FRAGMENT = new MyProvincesFragment();
	public static Typeface GABRIOLA_FONT;
	public static final long UNIT_CLAIM_INTERVAL = 1000;
	public static final float UNIT_CLAIM_MIN_DISTANCE = 10;

	// non-static immutable variables (local constants)
	private final TabItem[] tabItemArray = new TabItem[] { MAP_FRAGMENT,
			MISSION_LOG_FRAGMENT, PROFILE_FRAGMENT, HIGH_SCORE_FRAGMENT,
			MY_PROVINCES_FRAGMENT };
	private final MainActivity activity = this;
	private ActionBar actionBar;

	// static mutable variables
	public static int userId;
	public static String sid = "";
	public static boolean choosingHomeProvince;
	private static boolean locationServiceEnabled = false;

	// non-static mutable variables
	private Fragment currentFragment;
	private Toast toast;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_layout);
		GABRIOLA_FONT = Typeface.createFromAsset(getAssets(),
				"fonts/Gabriola.ttf");
		actionBar = getActionBar();
		createTabs();
		addTabRibbon();
		hideViews();
		updateUserInfo();
		setUnitClaimHandler();
		setLocationService();
	}

	/**
	 * 
	 * @return Whether a service always remains running that claims units in
	 *         background.
	 */

	public static boolean isLocationServiceEnabled() {
		return locationServiceEnabled;
	}

	/**
	 * Sets whether a service remains always running that claims units in
	 * background.
	 * 
	 * @param locationServiceEnabled
	 *            Service remains running in background?
	 */

	public void setLocationServiceEnabled(boolean locationServiceEnabled) {
		MainActivity.locationServiceEnabled = locationServiceEnabled;
		setLocationService();
	}

	/*
	 * Starts the location service if enabled and stops it if disabled.
	 */

	private void setLocationService() {
		if (isLocationServiceEnabled()) {
			Intent service = new Intent(this, LocationService.class);
			startService(service);
		} else
			stopService(new Intent(this, LocationService.class));
	}

	/*
	 * Creates a listener that sends unit claim requests to server when the
	 * player has moved.
	 */

	private void setUnitClaimHandler() {
		LocationManager l = (LocationManager) getApplicationContext()
				.getSystemService(Context.LOCATION_SERVICE);
		l.requestLocationUpdates("gps", UNIT_CLAIM_INTERVAL,
				UNIT_CLAIM_MIN_DISTANCE, new LocationChangeUIHandler(this));
	}

	/**
	 * Creates and sets all action bar tabs.
	 */

	private void createTabs() {
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		for (TabItem tabItem : tabItemArray) {
			String tabName = getString(tabItem.getTabNameId());
			int tabIconId = tabItem.getTabIconId();

			RelativeLayout tabLayout = (RelativeLayout) LayoutInflater.from(
					this).inflate(R.layout.tab_item, null);

			TextView tabText = (TextView) tabLayout
					.findViewById(R.id.tab_item_text);

			tabText.setTypeface(GABRIOLA_FONT);
			tabText.setAllCaps(true);
			tabText.setText(tabName);

			ImageView tabIcon = (ImageView) tabLayout
					.findViewById(R.id.tab_item_icon);
			tabIcon.setImageResource(tabIconId);

			Tab tab = actionBar.newTab().setCustomView(tabLayout)
					.setTag(tabName)
					.setTabListener(new TabListener(this, tabItem));
			actionBar.addTab(tab);
		}

		setToMapTab();
	}

	/**
	 * Adds the ribbon view on top of the tab bar.
	 */

	private void addTabRibbon() {
		int actionBarContainerId = getResources().getIdentifier(
				"action_bar_container", "id", "android");
		FrameLayout actionBarContainer = (FrameLayout) findViewById(actionBarContainerId);
		LayoutInflater inflater = getLayoutInflater();
		View ribbonView = inflater.inflate(R.layout.ribbon_layout, null);
		actionBarContainer.addView(ribbonView);
	}

	/**
	 * Hides the action bar and 'set home' views.
	 */

	private void hideViews() {
		actionBar.setDisplayShowHomeEnabled(false);
		actionBar.setDisplayShowTitleEnabled(false);

		TextView chooseHomeLabel = (TextView) findViewById(R.id.choose_home_label);
		chooseHomeLabel.setVisibility(View.INVISIBLE);
		Button setHomeButton = (Button) findViewById(R.id.set_home_current);
		setHomeButton.setVisibility(View.INVISIBLE);
	}

	/**
	 * Sets the given SID and user id into shared preferences.
	 * 
	 * @param sid
	 * @param userId
	 */

	public void setUserData(String sid, int userId) {
		SharedPreferences sharedPref = PreferenceManager
				.getDefaultSharedPreferences(this);
		SharedPreferences.Editor editor = sharedPref.edit();
		editor.putString("sid", sid);
		editor.putInt("userId", userId);
		editor.commit();
		updateUserInfo();
	}

	/**
	 * Gets the SID and user id from shared preferences and saves them as static
	 * variables.
	 */

	public void updateUserInfo() {
		SharedPreferences sharedPref = PreferenceManager
				.getDefaultSharedPreferences(this);
		userId = sharedPref.getInt("userId", 0);
		sid = sharedPref.getString("sid", "");
	}

	/**
	 * Displays a toast message with the given message.
	 * 
	 * @param message
	 */

	public void showToastMessage(final String message) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				toast = Toast.makeText(activity, message, Toast.LENGTH_LONG);
				toast.show();
			}
		});
	}

	/**
	 * Cancels the currently displayed toast message.
	 */

	public void cancelToastMessage() {
		if (toast != null)
			toast.cancel();
	}

	/**
	 * Changes the font (typeface) of the given layout's children to Gabriola.
	 * 
	 * @param layout
	 */

	public static void changeFonts(ViewGroup layout) {
		for (int i = 0; i < layout.getChildCount(); i++) {
			View view = layout.getChildAt(i);
			if (view instanceof TextView)
				((TextView) view).setTypeface(MainActivity.GABRIOLA_FONT);
			else if (view instanceof EditText)
				((EditText) view).setTypeface(MainActivity.GABRIOLA_FONT);
			else if (view instanceof Button)
				((Button) view).setTypeface(MainActivity.GABRIOLA_FONT);
			else if (view instanceof ViewGroup)
				changeFonts((ViewGroup) view);
		}
	}

	@Override
	public void onBackPressed() {
		if (MAP_FRAGMENT.isVisible())
			showExitConfirmationDialog();
		else
			changeFragment(MainActivity.MAP_FRAGMENT, getString(R.string.map));
	}

	/**
	 * Sets up and displays an application exit confirmation dialog.
	 */

	private void showExitConfirmationDialog() {
		final CustomDialog exitConfirmationDialog = new CustomDialog(activity);
		exitConfirmationDialog
				.setMessage(getString(R.string.confirmation_exit));
		exitConfirmationDialog.hideInput();

		exitConfirmationDialog.setPositiveButton(new OnClickListener() {
			@Override
			public void onClick(View v) {
				exitConfirmationDialog.dismiss();
				moveTaskToBack(true);
			}
		});
		exitConfirmationDialog.show();
	}

	/**
	 * Changes the fragment in the fragment container to the given fragment.
	 * 
	 * @param fragment
	 * @param fragmentTag
	 */

	public void changeFragment(Fragment fragment, String fragmentTag) {
		if (currentFragment == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.fragment_container, fragment, fragmentTag)
					.commit();
		} else if (currentFragment == MAP_FRAGMENT) {
			getFragmentManager().beginTransaction().detach(currentFragment)
					.add(R.id.fragment_container, fragment, fragmentTag)
					.commit();
		} else if (fragment == MAP_FRAGMENT) {
			getFragmentManager().beginTransaction().remove(currentFragment)
					.attach(fragment).commit();
		} else {
			getFragmentManager().beginTransaction()
					.replace(R.id.fragment_container, fragment, fragmentTag)
					.commit();
		}
		currentFragment = fragment;
	}

	/**
	 * Reattaches the currently displayed fragment.
	 */

	public void refreshCurrentFragment() {
		getFragmentManager().beginTransaction().detach(currentFragment)
				.attach(currentFragment).commit();
	}

	/**
	 * @return The currently displayed fragment.
	 */

	public Fragment getCurrentFragment() {
		return currentFragment;
	}

	/**
	 * Sets the currently chosen tab to the map tab.
	 */

	public void setToMapTab() {
		this.getActionBar().setSelectedNavigationItem(0);
	}

}
