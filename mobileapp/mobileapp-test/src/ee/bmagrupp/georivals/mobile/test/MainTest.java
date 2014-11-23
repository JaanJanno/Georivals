package ee.bmagrupp.georivals.mobile.test;

import ee.bmagrupp.georivals.mobile.R;
import ee.bmagrupp.georivals.mobile.ui.MainActivity;
import android.app.ActionBar;
import android.app.FragmentManager;
import android.app.Instrumentation;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

@SuppressWarnings("deprecation")
public class MainTest extends ActivityInstrumentationTestCase2<MainActivity> {

	private Instrumentation instrumentation;
	private MainActivity activity;
	private FrameLayout fragment_container;
	private ActionBar actionBar;
	private FragmentManager fragmentManager;
	public static boolean loginTested;

	public MainTest() {
		super(MainActivity.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		instrumentation = getInstrumentation();
		activity = getActivity();
		fragment_container = (FrameLayout) activity
				.findViewById(R.id.fragment_container);
		actionBar = activity.getActionBar();
		fragmentManager = activity.getFragmentManager();
		if (MainActivity.userId == 0)
			loginAndTest();
	}

	public void testPreconditions() {
		assertNotNull(fragment_container);
		assertNotNull(actionBar);
		assertNotNull(fragmentManager);
	}

	public void testLogin() {
		if (!loginTested) {
			MainActivity.userId = 0;
			loginAndTest();
		}
	}

	private void loginAndTest() {
		activity.runOnUiThread(new Runnable() {
			public void run() {
				actionBar.setSelectedNavigationItem(2);
			}
		});
		instrumentation.waitForIdleSync();
		final TextView loginTextView = (TextView) activity
				.findViewById(R.id.registration_existing_account);
		assertNotNull(loginTextView);
		activity.runOnUiThread(new Runnable() {
			public void run() {
				loginTextView.performClick();
			}
		});
		instrumentation.waitForIdleSync();
		activity.runOnUiThread(new Runnable() {
			public void run() {
				EditText loginEditText = (EditText) activity
						.findViewById(R.id.login_key_textbox);
				assertNotNull(loginEditText);
				loginEditText.setText("test");
				Button loginButton = (Button) activity
						.findViewById(R.id.login_start);
				assertNotNull(loginButton);
				loginButton.performClick();
			}
		});
		instrumentation.waitForIdleSync();
		assertTrue(MainActivity.userId != 0);
		loginTested = true;
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

		// test if map initialized
		waitForField(MainActivity.MAP_FRAGMENT.getMap(), 1000);
		assertNotNull(MainActivity.MAP_FRAGMENT.getMap());

		// test if map fragment is visible
		assertNotNull(MainActivity.MAP_FRAGMENT);
		assertTrue(MainActivity.MAP_FRAGMENT instanceof com.google.android.gms.maps.MapFragment);
		assertTrue(MainActivity.MAP_FRAGMENT.isVisible());
	}

	public void testProfileFragment() {
		// open profile fragment
		activity.runOnUiThread(new Runnable() {
			public void run() {
				actionBar.setSelectedNavigationItem(2);
				assertEquals(2, actionBar.getSelectedNavigationIndex());
			}
		});
		instrumentation.waitForIdleSync();

		// test if profile fragment is visible
		assertNotNull(MainActivity.PROFILE_FRAGMENT);
		assertTrue(MainActivity.PROFILE_FRAGMENT.isVisible());

		// test different fields
		assertNotNull(MainActivity.PROFILE_FRAGMENT.getProfileLayout());

		if (isNetworkAvailable()) {
			waitForField(MainActivity.PROFILE_FRAGMENT.getProfile(), 1000);

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
				actionBar.setSelectedNavigationItem(3);
				assertEquals(3, actionBar.getSelectedNavigationIndex());
			}
		});
		instrumentation.waitForIdleSync();

		// test if highscore fragment is visible
		assertNotNull(MainActivity.HIGH_SCORE_FRAGMENT);
		assertTrue(MainActivity.HIGH_SCORE_FRAGMENT.isVisible());

		// test different fields
		assertNotNull(MainActivity.HIGH_SCORE_FRAGMENT.getHighscoreLayout());

		if (isNetworkAvailable()) {
			waitForField(MainActivity.HIGH_SCORE_FRAGMENT.getPlayerList(), 1000);
			assertNotNull(MainActivity.HIGH_SCORE_FRAGMENT.getPlayerList());
			assertNotNull(MainActivity.HIGH_SCORE_FRAGMENT.getAdapter());

			// test first item in highscore list and its subviews
			ListView listView = (ListView) activity
					.findViewById(R.id.highscore_listview);
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
