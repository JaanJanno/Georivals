package ee.bmagrupp.georivals.mobile.ui.fragments;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;
import ee.bmagrupp.georivals.mobile.R;
import ee.bmagrupp.georivals.mobile.core.communications.loaders.units.MovableUnitsUILoader;
import ee.bmagrupp.georivals.mobile.core.communications.loaders.units.movement.CreateMovementUILoader;
import ee.bmagrupp.georivals.mobile.models.ServerResult;
import ee.bmagrupp.georivals.mobile.models.movement.BeginMovementDTO;
import ee.bmagrupp.georivals.mobile.models.movement.BeginMovementResponse;
import ee.bmagrupp.georivals.mobile.models.movement.MovementSelectionViewDTO;
import ee.bmagrupp.georivals.mobile.models.movement.MovementType;
import ee.bmagrupp.georivals.mobile.ui.MainActivity;
import ee.bmagrupp.georivals.mobile.ui.adapters.MovementSelectionAdapter;

public class MovementSelectionFragment extends Fragment {
	// non-static immutable variables (local constants)
	private static MainActivity activity;
	private LinearLayout movementSelectionLayout;

	// non-static mutable variables
	private List<MovementSelectionViewDTO> movableUnitsList;
	private int totalUnitCount;
	private int maxUnitCount;
	private int[] selectedUnitCountList;
	private MovementType movementType;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		movementSelectionLayout = (LinearLayout) inflater.inflate(
				R.layout.movement_selection_layout, container, false);
		activity = (MainActivity) getActivity();
		requestMyProvincesData();
		MainActivity.changeFonts(movementSelectionLayout);
		return movementSelectionLayout;
	}

	@Override
	public void onDestroyView() {
		activity.cancelToastMessage();
		super.onDestroyView();
	}

	/**
	 * Requests a list of the player's provinces from the server. If successful,
	 * it populates the layout.
	 */

	private void requestMyProvincesData() {
		MovableUnitsUILoader movableUnitsListLoader = new MovableUnitsUILoader(
				MainActivity.sid, ProvinceFragment.province.getLatitude(),
				ProvinceFragment.province.getLongitude(), activity) {

			@Override
			public void handleResponseInUI(
					List<MovementSelectionViewDTO> responseList) {
				if (responseList != null) {
					movableUnitsList = responseList;
					selectedUnitCountList = new int[movableUnitsList.size()];
					populateLayout();
				} else {
					activity.showToastMessage(activity
							.getString(R.string.error_retrieval_fail));
				}
			}

			@Override
			public void handleResponseInBackground(
					List<MovementSelectionViewDTO> responseList) {

			}
		};
		movableUnitsListLoader.retrieveResponse();
	}

	/**
	 * Populates the fragment's layout with more views.
	 */
	private void populateLayout() {
		sortListEntries();
		setListView();
		setTypeDependables();
		updateTotalUnitCount(0);
		setSendUnitsButton();
	}

	/**
	 * Sets movement type dependable variables and views.
	 */

	private void setTypeDependables() {
		totalUnitCount = 0;
		TextView titleTextView = (TextView) movementSelectionLayout
				.findViewById(R.id.movement_selection_title_header);
		if (movementType == MovementType.ATTACK) {
			titleTextView.setText(activity.getString(R.string.attack)
					+ ProvinceFragment.province.getProvinceName());
		} else {
			maxUnitCount = 100 - ProvinceFragment.province.getUnitSize();
			titleTextView.setText(activity
					.getString(R.string.units_transfer_to)
					+ ProvinceFragment.province.getProvinceName());
		}
	}

	/**
	 * Sets up the movement selection list view.
	 */

	private void setListView() {
		ExpandableListView listview = (ExpandableListView) movementSelectionLayout
				.findViewById(R.id.movement_selection_listview);
		listview.setChildDivider(getResources().getDrawable(R.color.dark_brown));
		MovementSelectionAdapter adapter = new MovementSelectionAdapter(
				activity, movableUnitsList);
		listview.setAdapter(adapter);
	}

	/**
	 * Sets the click listener for the 'send units' button.
	 */

	private void setSendUnitsButton() {
		Button sendButton = (Button) movementSelectionLayout
				.findViewById(R.id.movement_selection_button);
		sendButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				requestUnitMovement();
			}
		});
	}

	/**
	 * Sends a unit movement request to the server.
	 */

	private void requestUnitMovement() {
		List<BeginMovementDTO> movementList = createMovementList();

		CreateMovementUILoader l = new CreateMovementUILoader(movementList,
				MainActivity.sid, ProvinceFragment.province.getLatitude(),
				ProvinceFragment.province.getLongitude(), activity) {

			@Override
			public void handleResponseInUI(BeginMovementResponse responseObject) {
				if (responseObject.getResult() == ServerResult.OK) {
					activity.setToMissionLogTab();
				} else {
					activity.showToastMessage(activity
							.getString(R.string.error_unknown));
				}
			}

			@Override
			public void handleResponseInBackground(
					BeginMovementResponse responseObject) {

			}

		};
		l.retrieveResponse();
	}

	/**
	 * @return A list with all the unit movements the player wants to make.
	 */

	private List<BeginMovementDTO> createMovementList() {
		List<BeginMovementDTO> movementList = new ArrayList<>();
		int index = 0;
		for (int unitCount : selectedUnitCountList) {
			if (unitCount != 0)
				movementList.add(new BeginMovementDTO(movableUnitsList.get(
						index).getUnitId(), unitCount));
			index += 1;
		}
		return movementList;
	}

	/**
	 * Sorts the provinces list in descending order by the number of units.
	 */

	private void sortListEntries() {
		Collections.sort(movableUnitsList,
				new Comparator<MovementSelectionViewDTO>() {
					@Override
					public int compare(
							final MovementSelectionViewDTO province1,
							final MovementSelectionViewDTO province2) {
						double unitSize1 = province1.getUnitSize();
						double unitSize2 = province2.getUnitSize();
						if (unitSize1 < unitSize2)
							return 1;
						else
							return -1;
					}
				});
	}

	/**
	 * Updates the 'total units' counter variable and view.
	 * 
	 * @param change
	 */

	public void updateTotalUnitCount(int change) {
		totalUnitCount += change;
		TextView unitCount = (TextView) movementSelectionLayout
				.findViewById(R.id.movement_selection_unit_count);
		if (movementType == MovementType.ATTACK)
			unitCount.setText(activity.getString(R.string.units_total)
					+ totalUnitCount);
		else
			unitCount.setText(activity.getString(R.string.units_total)
					+ totalUnitCount + "/" + maxUnitCount);
	}

	/**
	 * @return The current total number of units to be sent.
	 */

	public int getTotalUnitCount() {
		return totalUnitCount;
	}

	/**
	 * Sets the current total number of units to be sent.
	 * 
	 * @param totalUnitCount
	 */

	public void setTotalUnitCount(int totalUnitCount) {
		this.totalUnitCount = totalUnitCount;
	}

	/**
	 * @return The maximum number of units that can be sent.
	 */

	public int getMaxUnitCount() {
		return maxUnitCount;
	}

	/**
	 * @param provinceIndex
	 *            The list index of a province.
	 * @return The current number of units that is set to be sent from the
	 *         province.
	 */

	public int getSelectedUnitCount(int provinceIndex) {
		return selectedUnitCountList[provinceIndex];
	}

	/**
	 * Sets the current number of units that is set to be sent from the
	 * province.
	 * 
	 * @param provinceIndex
	 *            The list index of a province.
	 * @param newCount
	 *            New number of units to be sent from the province.
	 */

	public void setSelectedUnitCount(int provinceIndex, int newCount) {
		this.selectedUnitCountList[provinceIndex] = newCount;
	}

	/**
	 * @return The type of unit movement (attack or transfer).
	 */

	public MovementType getMovementType() {
		return movementType;
	}

	/**
	 * Sets the type of unit movement.
	 */

	public void setMovementType(MovementType movementType) {
		this.movementType = movementType;
	}

}
