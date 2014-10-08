package ee.bmagrupp.aardejaht.ui;

import java.util.ArrayList;
import java.util.List;

import ee.bmagrupp.aardejaht.R;
import ee.bmagrupp.aardejaht.core.communications.highscore.HighScoreListLoader;
import ee.bmagrupp.aardejaht.models.HighScoreEntry;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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
						playerList = list;
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
		}
		return super.onOptionsItemSelected(item);
	}
}