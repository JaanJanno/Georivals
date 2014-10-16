package ee.bmagrupp.aardejaht.ui;

import ee.bmagrupp.aardejaht.R;
import ee.bmagrupp.aardejaht.core.communications.Constants;
import ee.bmagrupp.aardejaht.core.communications.highscore.ProfileEntryLoader;
import ee.bmagrupp.aardejaht.models.ProfileEntry;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class ProfileActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.profile_layout);

		ProfileEntryLoader l = new ProfileEntryLoader(Constants.PROFILE, 1) {
			@Override
			public void handleResponseObject(final ProfileEntry profile) {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						populateLayout(profile);
					}
				});
			}
		};
		l.retrieveProfileEntry();
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
		} else if (id == R.id.action_highscore) {
			Intent highscoreIntent = new Intent(this, HighScoreActivity.class);
			startActivity(highscoreIntent);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void populateLayout(ProfileEntry profile) {
		String username = profile.getUsername();
		String email = profile.getEmail();
		int totalUnits = profile.getTotalUnits();
		int ownedProvinces = profile.getOwnedProvinces();

		TextView usernameTextview = (TextView) findViewById(R.id.profile_username);
		TextView emailTextview = (TextView) findViewById(R.id.profile_email);
		TextView overallTimeTextview = (TextView) findViewById(R.id.profile_overall_time);
		TextView totalUnitsTextview = (TextView) findViewById(R.id.profile_total_units);
		TextView averageUnitsTextview = (TextView) findViewById(R.id.profile_average_units);
		TextView provincesTextview = (TextView) findViewById(R.id.profile_provinces);

		usernameTextview.setText(username);
		emailTextview.setText(email);
		overallTimeTextview.setText("5 day(s), 1 hour(s)");
		totalUnitsTextview.setText(Integer.toString(totalUnits));
		averageUnitsTextview.setText(Integer.toString(totalUnits
				/ ownedProvinces));
		provincesTextview.setText(Integer.toString(ownedProvinces));

	}
}
