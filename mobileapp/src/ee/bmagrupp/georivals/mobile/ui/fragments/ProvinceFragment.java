package ee.bmagrupp.georivals.mobile.ui.fragments;

import com.google.android.gms.maps.model.LatLng;
import ee.bmagrupp.georivals.mobile.R;
import ee.bmagrupp.georivals.mobile.core.communications.loaders.province.ProvinceViewUILoader;
import ee.bmagrupp.georivals.mobile.core.communications.loaders.province.modify.RenameProvinceUILoader;
import ee.bmagrupp.georivals.mobile.models.ServerResponse;
import ee.bmagrupp.georivals.mobile.models.ServerResult;
import ee.bmagrupp.georivals.mobile.models.province.ProvinceDTO;
import ee.bmagrupp.georivals.mobile.models.province.ProvinceType;
import ee.bmagrupp.georivals.mobile.ui.MainActivity;
import ee.bmagrupp.georivals.mobile.ui.widgets.CustomDialog;
import android.app.Fragment;
import android.content.res.Resources;
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
	private LinearLayout provinceLayout;
	private MainActivity activity;
	private Resources resources;

	public static LatLng provinceLatLng;
	private ProvinceDTO province;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		provinceLayout = (LinearLayout) inflater.inflate(
				R.layout.province_layout, container, false);
		activity = (MainActivity) getActivity();
		resources = activity.getResources();
		MainActivity.changeFonts(provinceLayout);
		requestProvinceData();
		return provinceLayout;
	}

	@Override
	public void onDestroyView() {
		if (MainActivity.toast != null)
			MainActivity.toast.cancel();
		super.onDestroyView();
	}

	private void requestProvinceData() {
		ProvinceViewUILoader l = new ProvinceViewUILoader(MainActivity.sid,
				provinceLatLng.longitude, provinceLatLng.latitude, activity) {

			@Override
			public void handleResponseObjectInUI(ProvinceDTO responseObject) {
				if (responseObject != null) {
					province = responseObject;
					populateLayout();
				} else {
					activity.showMessage(resources
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

	private void populateLayout() {
		TextView provinceNameTextview = (TextView) provinceLayout
				.findViewById(R.id.province_name);
		TextView ownerNameTextview = (TextView) provinceLayout
				.findViewById(R.id.province_owner_name);
		TextView unitsTextview = (TextView) provinceLayout
				.findViewById(R.id.province_unit_size);

		provinceNameTextview.setText(resources
				.getString(R.string.province_name)
				+ " "
				+ province.getProvinceName());
		ownerNameTextview.setText(resources.getString(R.string.owner_name)
				+ " " + province.getOwnerName());
		unitsTextview.setText(resources.getString(R.string.units_number) + " "
				+ Integer.toString(province.getUnitSize()));

		addButtons();
	}

	private void addButtons() {
		if (province.getType() == ProvinceType.PLAYER
				|| province.getType() == ProvinceType.HOME) {
			OnClickListener TransferUnitsClickListener = new OnClickListener() {

				@Override
				public void onClick(View v) {

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

					}

				};
				createButton(R.string.make_home, MakeHomeClickListener);
			}

		} else if (province.isAttackable()) {
			OnClickListener AttackClickListener = new OnClickListener() {

				@Override
				public void onClick(View v) {

				}

			};

			createButton(R.string.attack, AttackClickListener);
		}
	}

	private void createButton(int buttonTextId, OnClickListener clickListener) {
		Button button = new Button(activity);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		params.topMargin = 10;
		button.setLayoutParams(params);
		button.setTextColor(Color.WHITE);
		button.setText(resources.getString(buttonTextId));
		button.setTypeface(MainActivity.GABRIOLA_FONT);
		button.setId(buttonTextId);
		button.setBackgroundResource(R.drawable.button);
		button.setOnClickListener(clickListener);

		provinceLayout.addView(button);
	}

	private void showRenameConfirmationDialog() {
		final CustomDialog renameConfirmationDialog = new CustomDialog(activity);
		renameConfirmationDialog.setMessage(resources
				.getString(R.string.enter_province_name));
		renameConfirmationDialog.setInput(resources.getString(R.string.name));

		renameConfirmationDialog.setPositiveButton(
				resources.getString(R.string.ok), new OnClickListener() {
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

		renameConfirmationDialog.setNegativeButton(resources
				.getString(R.string.cancel));

		renameConfirmationDialog.show();
	}

	private void requestProvinceRename(String newName) {
		RenameProvinceUILoader l = new RenameProvinceUILoader(
				provinceLatLng.longitude, provinceLatLng.latitude, newName,
				MainActivity.sid, activity) {

			@Override
			public void handleResponseObjectInUI(ServerResponse responseObject) {
				ServerResult result = responseObject.getResult();
				if (result == ServerResult.OK) {
					activity.showMessage(resources
							.getString(R.string.province_renamed));
					activity.getFragmentManager().beginTransaction()
							.detach(MainActivity.PROVINCE_FRAGMENT)
							.attach(MainActivity.PROVINCE_FRAGMENT).commit();
				} else {
					activity.showMessage(resources
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