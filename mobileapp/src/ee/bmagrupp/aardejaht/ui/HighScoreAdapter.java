package ee.bmagrupp.aardejaht.ui;

import java.util.ArrayList;
import java.util.List;
import ee.bmagrupp.aardejaht.R;
import ee.bmagrupp.aardejaht.models.HighScoreEntry;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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

		View rowView = null;
		rowView = inflater.inflate(R.layout.highscore_item, parent, false);

		TextView rankView = (TextView) rowView.findViewById(R.id.player_rank);
		TextView nameView = (TextView) rowView.findViewById(R.id.player_name);
		TextView territoriesView = (TextView) rowView
				.findViewById(R.id.territories_owned);
		TextView unitsView = (TextView) rowView
				.findViewById(R.id.average_units);

		HighScoreEntry playerInfo = playersArrayList.get(position);

		rankView.setText((position+1) + ".");
		nameView.setText(playerInfo.getUsername());
		territoriesView
				.setText(String.valueOf(playerInfo.getTerritoriesOwned()));
		unitsView.setText(String.valueOf(playerInfo.getAverageUnits()));

		return rowView;
	}

}
