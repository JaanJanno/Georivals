package ee.bmagrupp.georivals.mobile.ui.fragments;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

import ee.bmagrupp.georivals.mobile.R;
import ee.bmagrupp.georivals.mobile.core.communications.loaders.province.ProvinceViewUILoader;
import ee.bmagrupp.georivals.mobile.core.communications.loaders.province.modify.ChangeHomeUILoader;
import ee.bmagrupp.georivals.mobile.core.communications.loaders.province.modify.RenameProvinceUILoader;
import ee.bmagrupp.georivals.mobile.models.ServerResponse;
import ee.bmagrupp.georivals.mobile.models.ServerResult;
import ee.bmagrupp.georivals.mobile.models.movement.MovementType;
import ee.bmagrupp.georivals.mobile.models.province.ProvinceDTO;
import ee.bmagrupp.georivals.mobile.models.province.ProvinceType;
import ee.bmagrupp.georivals.mobile.ui.MainActivity;
import ee.bmagrupp.georivals.mobile.ui.widgets.CustomDialog;

public class ProvinceFragment extends Fragment {
	// non-static immutable variables (local constants)
	private MainActivity activity;
	private LinearLayout provinceLayout;

	// static mutable variables
	public static LatLng provinceLatLng;
	public static ProvinceDTO province;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		provinceLayout = (LinearLayout) inflater.inflate(
				R.layout.province_layout, container, false);
		activity = (MainActivity) getActivity();
		MainActivity.changeFonts(provinceLayout);
		requestProvinceData();
		return provinceLayout;
	}

	@Override
	public void onDestroyView() {
		activity.cancelToastMessage();
		super.onDestroyView();
	}

	/**
	 * Requests the province data from the server. If successful, it populates
	 * the layout.
	 */

	private void requestProvinceData() {
		ProvinceViewUILoader l = new ProvinceViewUILoader(MainActivity.sid,
				provinceLatLng.latitude, provinceLatLng.longitude, activity) {

			@Override
			public void handleResponseInUI(ProvinceDTO responseObject) {
				if (responseObject != null) {
					province = responseObject;
					populateLayout();
				} else {
					activity.showToastMessage(activity
							.getString(R.string.error_retrieval_fail));
				}
			}

			@Override
			public void handleResponseInBackground(ProvinceDTO responseObject) {

			}

		};
		l.retrieveResponse();

	}

	/**
	 * Populates the layout.
	 */

	private void populateLayout() {
		TextView provinceInfo = (TextView) provinceLayout
				.findViewById(R.id.province_info);

		provinceInfo.setText(activity.getString(R.string.province_name)
				+ province.getProvinceName() + "\n"
				+ activity.getString(R.string.owner_name)
				+ province.getOwnerName() + "\n"
				+ activity.getString(R.string.units_number)
				+ province.getUnitSize());

		addButtons();
	}

	/**
	 * Checks which buttons are needed and adds them to the layout.
	 */

	private void addButtons() {
		ProvinceType provinceType = province.getType();

		if (provinceType != ProvinceType.HOME)
			addMakeHomeButton();

		if (provinceType == ProvinceType.PLAYER
				|| provinceType == ProvinceType.HOME) {
			addTransferUnitsButton();
			addRenameButton();
		} else if (province.isAttackable())
			addAttackButton();
	}

	/**
	 * Sets the 'Make home' button click listener and adds the button to the
	 * layout.
	 */

	private void addMakeHomeButton() {
		OnClickListener makeHomeClickListener = new OnClickListener() {
			@Override
			public void onClick(View v) {
				showHomeChangeConfirmationDialog();
			}
		};
		Button button = createButton(R.string.make_home, makeHomeClickListener);
		provinceLayout.addView(button);
	}

	/**
	 * Sets the 'Transfer units' button click listener and adds the button to
	 * the layout.
	 */

	private void addTransferUnitsButton() {
		OnClickListener transferUnitsClickListener = new OnClickListener() {
			@Override
			public void onClick(View v) {
				MainActivity.MOVEMENT_SELECTION_FRAGMENT
						.setMovementType(MovementType.TRANSFER);
				activity.changeFragment(
						MainActivity.MOVEMENT_SELECTION_FRAGMENT,
						activity.getString(R.string.movement_selection));
			}
		};
		Button button = createButton(R.string.units_transfer,
				transferUnitsClickListener);
		provinceLayout.addView(button);
	}

	/**
	 * Sets the 'Rename' button click listener and adds the button to the
	 * layout.
	 */

	private void addRenameButton() {
		OnClickListener renameClickListener = new OnClickListener() {
			@Override
			public void onClick(View v) {
				showRenameConfirmationDialog();
			}
		};
		Button button = createButton(R.string.rename, renameClickListener);
		provinceLayout.addView(button);
	}

	/**
	 * Sets the 'Attack' button click listener and adds the button to the
	 * layout.
	 */

	private void addAttackButton() {
		OnClickListener attackClickListener = new OnClickListener() {
			@Override
			public void onClick(View v) {
				MainActivity.MOVEMENT_SELECTION_FRAGMENT
						.setMovementType(MovementType.ATTACK);
				activity.changeFragment(
						MainActivity.MOVEMENT_SELECTION_FRAGMENT,
						activity.getString(R.string.movement_selection));
			}
		};
		Button button = createButton(R.string.attack, attackClickListener);
		provinceLayout.addView(button);
	}

	/**
	 * @return A button view with the given text and click listener.
	 * 
	 * @param buttonTextId
	 * @param clickListener
	 */

	private Button createButton(int buttonTextId, OnClickListener clickListener) {
		Button button = new Button(activity);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		params.topMargin = 10;
		button.setLayoutParams(params);
		button.setTextColor(Color.WHITE);
		button.setText(activity.getString(buttonTextId));
		button.setTypeface(MainActivity.GABRIOLA_FONT);
		button.setId(buttonTextId);
		button.setBackgroundResource(R.drawable.button);
		button.setOnClickListener(clickListener);
		return button;
	}

	/**
	 * Sets up the rename confirmation dialog and displays it.
	 */

	private void showRenameConfirmationDialog() {
		final CustomDialog renameConfirmationDialog = new CustomDialog(activity);
		renameConfirmationDialog.setMessage(activity
				.getString(R.string.enter_province_name));
		renameConfirmationDialog
				.setInputHint(activity.getString(R.string.name));

		renameConfirmationDialog.setPositiveButton(
				activity.getString(R.string.ok), new OnClickListener() {
					@Override
					public void onClick(View v) {
						String newName = renameConfirmationDialog
								.getInputValue();
						if (!newName.equals("")) {
							renameConfirmationDialog.dismiss();
							requestProvinceRename(newName);
						}
					}
				});

		renameConfirmationDialog.setNegativeButton(activity
				.getString(R.string.cancel));

		renameConfirmationDialog.show();
	}

	/**
	 * Sends a province rename request to the server.
	 * 
	 * @param newName
	 */

	private void requestProvinceRename(String newName) {
		RenameProvinceUILoader l = new RenameProvinceUILoader(
				provinceLatLng.latitude, provinceLatLng.longitude, newName,
				MainActivity.sid, activity) {

			@Override
			public void handleResponseInUI(ServerResponse responseObject) {
				ServerResult result = responseObject.getResult();
				if (result == ServerResult.OK) {
					activity.showToastMessage(activity
							.getString(R.string.province_renamed));
					activity.refreshCurrentFragment();
				} else {
					activity.showToastMessage(activity
							.getString(R.string.error_unknown));
				}

			}

			@Override
			public void handleResponseInBackground(ServerResponse responseObject) {

			}

		};
		l.retrieveResponse();
	}

	/**
	 * Sets up and displays a home change confirmation dialog.
	 */

	private void showHomeChangeConfirmationDialog() {
		final CustomDialog homeChangeConfirmationDialog = new CustomDialog(
				activity);
		homeChangeConfirmationDialog
				.setMessage(getString(R.string.confirmation_change_home));
		homeChangeConfirmationDialog.hideInput();

		homeChangeConfirmationDialog.setPositiveButton(new OnClickListener() {
			@Override
			public void onClick(View v) {
				homeChangeConfirmationDialog.dismiss();
				requestHomeChange();
			}
		});
		homeChangeConfirmationDialog.show();
	}

	/**
	 * Sends a home change request to the server.
	 */

	private void requestHomeChange() {
		ChangeHomeUILoader l = new ChangeHomeUILoader(provinceLatLng.latitude,
				provinceLatLng.longitude, MainActivity.sid, activity) {

			@Override
			public void handleResponseInUI(ServerResponse responseObject) {
				ServerResult result = responseObject.getResult();
				if (result == ServerResult.OK) {
					MainActivity.MAP_FRAGMENT.clearMap();
					activity.setUserData(null, 0, new LatLng(
							provinceLatLng.latitude, provinceLatLng.longitude));
					activity.showToastMessage(activity
							.getString(R.string.home_changed));
					activity.refreshCurrentFragment();
				} else {
					activity.showToastMessage(activity
							.getString(R.string.error_unknown));
				}

			}

			@Override
			public void handleResponseInBackground(ServerResponse responseObject) {

			}

		};
		l.retrieveResponse();
	}

}