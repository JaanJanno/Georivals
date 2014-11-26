package ee.bmagrupp.georivals.mobile.ui.adapters;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import ee.bmagrupp.georivals.mobile.R;
import ee.bmagrupp.georivals.mobile.models.movement.MovementSelectionViewDTO;
import ee.bmagrupp.georivals.mobile.models.movement.MovementType;
import ee.bmagrupp.georivals.mobile.models.province.ProvinceType;
import ee.bmagrupp.georivals.mobile.ui.MainActivity;

@SuppressLint("InflateParams")
public class MovementSelectionAdapter extends BaseExpandableListAdapter {
	private final Context context;
	private final List<MovementSelectionViewDTO> movableUnitsList;

	public MovementSelectionAdapter(Context context,
			List<MovementSelectionViewDTO> movableUnitsList) {
		this.context = context;
		this.movableUnitsList = movableUnitsList;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LinearLayout listItemView = (LinearLayout) inflater.inflate(
				R.layout.movement_selection_group, parent, false);
		MainActivity.changeFonts(listItemView);
		MovementSelectionViewDTO province = movableUnitsList.get(groupPosition);
		TextView provinceNameTextView = (TextView) listItemView
				.findViewById(R.id.movement_province_name);
		provinceNameTextView.setText(province.getProvinceName());

		return listItemView;
	}

	@Override
	public View getChildView(final int groupPosition, final int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LinearLayout listItemView = (LinearLayout) inflater.inflate(
				R.layout.movement_selection_child, parent, false);
		MainActivity.changeFonts(listItemView);
		MovementSelectionViewDTO province = movableUnitsList.get(groupPosition);

		setUnitSlider(listItemView, groupPosition, province);

		return listItemView;
	}

	/**
	 * Sets up the unit slider for the province's list view item.
	 * 
	 * @param listItemView
	 * @param listItemPosition
	 * @param province
	 */

	private void setUnitSlider(LinearLayout listItemView,
			final int listItemPosition, MovementSelectionViewDTO province) {
		final SeekBar unitSlider = (SeekBar) listItemView
				.findViewById(R.id.movement_unit_slider);
		unitSlider.setProgress(MainActivity.MOVEMENT_SELECTION_FRAGMENT
				.getSelectedUnitCount(listItemPosition));

		if (province.getType() == ProvinceType.HOME)
			unitSlider.setMax(province.getUnitSize());
		else
			unitSlider.setMax(province.getUnitSize() - 1);

		unitSlider.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {

			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {

			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				int progressChange = progress
						- MainActivity.MOVEMENT_SELECTION_FRAGMENT
								.getSelectedUnitCount(listItemPosition);
				int maxChange = MainActivity.MOVEMENT_SELECTION_FRAGMENT
						.getMaxUnitCount()
						- MainActivity.MOVEMENT_SELECTION_FRAGMENT
								.getTotalUnitCount();
				if (MainActivity.MOVEMENT_SELECTION_FRAGMENT.getMovementType() == MovementType.TRANSFER
						&& progressChange >= maxChange) {
					progressChange = maxChange;
					progress = MainActivity.MOVEMENT_SELECTION_FRAGMENT
							.getSelectedUnitCount(listItemPosition)
							+ progressChange;
					unitSlider.setProgress(progress);
				}
				LinearLayout parent = (LinearLayout) seekBar.getParent();
				TextView unitCountTextView = (TextView) parent
						.findViewById(R.id.movement_unit_count);
				unitCountTextView.setText(progress + "/" + unitSlider.getMax());
				MainActivity.MOVEMENT_SELECTION_FRAGMENT
						.updateTotalUnitCount(progressChange);
				MainActivity.MOVEMENT_SELECTION_FRAGMENT.setSelectedUnitCount(
						listItemPosition, progress);
			}
		});

		TextView unitCountTextView = (TextView) listItemView
				.findViewById(R.id.movement_unit_count);
		unitCountTextView.setText(MainActivity.MOVEMENT_SELECTION_FRAGMENT
				.getSelectedUnitCount(listItemPosition)
				+ "/"
				+ unitSlider.getMax());
	}

	@Override
	public int getGroupCount() {
		return movableUnitsList.size();
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return 1;
	}

	@Override
	public Object getGroup(int groupPosition) {
		return null;
	}

	@Override
	public Object getChild(int groupPosition, int childPosititon) {
		return null;
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return false;
	}

}