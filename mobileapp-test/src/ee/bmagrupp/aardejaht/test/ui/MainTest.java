package ee.bmagrupp.aardejaht.test.ui;

import com.google.android.gms.maps.GoogleMap;

import ee.bmagrupp.aardejaht.R;
import ee.bmagrupp.aardejaht.ui.MainActivity;
import ee.bmagrupp.aardejaht.ui.fragments.HighScoreFragment;
import ee.bmagrupp.aardejaht.ui.fragments.MapFragment;
import ee.bmagrupp.aardejaht.ui.fragments.ProfileFragment;
import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.Instrumentation;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class MainTest extends ActivityInstrumentationTestCase2<MainActivity> {
	private Instrumentation instrumentation;
	private MainActivity activity;
	private LinearLayout fragment_container;
	private ActionBar actionBar;
	private FragmentManager fragmentManager;

	@SuppressWarnings("deprecation")
	public MainTest() {
		super("ee.bmagrupp.aardejaht", MainActivity.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		instrumentation = getInstrumentation();
		activity = this.getActivity();
		fragment_container = (LinearLayout) activity
				.findViewById(ee.bmagrupp.aardejaht.R.id.fragment_container);
		actionBar = this.activity.getActionBar();
		fragmentManager = this.activity.getFragmentManager();
	}

	public void testPreconditions() {
		assertNotNull(fragment_container);
		assertNotNull(actionBar);
		assertNotNull(fragmentManager);
	}

	public void testMapFragment() {
		// open map fragment
		activity.runOnUiThread(new Runnable() {
			public void run() {
				actionBar.setSelectedNavigationItem(0);
				assertEquals(0, actionBar.getSelectedNavigationIndex());
			}
		});
		instrumentation.waitForIdleSync();

		// test if map fragment is visible
		Fragment fragment = fragmentManager.findFragmentByTag("MapFragment");
		MapFragment mapFragment = (MapFragment) fragment;
		assertNotNull(mapFragment);
		assertTrue(mapFragment instanceof com.google.android.gms.maps.MapFragment);
		assertTrue(mapFragment.isVisible());

		// test different fields
		GoogleMap map = mapFragment.getMap();
		assertNotNull(map);
		assertNotNull(mapFragment.getLocationRequest());
		assertNotNull(mapFragment.getLocationManager());
		assertNotNull(mapFragment.getGoogleApiClient());
	}

	public void testProfileFragment() {
		// open profile fragment
		activity.runOnUiThread(new Runnable() {
			public void run() {
				actionBar.setSelectedNavigationItem(1);
				assertEquals(1, actionBar.getSelectedNavigationIndex());
			}
		});
		instrumentation.waitForIdleSync();

		// test if profile fragment is visible
		Fragment fragment = fragmentManager
				.findFragmentByTag("ProfileFragment");
		ProfileFragment profileFragment = (ProfileFragment) fragment;
		assertNotNull(profileFragment);
		assertTrue(profileFragment.isVisible());

		// test different fields
		assertNotNull(profileFragment.getProfileEntryLoader());
		assertNotNull(profileFragment.getProfileLayout());

		if (isNetworkAvailable()) {
			waitForField(profileFragment.getProfile(), 1000);

			TextView username = (TextView) activity
					.findViewById(R.id.profile_username);
			TextView email = (TextView) activity
					.findViewById(R.id.profile_email);
			TextView totalUnits = (TextView) activity
					.findViewById(R.id.profile_total_units);
			TextView averageUnits = (TextView) activity
					.findViewById(R.id.profile_average_units);
			TextView provinces = (TextView) activity
					.findViewById(R.id.profile_provinces);

			assertTrue(!username.getText().equals(""));
			assertTrue(!email.getText().equals(""));
			assertTrue(!totalUnits.getText().equals(""));
			assertTrue(!averageUnits.getText().equals(""));
			assertTrue(!provinces.getText().equals(""));
		}
	}

	public void testHighscoreFragment() {
		// open highscore fragment
		activity.runOnUiThread(new Runnable() {
			public void run() {
				actionBar.setSelectedNavigationItem(2);
				assertEquals(2, actionBar.getSelectedNavigationIndex());
			}
		});
		instrumentation.waitForIdleSync();

		// test if highscore fragment is visible
		Fragment fragment = fragmentManager
				.findFragmentByTag("HighscoreFragment");
		HighScoreFragment highScoreFragment = (HighScoreFragment) fragment;
		assertNotNull(highScoreFragment);
		assertTrue(highScoreFragment.isVisible());

		// test different fields
		assertNotNull(highScoreFragment.getHighScoreListLoader());
		assertNotNull(highScoreFragment.getHighscoreLayout());

		if (isNetworkAvailable()) {
			waitForField(highScoreFragment.getPlayerList(), 1000);
			assertNotNull(highScoreFragment.getPlayerList());
			assertNotNull(highScoreFragment.getAdapter());

			// test first item in highscore list and its subviews
			ListView listView = (ListView) activity
					.findViewById(R.id.highscore_listView);
			View firstListItem = listView.getChildAt(0);
			assertNotNull(firstListItem);

			TextView rankView = (TextView) firstListItem
					.findViewById(R.id.player_rank);
			TextView nameView = (TextView) firstListItem
					.findViewById(R.id.player_name);
			TextView provincesView = (TextView) firstListItem
					.findViewById(R.id.provinces_owned);
			TextView unitsView = (TextView) firstListItem
					.findViewById(R.id.average_units);

			assertNotNull(rankView);
			assertNotNull(nameView);
			assertNotNull(provincesView);
			assertNotNull(unitsView);

			// test sort buttons
			final ImageButton UnitSortButton = (ImageButton) activity
					.findViewById(R.id.unit_sort_button);
			final ImageButton TerritorySortButton = (ImageButton) activity
					.findViewById(R.id.provinces_sort_button);
			activity.runOnUiThread(new Runnable() {
				public void run() {
					UnitSortButton.performClick();
					TerritorySortButton.performClick();
				}
			});
		}

	}

	private boolean isNetworkAvailable() {
		ConnectivityManager connectivityManager = (ConnectivityManager) activity
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager
				.getActiveNetworkInfo();
		return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}

	private void waitForField(Object field, int maxWaitTime) {
		long time = System.currentTimeMillis();
		while (field == null && System.currentTimeMillis() < time + maxWaitTime) {
			// wait
		}
	}
}
