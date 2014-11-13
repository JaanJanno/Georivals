package ee.bmagrupp.georivals.mobile.ui;

import ee.bmagrupp.georivals.mobile.R;
import ee.bmagrupp.georivals.mobile.ui.fragments.HighScoreFragment;
import ee.bmagrupp.georivals.mobile.ui.fragments.LoginFragment;
import ee.bmagrupp.georivals.mobile.ui.fragments.MapFragment;
import ee.bmagrupp.georivals.mobile.ui.fragments.MissionLogFragment;
import ee.bmagrupp.georivals.mobile.ui.fragments.MyPlacesFragment;
import ee.bmagrupp.georivals.mobile.ui.fragments.ProfileFragment;
import ee.bmagrupp.georivals.mobile.ui.fragments.RegistrationFragment;
import ee.bmagrupp.georivals.mobile.ui.listeners.TabListener;
import ee.bmagrupp.georivals.mobile.ui.widgets.TabItem;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ActionBar.Tab;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("InflateParams")
public class MainActivity extends Activity {
	public static final RegistrationFragment REGISTRATION_FRAGMENT = new RegistrationFragment();
	public static final LoginFragment LOGIN_FRAGMENT = new LoginFragment();
	public static MapFragment MAP_FRAGMENT;
	public static MissionLogFragment MISSION_LOG_FRAGMENT;
	public static ProfileFragment PROFILE_FRAGMENT;
	public static HighScoreFragment HIGH_SCORE_FRAGMENT;
	public static MyPlacesFragment MY_PLACES_FRAGMENT;
	public static Typeface GABRIOLA_FONT;
	private final Activity activity = this;
	private Resources resources;
	private ActionBar actionBar;

	public static Toast toast;
	public static int userId;
	public static String sid = "";
	public static boolean choosingHomeProvince;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_layout);
		GABRIOLA_FONT = Typeface.createFromAsset(getAssets(),
				"fonts/Gabriola.ttf");
		resources = activity.getResources();
		actionBar = getActionBar();

		createTabs();
		addTabRibbon();
		hideViews();
		updatePlayerInfo();
	}

	@Override
	public void onStop() {
		if ("google_sdk".equals(Build.PRODUCT)) // if emulator is used
			System.exit(0);
		super.onStop();
	}

	private void createTabs() {
		MAP_FRAGMENT = new MapFragment(resources.getString(R.string.map),
				R.drawable.places_icon);
		MISSION_LOG_FRAGMENT = new MissionLogFragment(
				resources.getString(R.string.mission_log), R.drawable.log_icon);
		PROFILE_FRAGMENT = new ProfileFragment(
				resources.getString(R.string.profile), R.drawable.profile_icon);
		HIGH_SCORE_FRAGMENT = new HighScoreFragment(
				resources.getString(R.string.highscores),
				R.drawable.leaders_icon);
		MY_PLACES_FRAGMENT = new MyPlacesFragment(
				resources.getString(R.string.my_places), R.drawable.places_icon);

		TabItem[] tabItemArray = new TabItem[] { MAP_FRAGMENT,
				MISSION_LOG_FRAGMENT, PROFILE_FRAGMENT, HIGH_SCORE_FRAGMENT,
				MY_PLACES_FRAGMENT };

		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		for (TabItem tabItem : tabItemArray) {
			String tabName = tabItem.getTabName();
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

		actionBar.setSelectedNavigationItem(0);
	}

	private void addTabRibbon() {
		int actionBarContainerId = getResources().getIdentifier(
				"action_bar_container", "id", "android");
		FrameLayout actionBarContainer = (FrameLayout) findViewById(actionBarContainerId);
		LayoutInflater inflater = getLayoutInflater();
		View ribbonView = inflater.inflate(R.layout.ribbon_layout, null);
		actionBarContainer.addView(ribbonView);
	}

	private void hideViews() {
		actionBar.setDisplayShowHomeEnabled(false);
		actionBar.setDisplayShowTitleEnabled(false);

		TextView chooseHomeLabel = (TextView) findViewById(R.id.choose_home_label);
		chooseHomeLabel.setVisibility(View.INVISIBLE);
		Button setHomeButton = (Button) findViewById(R.id.set_home_current);
		setHomeButton.setVisibility(View.INVISIBLE);
	}

	public void updatePlayerInfo() {
		SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
		userId = sharedPref.getInt("userId", 0);
		sid = sharedPref.getString("sid", "");
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

	public void logout(View v) {
		SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPref.edit();
		editor.putString("sid", "");
		editor.putString("userName", "");
		editor.putInt("userId", 0);
		editor.commit();
		updatePlayerInfo();
		actionBar.setSelectedNavigationItem(0);
	}

	public void sortByUnits(View v) {
		HIGH_SCORE_FRAGMENT.sortEntries("averageUnits");
	}

	public void sortByProvinces(View v) {
		HIGH_SCORE_FRAGMENT.sortEntries("provincesOwned");
	}

}
