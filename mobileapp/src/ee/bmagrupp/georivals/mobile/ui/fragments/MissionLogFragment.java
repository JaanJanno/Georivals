package ee.bmagrupp.georivals.mobile.ui.fragments;

import java.util.List;

import ee.bmagrupp.georivals.mobile.R;
import ee.bmagrupp.georivals.mobile.core.communications.loaders.battle.history.BattleHistoryUILoader;
import ee.bmagrupp.georivals.mobile.core.communications.loaders.units.MovableUnitsUILoader;
import ee.bmagrupp.georivals.mobile.core.communications.loaders.units.movement.CancelMovementUILoader;
import ee.bmagrupp.georivals.mobile.core.communications.loaders.units.movement.MovementViewUILoader;
import ee.bmagrupp.georivals.mobile.models.battle.history.BattleHistoryDTO;
import ee.bmagrupp.georivals.mobile.models.movement.MovementSelectionViewDTO;
import ee.bmagrupp.georivals.mobile.models.movement.MovementViewDTO;
import ee.bmagrupp.georivals.mobile.ui.MainActivity;
import ee.bmagrupp.georivals.mobile.ui.adapters.BattleHistoryAdapter;
import ee.bmagrupp.georivals.mobile.ui.adapters.MovementOverviewAdapter;
import ee.bmagrupp.georivals.mobile.ui.adapters.MovementSelectionAdapter;
import ee.bmagrupp.georivals.mobile.ui.widgets.TabItem;
import android.app.Fragment;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;

@SuppressWarnings("unused")
public class MissionLogFragment extends Fragment implements TabItem {
	// non-static immutable variables (local constants)
	private MainActivity activity;
	private LinearLayout missionLogLayout;
	private final int tabNameId = R.string.mission_log;
	private final int tabIconId = R.drawable.log_icon;

	private List<BattleHistoryDTO> battleHistoryList;
	private List<MovementViewDTO> movementOverviewList;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		missionLogLayout = (LinearLayout) inflater.inflate(
				R.layout.mission_log_layout, container, false);
		activity = (MainActivity) getActivity();
		requestBattleHistoryData();
		requestMovementOverviewData();
		MainActivity.changeFonts(missionLogLayout);
		return missionLogLayout;
	}

	/**
	 * Requests a list of the player's battles from the server. If successful,
	 * it populates the battle history list view.
	 */

	private void requestBattleHistoryData() {
		BattleHistoryUILoader battleHistoryListLoader = new BattleHistoryUILoader(
				MainActivity.sid, activity) {

			@Override
			public void handleResponseObjectInUI(
					List<BattleHistoryDTO> responseList) {
				if (responseList != null
						&& MainActivity.MISSION_LOG_FRAGMENT.isVisible()) {
					battleHistoryList = responseList;
					populateBattleHistory();
				} else if (responseList == null) {
					activity.showToastMessage(activity
							.getString(R.string.error_retrieval_fail));
				}
			}

			@Override
			public void handleResponseObjectInBackground(
					List<BattleHistoryDTO> responseObject) {

			}
		};
		battleHistoryListLoader.retrieveResponse();
	}

	/**
	 * Populates the battle history list view.
	 */

	private void populateBattleHistory() {
		ExpandableListView listview = (ExpandableListView) missionLogLayout
				.findViewById(R.id.battle_history_listview);
		if (battleHistoryList.size() > 0) {
			listview.setChildDivider(getResources().getDrawable(
					R.color.dark_brown));
			BattleHistoryAdapter adapter = new BattleHistoryAdapter(activity,
					battleHistoryList);
			listview.setAdapter(adapter);
		} else {
			activity.replaceWithTextView(missionLogLayout, listview,
					activity.getString(R.string.no_battles));
		}
	}

	/**
	 * Requests a list of the player's current unit movements from the server.
	 * If successful, it populates the movement overview list view.
	 */

	private void requestMovementOverviewData() {
		MovementViewUILoader movementViewListLoader = new MovementViewUILoader(
				MainActivity.sid, activity) {

			@Override
			public void handleResponseListInUI(
					List<MovementViewDTO> responseList) {
				if (responseList != null
						&& MainActivity.MISSION_LOG_FRAGMENT.isVisible()) {
					movementOverviewList = responseList;
					populateMovementOverview();
				} else if (responseList == null) {
					activity.showToastMessage(activity
							.getString(R.string.error_retrieval_fail));
				}
			}

			@Override
			public void handleResponseListInBackground(
					List<MovementViewDTO> responseList) {

			}
		};
		movementViewListLoader.retrieveResponse();
	}

	/**
	 * Populates the movement overview list view.
	 */

	private void populateMovementOverview() {
		ExpandableListView listview = (ExpandableListView) missionLogLayout
				.findViewById(R.id.movement_overview_listview);
		if (movementOverviewList.size() > 0) {
			listview.setChildDivider(getResources().getDrawable(
					R.color.dark_brown));
			MovementOverviewAdapter adapter = new MovementOverviewAdapter(
					activity, movementOverviewList);
			listview.setAdapter(adapter);
		} else {
			activity.replaceWithTextView(missionLogLayout, listview,
					activity.getString(R.string.no_movements));
		}
	}

	/**
	 * Sends a movement cancel request to the server. If succesful, it refreshes
	 * the layout.
	 * 
	 * @param movementId
	 */

	public void requestCancelMovement(int movementId) {
		CancelMovementUILoader cancelMovementLoader = new CancelMovementUILoader(
				MainActivity.sid, movementId, activity) {

			@Override
			public void handleResponseObjectInUI(MovementViewDTO responseObject) {
				if (responseObject != null) {
					activity.refreshCurrentFragment();
				} else {
					activity.showToastMessage(activity
							.getString(R.string.error_connection_fail));
				}
			}

			@Override
			public void handleResponseObjectInBackground(
					MovementViewDTO responseObject) {

			}
		};
		cancelMovementLoader.retrieveResponse();
	}

	@Override
	public void onDestroyView() {
		activity.cancelToastMessage();
		super.onDestroyView();
	}

	@Override
	public int getTabIconId() {
		return tabIconId;
	}

	@Override
	public int getTabNameId() {
		return tabNameId;
	}

}
