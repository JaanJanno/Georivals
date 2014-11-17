package ee.bmagrupp.georivals.mobile.ui.fragments;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import ee.bmagrupp.georivals.mobile.R;
import ee.bmagrupp.georivals.mobile.core.communications.highscore.HighScoreListLoader;
import ee.bmagrupp.georivals.mobile.models.highscore.HighScoreEntry;
import ee.bmagrupp.georivals.mobile.ui.MainActivity;
import ee.bmagrupp.georivals.mobile.ui.adapters.HighScoreAdapter;
import ee.bmagrupp.georivals.mobile.ui.widgets.TabItem;
import android.app.Fragment;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

public class HighScoreFragment extends Fragment implements TabItem {
	private final int tabNameId;
	private final int tabIconId;

	private List<HighScoreEntry> playerList;
	private HighScoreAdapter adapter;
	private MainActivity activity;
	private Resources resources;
	private HighScoreListLoader highScoreListLoader;
	private LinearLayout highscoreLayout;

	public HighScoreFragment(int tabNameId, int tabIconId) {
		this.tabNameId = tabNameId;
		this.tabIconId = tabIconId;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		highscoreLayout = (LinearLayout) inflater.inflate(
				R.layout.highscore_layout, container, false);
		activity = (MainActivity) getActivity();
		resources = activity.getResources();
		requestHighScoreData();
		MainActivity.changeFonts(highscoreLayout);
		return highscoreLayout;
	}

	@Override
	public void onDestroyView() {
		if (MainActivity.toast != null)
			MainActivity.toast.cancel();
		super.onDestroyView();
	}

	private void requestHighScoreData() {
		highScoreListLoader = new HighScoreListLoader(
				ee.bmagrupp.georivals.mobile.core.communications.Constants.HIGHSCORE) {

			public void handleResponseList(final List<HighScoreEntry> list) {
				activity.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						if (list != null) {
							playerList = list;
							populateLayout();
						} else {
							activity.showMessage(resources
									.getString(R.string.error_retrieval_fail));
						}
					}
				});
			}
		};
		highScoreListLoader.retrieveHighScoreEntries();
	}

	private void populateLayout() {
		sortEntries("averageUnits");
		ListView listview = (ListView) highscoreLayout
				.findViewById(R.id.highscore_listView);
		adapter = new HighScoreAdapter(activity, playerList);
		listview.setAdapter(adapter);
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

	public LinearLayout getHighscoreLayout() {
		return highscoreLayout;
	}

	@Override
	public int getTabIconId() {
		return tabIconId;
	}

	@Override
	public int getTabNameId() {
		return tabNameId;
	}

}