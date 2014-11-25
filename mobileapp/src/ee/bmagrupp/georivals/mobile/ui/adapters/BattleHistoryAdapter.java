package ee.bmagrupp.georivals.mobile.ui.adapters;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import ee.bmagrupp.georivals.mobile.R;
import ee.bmagrupp.georivals.mobile.models.battle.history.BattleHistoryDTO;
import ee.bmagrupp.georivals.mobile.ui.MainActivity;

public class BattleHistoryAdapter extends BaseExpandableListAdapter {
	private final Context context;
	private final List<BattleHistoryDTO> battleHistoryList;

	public BattleHistoryAdapter(Context context,
			List<BattleHistoryDTO> battleHistoryList) {
		this.context = context;
		this.battleHistoryList = battleHistoryList;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LinearLayout listItemView = (LinearLayout) inflater.inflate(
				R.layout.battle_history_group, parent, false);
		MainActivity.changeFonts(listItemView);
		BattleHistoryDTO battle = battleHistoryList.get(groupPosition);

		TextView battleType = (TextView) listItemView
				.findViewById(R.id.battle_history_type);

		TextView battleResult = (TextView) listItemView
				.findViewById(R.id.battle_history_result);

		String attacking = battle.getProvinceName() + " -> "
				+ battle.getOtherPlayer();
		String defending = battle.getOtherPlayer() + " -> "
				+ battle.getProvinceName();
		String won = context.getString(R.string.won);
		String lost = context.getString(R.string.lost);

		switch (battle.getType()) {
		case ATTACK_PLAYER_WON:
			battleType.setText(attacking);
			battleResult.setText(won);
			break;
		case ATTACK_PLAYER_LOST:
			battleType.setText(attacking);
			battleResult.setText(lost);
			break;
		case DEFENCE_PLAYER_WON:
			battleType.setText(defending);
			battleResult.setText(won);
			break;
		case DEFENCE_PLAYER_LOST:
			battleType.setText(defending);
			battleResult.setText(lost);
			break;
		}

		return listItemView;
	}

	@Override
	public View getChildView(final int groupPosition, final int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LinearLayout listItemView = (LinearLayout) inflater.inflate(
				R.layout.battle_history_child, parent, false);
		MainActivity.changeFonts(listItemView);
		BattleHistoryDTO battle = battleHistoryList.get(groupPosition);

		TextView battleInfo = (TextView) listItemView
				.findViewById(R.id.battle_history_info);
		battleInfo.setText(getBattleInfo(battle.getProvinceName(),
				battle.getMyUnits(), battle.getMyLosses())
				+ "\n"
				+ getBattleInfo(battle.getOtherPlayer(),
						battle.getOtherUnits(), battle.getOtherLosses()));

		return listItemView;
	}

	/**
	 * @param provinceName
	 * @param units
	 * @param losses
	 * 
	 * @return A presentable string of the given battle attributes.
	 */

	private String getBattleInfo(String provinceName, int units, int losses) {
		return provinceName + context.getString(R.string.battle_info1) + units
				+ context.getString(R.string.battle_info2) + losses
				+ context.getString(R.string.battle_info3);
	}

	@Override
	public int getGroupCount() {
		return battleHistoryList.size();
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
