package ee.bmagrupp.georivals.mobile.ui.adapters;

import java.util.List;

import ee.bmagrupp.georivals.mobile.R;
import ee.bmagrupp.georivals.mobile.models.highscore.HighScoreEntry;
import ee.bmagrupp.georivals.mobile.ui.MainActivity;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class HighScoreAdapter extends ArrayAdapter<HighScoreEntry> {
	private final Context context;
	private final List<HighScoreEntry> entriesList;

	public HighScoreAdapter(Context context, List<HighScoreEntry> entriesList) {
		super(context, R.layout.highscore_item, entriesList);
		this.context = context;
		this.entriesList = entriesList;
	}

	@SuppressLint("ViewHolder")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		LinearLayout listItemView = (LinearLayout) inflater.inflate(
				R.layout.highscore_item, parent, false);

		MainActivity.changeFonts(listItemView);

		TextView rankView = (TextView) listItemView
				.findViewById(R.id.player_rank);
		TextView nameView = (TextView) listItemView
				.findViewById(R.id.player_name);
		TextView provincesView = (TextView) listItemView
				.findViewById(R.id.provinces_owned);
		TextView unitsView = (TextView) listItemView
				.findViewById(R.id.average_units);

		HighScoreEntry playerInfo = entriesList.get(position);

		rankView.setText((position + 1) + ".");
		nameView.setText(playerInfo.getUsername());
		provincesView.setText(String.valueOf(playerInfo.getProvincesOwned()));
		unitsView.setText(String.valueOf((double) Math.round(playerInfo
				.getAverageUnits() * 10) / 10));

		return listItemView;
	}

}