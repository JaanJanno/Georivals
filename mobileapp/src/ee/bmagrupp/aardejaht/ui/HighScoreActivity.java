package ee.bmagrupp.aardejaht.ui;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import ee.bmagrupp.aardejaht.R;
import ee.bmagrupp.aardejaht.core.communications.highscore.HighScoreListLoader;
import ee.bmagrupp.aardejaht.models.HighScoreEntry;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

public class HighScoreActivity extends Activity {
	private static List<HighScoreEntry> playerList;
	private HighScoreAdapter adapter;
	private Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.highscore_layout);
		context = getApplicationContext();

		HighScoreListLoader l = new HighScoreListLoader(
				ee.bmagrupp.aardejaht.core.communications.Constants.HIGHSCORE) {

			public void handleResponseList(final List<HighScoreEntry> list) {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						sortEntries(list, "averageUnits");
						if (playerList != null) {
							ListView listview = (ListView) findViewById(R.id.highscore_listView);
							adapter = new HighScoreAdapter(context, playerList);
							listview.setAdapter(adapter);
						} else {
							MapActivity
									.showMessage(context,
											"Failed to retrieve the highscore list from the server.");
						}
					}
				});
			}
		};
		l.retrieveHighScoreEntries();

	}

	private void sortEntries(List<HighScoreEntry> list, final String sortBy) {
		if (list != null) {
			Collections.sort(list, new Comparator<HighScoreEntry>() {
				@Override
				public int compare(final HighScoreEntry entry1,
						final HighScoreEntry entry2) {
					double averageUnits1 = entry1.getAverageUnits();
					double averageUnits2 = entry2.getAverageUnits();
					Integer territoriesOwned1 = Integer.valueOf(entry1
							.getTerritoriesOwned());
					Integer territoriesOwned2 = Integer.valueOf(entry2
							.getTerritoriesOwned());
					if (sortBy.equals("averageUnits")) {
						if (averageUnits1 < averageUnits2)
							return 1;
						else if (averageUnits1 > averageUnits2)
							return -1;
						else
							return territoriesOwned2
									.compareTo(territoriesOwned1);
					} else {
						if (territoriesOwned1 < territoriesOwned2)
							return 1;
						else if (territoriesOwned1 > territoriesOwned2)
							return -1;
						else
							return Double.compare(averageUnits2, averageUnits1);
					}
				}
			});
			playerList = list;
			if (adapter != null)
				adapter.notifyDataSetChanged();
		}
	}

	public void sortByUnits(View v) {
		sortEntries(playerList, "averageUnits");
	}

	public void sortByTerritories(View v) {
		sortEntries(playerList, "territoriesOwned");
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
		} else if (id == R.id.action_profile) {
			Intent profileIntent = new Intent(this, ProfileActivity.class);
			startActivity(profileIntent);
			return true;
		} 
		return super.onOptionsItemSelected(item);
	}
}