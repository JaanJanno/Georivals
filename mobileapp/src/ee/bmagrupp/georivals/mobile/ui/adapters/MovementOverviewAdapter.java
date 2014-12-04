package ee.bmagrupp.georivals.mobile.ui.adapters;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import ee.bmagrupp.georivals.mobile.R;
import ee.bmagrupp.georivals.mobile.models.movement.MovementViewDTO;
import ee.bmagrupp.georivals.mobile.ui.MainActivity;

public class MovementOverviewAdapter extends BaseExpandableListAdapter {
	private final Context context;
	private final List<MovementViewDTO> movementOverviewList;

	public MovementOverviewAdapter(Context context,
			List<MovementViewDTO> movementOverviewList) {
		this.context = context;
		this.movementOverviewList = movementOverviewList;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LinearLayout listItemView = (LinearLayout) inflater.inflate(
				R.layout.movement_overview_group, parent, false);
		MainActivity.changeFonts(listItemView);
		MovementViewDTO movement = movementOverviewList.get(groupPosition);

		TextView movementInfo = (TextView) listItemView
				.findViewById(R.id.movement_overview_info);

		if (movement.isAttack())
			movementInfo.setText(context.getString(R.string.movement_info_a1)
					+ movement.getDestinationName()
					+ context.getString(R.string.movement_info_a2)
					+ movement.getUnitSize()
					+ context.getString(R.string.movement_info_a3) + "\n"
					+ context.getString(R.string.movement_info_arrival)
					+ changeDateFormat(movement.getEndDate()));
		else
			movementInfo.setText(context.getString(R.string.movement_info_b1)
					+ movement.getUnitSize()
					+ context.getString(R.string.movement_info_b2)
					+ movement.getDestinationName() + ".\n"
					+ context.getString(R.string.movement_info_arrival)
					+ changeDateFormat(movement.getEndDate()));

		return listItemView;
	}

	/**
	 * @param date
	 * 
	 * @return A string of the given date where the format is changed to a more
	 *         suitable one.
	 */

	@SuppressLint("SimpleDateFormat")
	private String changeDateFormat(Date date) {
		SimpleDateFormat df = new SimpleDateFormat("dd'.' MMMM',' EEEE HH:mm");
		return df.format(date);
	}

	@Override
	public View getChildView(final int groupPosition, final int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LinearLayout listItemView = (LinearLayout) inflater.inflate(
				R.layout.movement_overview_child, parent, false);
		MainActivity.changeFonts(listItemView);
		final MovementViewDTO movement = movementOverviewList
				.get(groupPosition);

		listItemView.setGravity(Gravity.CENTER);

		Button cancelButton = (Button) listItemView
				.findViewById(R.id.movement_overview_cancel);
		cancelButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				MainActivity.MISSION_LOG_FRAGMENT
						.requestCancelMovement(movement.getMovementId());
			}
		});

		return listItemView;
	}

	@Override
	public int getGroupCount() {
		return movementOverviewList.size();
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return 0; // 0 - cancel movement disabled; 1 - cancel movement enabled
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
