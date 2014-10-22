package ee.bmagrupp.aardejaht.ui;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import ee.bmagrupp.aardejaht.R;
import ee.bmagrupp.aardejaht.core.communications.highscore.HighScoreListLoader;
import ee.bmagrupp.aardejaht.models.HighScoreEntry;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;

public class HighScoreFragment extends Fragment {
	private static List<HighScoreEntry> playerList;
	private HighScoreAdapter adapter;
	private Activity activity;
	private HighScoreListLoader highScoreListLoader;
	private RelativeLayout highscoreLayout;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		highscoreLayout = (RelativeLayout) inflater.inflate(
				R.layout.highscore_layout, container, false);
		return highscoreLayout;
	}

	@Override
	public void onDestroyView() {
		if (MainActivity.toast != null)
			MainActivity.toast.cancel();
		super.onDestroyView();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (activity == null) {
			activity = getActivity();
			highScoreListLoader = new HighScoreListLoader(
					ee.bmagrupp.aardejaht.core.communications.Constants.HIGHSCORE) {

				public void handleResponseList(final List<HighScoreEntry> list) {
					activity.runOnUiThread(new Runnable() {
						@Override
						public void run() {
							playerList = list;
							sortEntries("averageUnits");
							if (playerList != null) {
								ListView listview = (ListView) highscoreLayout
										.findViewById(R.id.highscore_listView);
								adapter = new HighScoreAdapter(activity,
										playerList);
								listview.setAdapter(adapter);
							} else {
								MainActivity
										.showMessage(activity,
												"Failed to retrieve the highscore list from the server.");
							}
						}
					});
				}
			};
		}
		highScoreListLoader.retrieveHighScoreEntries();
	}

	public void sortEntries(final String sortBy) {
		if (playerList != null) {
			Collections.sort(playerList, new Comparator<HighScoreEntry>() {
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
			if (adapter != null)
				adapter.notifyDataSetChanged();
		}
	}

}