package ee.bmagrupp.georivals.mobile.ui.adapters;

import java.util.ArrayList;
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
import android.widget.RelativeLayout;
import android.widget.TextView;

public class HighScoreAdapter extends ArrayAdapter<HighScoreEntry> {

	private final Context context;
	private final ArrayList<HighScoreEntry> playersArrayList;

	public HighScoreAdapter(Context context, List<HighScoreEntry> playerList) {
		super(context, R.layout.highscore_item, playerList);
		this.context = context;
		this.playersArrayList = (ArrayList<HighScoreEntry>) playerList;
	}

	@SuppressLint("ViewHolder")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		RelativeLayout rowView = (RelativeLayout) inflater.inflate(
				R.layout.highscore_item, parent, false);

		MainActivity.changeFonts(rowView);

		TextView rankView = (TextView) rowView.findViewById(R.id.player_rank);
		TextView nameView = (TextView) rowView.findViewById(R.id.player_name);
		TextView provincesView = (TextView) rowView
				.findViewById(R.id.provinces_owned);
		TextView unitsView = (TextView) rowView
				.findViewById(R.id.average_units);

		HighScoreEntry playerInfo = playersArrayList.get(position);

		rankView.setText((position + 1) + ".");
		nameView.setText(playerInfo.getUsername());
		provincesView.setText(String.valueOf(playerInfo.getProvincesOwned()));
		unitsView.setText(String.valueOf(playerInfo.getAverageUnits()));

		return rowView;
	}

}