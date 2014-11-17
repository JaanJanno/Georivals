package ee.bmagrupp.georivals.mobile.ui;

import ee.bmagrupp.georivals.mobile.R;
import ee.bmagrupp.georivals.mobile.ui.fragments.HighScoreFragment;
import ee.bmagrupp.georivals.mobile.ui.fragments.LoginFragment;
import ee.bmagrupp.georivals.mobile.ui.fragments.MapFragment;
import ee.bmagrupp.georivals.mobile.ui.fragments.MissionLogFragment;
import ee.bmagrupp.georivals.mobile.ui.fragments.MyProvincesFragment;
import ee.bmagrupp.georivals.mobile.ui.fragments.ProfileFragment;
import ee.bmagrupp.georivals.mobile.ui.fragments.ProvinceFragment;
import ee.bmagrupp.georivals.mobile.ui.fragments.RegistrationFragment;
import ee.bmagrupp.georivals.mobile.ui.listeners.TabListener;
import ee.bmagrupp.georivals.mobile.ui.widgets.TabItem;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
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
import android.view.Window;
import android.view.View.OnClickListener;
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
	public static final ProvinceFragment PROVINCE_FRAGMENT = new ProvinceFragment();
	public static final RegistrationFragment REGISTRATION_FRAGMENT = new RegistrationFragment();
	public static final LoginFragment LOGIN_FRAGMENT = new LoginFragment();
	public static final MapFragment MAP_FRAGMENT = new MapFragment(
			R.string.map, R.drawable.places_icon);
	public static final MissionLogFragment MISSION_LOG_FRAGMENT = new MissionLogFragment(
			R.string.mission_log, R.drawable.log_icon);;
	public static final ProfileFragment PROFILE_FRAGMENT = new ProfileFragment(
			R.string.profile, R.drawable.profile_icon);
	public static final HighScoreFragment HIGH_SCORE_FRAGMENT = new HighScoreFragment(
			R.string.highscores, R.drawable.leaders_icon);
	public static final MyProvincesFragment MY_PLACES_FRAGMENT = new MyProvincesFragment(
			R.string.my_provinces, R.drawable.places_icon);
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
		TabItem[] tabItemArray = new TabItem[] { MAP_FRAGMENT,
				MISSION_LOG_FRAGMENT, PROFILE_FRAGMENT, HIGH_SCORE_FRAGMENT,
				MY_PLACES_FRAGMENT };

		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		for (TabItem tabItem : tabItemArray) {
			String tabName = resources.getString(tabItem.getTabNameId());
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

	public void sortByUnits(View v) {
		HIGH_SCORE_FRAGMENT.sortEntries("averageUnits");
	}

	public void sortByProvinces(View v) {
		HIGH_SCORE_FRAGMENT.sortEntries("provincesOwned");
	}

	public static double roundDouble(double roundable, int precision) {
		double rounded = Math.round(roundable * precision);
		return rounded / precision;
	}

	@Override
	public void onBackPressed() {
		if (MAP_FRAGMENT.isVisible())
			showExitConfirmationDialog();
		else
			activity.getFragmentManager()
					.beginTransaction()
					.replace(R.id.fragment_container,
							MainActivity.MAP_FRAGMENT,
							resources.getString(R.string.map)).commit();
	}

	private void showExitConfirmationDialog() {
		final Dialog confirmationDialog = new Dialog(activity);
		confirmationDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		confirmationDialog.setContentView(R.layout.dialog_layout);

		TextView questionTextView = (TextView) confirmationDialog
				.findViewById(R.id.dialog_question_label);
		Button yesButton = (Button) confirmationDialog
				.findViewById(R.id.dialog_yes);
		Button noButton = (Button) confirmationDialog
				.findViewById(R.id.dialog_no);

		questionTextView.setText(resources
				.getString(R.string.confirmation_exit));
		questionTextView.setTypeface(MainActivity.GABRIOLA_FONT);
		yesButton.setTypeface(MainActivity.GABRIOLA_FONT);
		noButton.setTypeface(MainActivity.GABRIOLA_FONT);

		yesButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				confirmationDialog.dismiss();
				moveTaskToBack(true);
			}
		});

		noButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				confirmationDialog.dismiss();
			}
		});
		confirmationDialog.show();
	}

}
