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
import android.widget.RelativeLayout;
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

		RelativeLayout rowView = (RelativeLayout) inflater.inflate(
				R.layout.my_provinces_item, parent, false);

		MainActivity.changeFonts(rowView);

		TextView provinceNameView = (TextView) rowView
				.findViewById(R.id.my_province_name);
		TextView unitsView = (TextView) rowView
				.findViewById(R.id.my_provinces_units);

		ProvinceDTO province = provinceList.get(position);

		provinceNameView.setText(province.getProvinceName());
		unitsView.setText(String.valueOf(province.getUnitSize()));

		boolean isHomeProvince = checkIfHomeProvince(province, rowView);
		if (!isHomeProvince) {
			FrameLayout rankFrame = (FrameLayout) rowView
					.findViewById(R.id.province_rank);
			TextView rankTextView = new TextView(context);
			rankTextView.setText(String.valueOf(position));
			rankTextView.setTypeface(MainActivity.GABRIOLA_FONT);
			rankTextView.setTextSize(24);
			rankTextView.setGravity(Gravity.CENTER);
			rankFrame.addView(rankTextView);
		}

		return rowView;
	}

	private boolean checkIfHomeProvince(ProvinceDTO province,
			RelativeLayout rowView) {
		if (province.getType() == ProvinceType.HOME) {
			FrameLayout rankFrame = (FrameLayout) rowView
					.findViewById(R.id.province_rank);
			ImageView homeImage = new ImageView(context);
			homeImage.setImageResource(R.drawable.castle_icon);
			rankFrame.addView(homeImage);
			return true;
		}
		return false;
	}

}
