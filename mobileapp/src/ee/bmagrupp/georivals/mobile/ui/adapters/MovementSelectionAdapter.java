package ee.bmagrupp.georivals.mobile.ui.adapters;

import java.util.List;

import ee.bmagrupp.georivals.mobile.R;
import ee.bmagrupp.georivals.mobile.models.movement.MovementSelectionViewDTO;
import ee.bmagrupp.georivals.mobile.ui.MainActivity;
import ee.bmagrupp.georivals.mobile.ui.fragments.MovementSelectionFragment;
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
		LinearLayout rowView = (LinearLayout) inflater.inflate(
				R.layout.movement_selection_group, parent, false);
		MovementSelectionViewDTO province = movableUnitsList.get(groupPosition);
		MainActivity.changeFonts(rowView);
		TextView provinceNameTextView = (TextView) rowView
				.findViewById(R.id.movement_province_name);
		provinceNameTextView.setText(province.getProvinceName());

		return rowView;
	}

	@Override
	public View getChildView(final int groupPosition, final int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LinearLayout rowView = (LinearLayout) inflater.inflate(
				R.layout.movement_selection_item, parent, false);
		MainActivity.changeFonts(rowView);
		MovementSelectionViewDTO province = movableUnitsList.get(groupPosition);

		setUnitSlider(rowView, groupPosition, province);

		return rowView;
	}

	private void setUnitSlider(LinearLayout rowView, final int groupPosition,
			MovementSelectionViewDTO province) {
		final SeekBar unitSlider = (SeekBar) rowView
				.findViewById(R.id.movement_unit_slider);
		unitSlider
				.setProgress(MovementSelectionFragment.selectedUnitCountList[groupPosition]);
		unitSlider.setMax(province.getUnitSize());
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
						- MovementSelectionFragment.selectedUnitCountList[groupPosition];
				int maxChange = MovementSelectionFragment.maxUnitCount
						- MovementSelectionFragment.totalUnitCount;
				if (progressChange >= maxChange) {
					progressChange = maxChange;
					progress = MovementSelectionFragment.selectedUnitCountList[groupPosition]
							+ progressChange;
					unitSlider.setProgress(progress);
				}
				LinearLayout parent = (LinearLayout) seekBar.getParent();
				TextView unitCountTextView = (TextView) parent
						.findViewById(R.id.movement_unit_count);
				unitCountTextView.setText(progress + "/" + unitSlider.getMax());
				MovementSelectionFragment.updateTotalUnitCount(progressChange);
				MovementSelectionFragment.selectedUnitCountList[groupPosition] = progress;
			}
		});

		TextView unitCountTextView = (TextView) rowView
				.findViewById(R.id.movement_unit_count);
		unitCountTextView
				.setText(MovementSelectionFragment.selectedUnitCountList[groupPosition]
						+ "/" + unitSlider.getMax());
	}

	@Override
	public Object getGroup(int groupPosition) {
		return null;
	}

	@Override
	public int getGroupCount() {
		return movableUnitsList.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public Object getChild(int groupPosition, int childPosititon) {
		return null;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return 1;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return false;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

}