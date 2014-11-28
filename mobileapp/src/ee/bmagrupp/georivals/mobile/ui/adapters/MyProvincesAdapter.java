package ee.bmagrupp.georivals.mobile.ui.adapters;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import ee.bmagrupp.georivals.mobile.R;
import ee.bmagrupp.georivals.mobile.models.province.ProvinceDTO;
import ee.bmagrupp.georivals.mobile.models.province.ProvinceType;
import ee.bmagrupp.georivals.mobile.ui.MainActivity;

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

		RelativeLayout listItemView = (RelativeLayout) inflater.inflate(
				R.layout.my_provinces_item, parent, false);

		MainActivity.changeFonts(listItemView);

		TextView provinceNameView = (TextView) listItemView
				.findViewById(R.id.my_provinces_name);
		TextView unitsView = (TextView) listItemView
				.findViewById(R.id.my_provinces_units);
		TextView claimableUnitsView = (TextView) listItemView
				.findViewById(R.id.my_provinces_claimable_units);

		ProvinceDTO province = provinceList.get(position);

		provinceNameView.setText(province.getProvinceName());
		unitsView.setText(String.valueOf(province.getUnitSize()));
		claimableUnitsView.setText("(+" + province.getNewUnitSize() + ")");

		if (province.getType() != ProvinceType.HOME) {
			ImageView homeIcon = (ImageView) listItemView
					.findViewById(R.id.my_provinces_home_icon);
			homeIcon.setVisibility(View.GONE);
		}

		return listItemView;
	}

}
