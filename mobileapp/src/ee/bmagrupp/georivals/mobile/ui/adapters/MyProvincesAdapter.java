package ee.bmagrupp.georivals.mobile.ui.adapters;

import java.util.List;

import ee.bmagrupp.georivals.mobile.R;
import ee.bmagrupp.georivals.mobile.models.province.ProvinceDTO;
import ee.bmagrupp.georivals.mobile.models.province.ProvinceType;
import ee.bmagrupp.georivals.mobile.ui.MainActivity;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MyProvincesAdapter extends ArrayAdapter<ProvinceDTO> {
	private final Context context;
	private final List<ProvinceDTO> provinceList;

	public MyProvincesAdapter(Context context, List<ProvinceDTO> provinceList) {
		super(context, R.layout.my_provinces_item, provinceList);
		this.context = context;
		this.provinceList = provinceList;
	}

	@SuppressLint("ViewHolder")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		LinearLayout listItemView = (LinearLayout) inflater.inflate(
				R.layout.my_provinces_item, parent, false);

		MainActivity.changeFonts(listItemView);

		TextView provinceNameView = (TextView) listItemView
				.findViewById(R.id.my_province_name);
		TextView unitsView = (TextView) listItemView
				.findViewById(R.id.my_provinces_units);

		ProvinceDTO province = provinceList.get(position);

		provinceNameView.setText(province.getProvinceName());
		unitsView.setText(String.valueOf(province.getUnitSize()));

		if (province.getType() == ProvinceType.HOME) {
			setHomeIcon(listItemView);
		} else {
			setRank(listItemView, position);
		}

		return listItemView;
	}

	/**
	 * Sets the home icon as the province's rank.
	 * 
	 * @param listItemView
	 */

	private void setHomeIcon(LinearLayout listItemView) {
		FrameLayout rankFrame = (FrameLayout) listItemView
				.findViewById(R.id.province_rank);
		ImageView homeImage = new ImageView(context);
		homeImage.setImageResource(R.drawable.castle_icon);
		rankFrame.addView(homeImage);
	}

	/**
	 * // * Sets the province's rank.
	 * 
	 * @param listItemView
	 * @param rank
	 */

	private void setRank(LinearLayout listItemView, int rank) {
		FrameLayout rankFrame = (FrameLayout) listItemView
				.findViewById(R.id.province_rank);
		TextView rankTextView = new TextView(context);
		rankTextView.setText(rank + ".");
		rankTextView.setTypeface(MainActivity.GABRIOLA_FONT);
		rankTextView.setTextSize(18);
		rankTextView.setGravity(Gravity.CENTER);
		rankFrame.addView(rankTextView);
	}

}
