package ee.bmagrupp.georivals.mobile.ui.fragments;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import ee.bmagrupp.georivals.mobile.R;
import ee.bmagrupp.georivals.mobile.core.communications.loaders.units.MovableUnitsUILoader;
import ee.bmagrupp.georivals.mobile.core.communications.loaders.units.movement.CreateMovementUILoader;
import ee.bmagrupp.georivals.mobile.models.ServerResult;
import ee.bmagrupp.georivals.mobile.models.movement.BeginMovementDTO;
import ee.bmagrupp.georivals.mobile.models.movement.BeginMovementResponse;
import ee.bmagrupp.georivals.mobile.models.movement.MovementSelectionViewDTO;
import ee.bmagrupp.georivals.mobile.models.movement.MovementType;
import ee.bmagrupp.georivals.mobile.models.province.ProvinceType;
import ee.bmagrupp.georivals.mobile.ui.MainActivity;
import ee.bmagrupp.georivals.mobile.ui.adapters.MovementSelectionAdapter;
import android.app.Fragment;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MovementSelectionFragment extends Fragment {
	private List<MovementSelectionViewDTO> movableUnitsList;
	private MovementSelectionAdapter adapter;
	private MainActivity activity;
	private static Resources resources;
	private MovableUnitsUILoader movableUnitsListLoader;
	private static LinearLayout movementSelectionLayout;
	public static int totalUnitCount = 0;
	public static int maxUnitCount;
	public static int[] selectedUnitCountList;

	public static MovementType movementType;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		movementSelectionLayout = (LinearLayout) inflater.inflate(
				R.layout.movement_selection_layout, container, false);
		activity = (MainActivity) getActivity();
		resources = activity.getResources();
		requestMyProvincesData();
		MainActivity.changeFonts(movementSelectionLayout);
		return movementSelectionLayout;
	}

	@Override
	public void onDestroyView() {
		if (MainActivity.toast != null)
			MainActivity.toast.cancel();
		super.onDestroyView();
	}

	private void requestMyProvincesData() {
		movableUnitsListLoader = new MovableUnitsUILoader(MainActivity.sid,
				activity) {

			@Override
			public void handleResponseObjectInUI(
					List<MovementSelectionViewDTO> responseList) {
				if (responseList != null) {
					movableUnitsList = responseList;
					selectedUnitCountList = new int[movableUnitsList.size()];
					populateLayout();
				} else {
					activity.showMessage(resources
							.getString(R.string.error_retrieval_fail));
				}
			}

			@Override
			public void handleResponseObjectInBackground(
					List<MovementSelectionViewDTO> responseList) {

			}
		};
		movableUnitsListLoader.retrieveList();
	}

	private void populateLayout() {
		sortListEntries();
		setListView();
		setTypeDependables();
		setTotalUnitCounter();
		setSendUnitsButton();
	}

	private void setTypeDependables() {
		TextView titleTextView = (TextView) movementSelectionLayout
				.findViewById(R.id.movement_selection_title_header);
		if (movementType == MovementType.ATTACK) {
			maxUnitCount = 100;
			titleTextView.setText(resources.getString(R.string.attack) + " "
					+ ProvinceFragment.province.getProvinceName());
		} else {
			maxUnitCount = 100 - ProvinceFragment.province.getUnitSize();
			titleTextView.setText(resources
					.getString(R.string.units_transfer_to)
					+ " "
					+ ProvinceFragment.province.getProvinceName());
		}
	}

	private void setTotalUnitCounter() {
		TextView unitCountTextView = (TextView) movementSelectionLayout
				.findViewById(R.id.movement_selection_unit_count);
		unitCountTextView.setText(resources.getString(R.string.units_total)
				+ " 0/" + maxUnitCount);
	}

	private void setListView() {
		ExpandableListView listview = (ExpandableListView) movementSelectionLayout
				.findViewById(R.id.movement_selection_listview);
		listview.setChildDivider(getResources().getDrawable(R.color.dark_brown));
		adapter = new MovementSelectionAdapter(activity, movableUnitsList);
		listview.setAdapter(adapter);
	}

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

	private void requestUnitMovement() {
		List<BeginMovementDTO> movementList = createMovementList();

		CreateMovementUILoader l = new CreateMovementUILoader(movementList,
				MainActivity.sid, ProvinceFragment.province.getLatitude(),
				ProvinceFragment.province.getLongitude(), activity) {

			@SuppressWarnings("deprecation")
			@Override
			public void handleResponseObjectInUI(
					BeginMovementResponse responseObject) {
				if (responseObject.getResult() == ServerResult.OK) {
					activity.showMessage(resources
							.getString(R.string.units_total)
							+ " "
							+ responseObject.getArrivalTime());
					activity.getActionBar().setSelectedNavigationItem(0);
				} else {
					activity.showMessage(resources
							.getString(R.string.error_unknown));
				}
			}

			@Override
			public void handleResponseObjectInBackground(
					BeginMovementResponse responseObject) {

			}

		};
		l.retrieveObject();
	}

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

	private void sortListEntries() {
		if (movableUnitsList != null) {
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
			for (MovementSelectionViewDTO province : movableUnitsList) {
				if (province.getType() == ProvinceType.HOME) {
					movableUnitsList.remove(province);
					movableUnitsList.add(0, province);
					break;
				}
			}
		}
	}

	public static void updateTotalUnitCount(int change) {
		totalUnitCount += change;
		TextView unitCount = (TextView) movementSelectionLayout
				.findViewById(R.id.movement_selection_unit_count);
		unitCount.setText(resources.getString(R.string.units_total) + " "
				+ totalUnitCount + "/" + maxUnitCount);
	}
}
