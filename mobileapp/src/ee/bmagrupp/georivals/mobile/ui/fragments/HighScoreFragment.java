package ee.bmagrupp.georivals.mobile.ui.fragments;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import ee.bmagrupp.aardejaht.R;
import ee.bmagrupp.georivals.mobile.core.communications.highscore.HighScoreListLoader;
import ee.bmagrupp.georivals.mobile.models.HighScoreEntry;
import ee.bmagrupp.georivals.mobile.ui.MainActivity;
import ee.bmagrupp.georivals.mobile.ui.adapters.HighScoreAdapter;
import ee.bmagrupp.georivals.mobile.ui.widgets.TabItem;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;

public class HighScoreFragment extends Fragment implements TabItem {
	private String tabName = "Highscores";
	private int tabIconId = R.drawable.leaders_icon;
	private List<HighScoreEntry> playerList;
	private HighScoreAdapter adapter;
	private MainActivity activity;
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
			activity = (MainActivity) getActivity();
			highScoreListLoader = new HighScoreListLoader(
					ee.bmagrupp.georivals.mobile.core.communications.Constants.HIGHSCORE) {

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
								activity.showMessage("Failed to retrieve the highscore list from the server.");
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
					Integer provincesOwned1 = Integer.valueOf(entry1
							.getProvincesOwned());
					Integer provincesOwned2 = Integer.valueOf(entry2
							.getProvincesOwned());
					if (sortBy.equals("averageUnits")) {
						if (averageUnits1 < averageUnits2)
							return 1;
						else if (averageUnits1 > averageUnits2)
							return -1;
						else
							return provincesOwned2.compareTo(provincesOwned1);
					} else {
						if (provincesOwned1 < provincesOwned2)
							return 1;
						else if (provincesOwned1 > provincesOwned2)
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

	public List<HighScoreEntry> getPlayerList() {
		return playerList;
	}

	public HighScoreAdapter getAdapter() {
		return adapter;
	}

	public HighScoreListLoader getHighScoreListLoader() {
		return highScoreListLoader;
	}

	public RelativeLayout getHighscoreLayout() {
		return highscoreLayout;
	}

	@Override
	public int getTabIconId() {
		return tabIconId;
	}

	@Override
	public String getTabName() {
		return tabName;
	}

}