package ee.bmagrupp.aardejaht.ui;

import ee.bmagrupp.aardejaht.R;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ActionBar.Tab;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends Activity {
	static Toast toast;
	MapFragment mapFragment;
	ProfileFragment profileFragment;
	HighScoreFragment highscoreFragment;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_layout);
		createFragments();

	}

	@Override
	public void onStop() {
		if ("google_sdk".equals(Build.PRODUCT)) // if emulator is used
			System.exit(0);
		super.onStop();
	}

	private void createFragments() {
		final ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		mapFragment = new MapFragment();
		Tab mapTab = actionBar.newTab().setText("Map")
				.setTabListener(new TabListener(mapFragment, "MapFragment"));
		actionBar.addTab(mapTab);

		profileFragment = new ProfileFragment();
		Tab profileTab = actionBar
				.newTab()
				.setText("Profile")
				.setTabListener(
						new TabListener(profileFragment, "ProfileFragment"));
		actionBar.addTab(profileTab);

		highscoreFragment = new HighScoreFragment();
		Tab highscoreTab = actionBar
				.newTab()
				.setText("Highscores")
				.setTabListener(
						new TabListener(highscoreFragment, "HighscoreFragment"));
		actionBar.addTab(highscoreTab);

		actionBar.setSelectedNavigationItem(0);
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

	public void sortByUnits(View v) {
		highscoreFragment.sortEntries("averageUnits");
	}

	public void sortByTerritories(View v) {
		highscoreFragment.sortEntries("territoriesOwned");
	}
}
