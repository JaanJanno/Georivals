package ee.bmagrupp.georivals.mobile.ui.fragments;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import ee.bmagrupp.georivals.mobile.R;
import ee.bmagrupp.georivals.mobile.core.communications.loaders.highscore.HighScoreListLoader;
import ee.bmagrupp.georivals.mobile.models.highscore.HighScoreEntry;
import ee.bmagrupp.georivals.mobile.ui.MainActivity;
import ee.bmagrupp.georivals.mobile.ui.adapters.HighScoreAdapter;
import ee.bmagrupp.georivals.mobile.ui.widgets.TabItem;

public class HighScoreFragment extends Fragment implements TabItem {
	// non-static immutable variables (local constants)
	private MainActivity activity;
	private final int tabNameId = R.string.highscores;
	private final int tabIconId = R.drawable.leaders_icon;
	private final int sortByUnits = 1;
	private final int sortByProvinces = 2;
	private LinearLayout highscoreLayout;

	// non-static mutable variables
	private List<HighScoreEntry> playerList;
	private HighScoreAdapter adapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		highscoreLayout = (LinearLayout) inflater.inflate(
				R.layout.highscore_layout, container, false);
		activity = (MainActivity) getActivity();
		requestHighScoreData();
		MainActivity.changeFonts(highscoreLayout);
		setButtonListeners();
		return highscoreLayout;
	}

	@Override
	public void onDestroyView() {
		activity.cancelToastMessage();
		super.onDestroyView();
	}

	/**
	 * Sets click listeners for the layout's buttons.
	 */

	private void setButtonListeners() {
		ImageButton unitSortButton = (ImageButton) highscoreLayout
				.findViewById(R.id.unit_sort_button);
		ImageButton provincesSortButton = (ImageButton) highscoreLayout
				.findViewById(R.id.provinces_sort_button);

		unitSortButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				sortEntries(sortByUnits);
			}
		});

		provincesSortButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				sortEntries(sortByProvinces);
			}
		});
	}

	/**
	 * Requests high scores data from the server. If successful, it populates
	 * the list view.
	 */

	private void requestHighScoreData() {
		HighScoreListLoader highScoreListLoader = new HighScoreListLoader() {

			public void handleResponse(final List<HighScoreEntry> list) {
				activity.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						if (list != null) {
							playerList = list;
							sortEntries(sortByUnits);
							populateLayout();
						} else {
							activity.showToastMessage(activity
									.getString(R.string.error_retrieval_fail));
						}
					}
				});
			}
		};
		highScoreListLoader.retrieveResponse();
	}

	/**
	 * Populates the high scores list view.
	 */

	private void populateLayout() {
		ListView listview = (ListView) highscoreLayout
				.findViewById(R.id.highscore_listview);
		adapter = new HighScoreAdapter(activity, playerList);
		listview.setAdapter(adapter);
	}

	/**
	 * Sorts the current high scores player list by the given type.
	 * 
	 * @param sortBy
	 */

	public void sortEntries(final int sortBy) {
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
					if (sortBy == sortByUnits) {
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

	/**
	 * @return The current high scores player list.
	 */

	public List<HighScoreEntry> getPlayerList() {
		return playerList;
	}

	/**
	 * @return The current high score list adapter.
	 */

	public HighScoreAdapter getAdapter() {
		return adapter;
	}

	/**
	 * @return The layout of the high scores fragment.
	 */

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