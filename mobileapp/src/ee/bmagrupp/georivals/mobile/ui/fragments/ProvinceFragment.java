package ee.bmagrupp.georivals.mobile.ui.fragments;

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
				provinceLatLng.longitude, provinceLatLng.latitude, activity) {

			@Override
			public void handleResponseObjectInUI(ProvinceDTO responseObject) {
				if (responseObject != null) {
					province = responseObject;
					populateLayout();
				} else {
					activity.showToastMessage(activity
							.getString(R.string.error_retrieval_fail));
				}
			}

			@Override
			public void handleResponseObjectInBackground(
					ProvinceDTO responseObject) {

			}

		};
		l.retrieveObject();

	}

	/**
	 * Populates the layout.
	 */

	private void populateLayout() {
		TextView provinceNameTextview = (TextView) provinceLayout
				.findViewById(R.id.province_name);
		TextView ownerNameTextview = (TextView) provinceLayout
				.findViewById(R.id.province_owner_name);
		TextView unitsTextview = (TextView) provinceLayout
				.findViewById(R.id.province_unit_size);

		provinceNameTextview.setText(activity.getString(R.string.province_name)
				+ " " + province.getProvinceName());
		ownerNameTextview.setText(activity.getString(R.string.owner_name) + " "
				+ province.getOwnerName());
		unitsTextview.setText(activity.getString(R.string.units_number) + " "
				+ Integer.toString(province.getUnitSize()));

		addButtons();
	}

	/**
	 * Adds suitable button views to the layout and sets their click listeners.
	 */

	private void addButtons() {
		if (province.getType() == ProvinceType.PLAYER
				|| province.getType() == ProvinceType.HOME) {
			OnClickListener TransferUnitsClickListener = new OnClickListener() {
				@Override
				public void onClick(View v) {
					MainActivity.MOVEMENT_SELECTION_FRAGMENT
							.setMovementType(MovementType.TRANSFER);
					activity.changeFragment(
							MainActivity.MOVEMENT_SELECTION_FRAGMENT,
							activity.getString(R.string.movement_selection));
				}

			};
			createButton(R.string.units_transfer, TransferUnitsClickListener);

			OnClickListener RenameClickListener = new OnClickListener() {
				@Override
				public void onClick(View v) {
					showRenameConfirmationDialog();
				}
			};
			createButton(R.string.rename, RenameClickListener);

			if (province.getType() != ProvinceType.HOME) {
				OnClickListener MakeHomeClickListener = new OnClickListener() {
					@Override
					public void onClick(View v) {
						showHomeChangeConfirmationDialog();
					}
				};
				createButton(R.string.make_home, MakeHomeClickListener);
			}

		} else if (province.isAttackable()) {
			OnClickListener AttackClickListener = new OnClickListener() {
				@Override
				public void onClick(View v) {
					MainActivity.MOVEMENT_SELECTION_FRAGMENT
							.setMovementType(MovementType.ATTACK);
					activity.changeFragment(
							MainActivity.MOVEMENT_SELECTION_FRAGMENT,
							activity.getString(R.string.movement_selection));
				}
			};

			createButton(R.string.attack, AttackClickListener);
		}
	}

	/**
	 * Creates a button view with the given text and click listener and adds it
	 * to the layout.
	 * 
	 * @param buttonTextId
	 * @param clickListener
	 */

	private void createButton(int buttonTextId, OnClickListener clickListener) {
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

		provinceLayout.addView(button);
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
				provinceLatLng.longitude, provinceLatLng.latitude, newName,
				MainActivity.sid, activity) {

			@Override
			public void handleResponseObjectInUI(ServerResponse responseObject) {
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
			public void handleResponseObjectInBackground(
					ServerResponse responseObject) {

			}

		};
		l.retrieveObject();
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
			public void handleResponseObjectInUI(ServerResponse responseObject) {
				ServerResult result = responseObject.getResult();
				if (result == ServerResult.OK) {
					activity.showToastMessage(activity
							.getString(R.string.home_changed));
					activity.refreshCurrentFragment();
				} else {
					activity.showToastMessage(activity
							.getString(R.string.error_unknown));
				}

			}

			@Override
			public void handleResponseObjectInBackground(
					ServerResponse responseObject) {

			}

		};
		l.retrieveObject();
	}

}