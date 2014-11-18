package ee.bmagrupp.georivals.mobile.ui.fragments;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.google.android.gms.maps.model.LatLng;

import ee.bmagrupp.georivals.mobile.R;
import ee.bmagrupp.georivals.mobile.core.communications.loaders.province.MyProvincesViewUILoader;
import ee.bmagrupp.georivals.mobile.models.province.ProvinceDTO;
import ee.bmagrupp.georivals.mobile.models.province.ProvinceType;
import ee.bmagrupp.georivals.mobile.ui.MainActivity;
import ee.bmagrupp.georivals.mobile.ui.adapters.MyProvincesAdapter;
import ee.bmagrupp.georivals.mobile.ui.widgets.TabItem;
import android.app.Fragment;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

public class MyProvincesFragment extends Fragment implements TabItem {
	private final int tabNameId = R.string.my_provinces;
	private final int tabIconId = R.drawable.places_icon;

	private List<ProvinceDTO> myProvincesList;
	private MyProvincesAdapter adapter;
	private MainActivity activity;
	private Resources resources;
	private MyProvincesViewUILoader myProvincesListLoader;
	private LinearLayout myProvincesLayout;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		myProvincesLayout = (LinearLayout) inflater.inflate(
				R.layout.my_provinces_layout, container, false);
		activity = (MainActivity) getActivity();
		resources = activity.getResources();
		requestMyProvincesData();
		MainActivity.changeFonts(myProvincesLayout);
		return myProvincesLayout;
	}

	@Override
	public void onDestroyView() {
		if (MainActivity.toast != null)
			MainActivity.toast.cancel();
		super.onDestroyView();
	}

	private void requestMyProvincesData() {
		myProvincesListLoader = new MyProvincesViewUILoader(MainActivity.sid,
				activity) {

			@Override
			public void handleResponseListInUI(List<ProvinceDTO> responseList) {
				if (responseList != null) {
					myProvincesList = responseList;
					populateLayout();
				} else {
					activity.showMessage(resources
							.getString(R.string.error_retrieval_fail));
				}

			}

			@Override
			public void handleResponseListInBackground(
					List<ProvinceDTO> responseList) {

			}

		};
		myProvincesListLoader.retrieveList();
	}

	private void populateLayout() {
		sortProvinces();
		ListView listview = (ListView) myProvincesLayout
				.findViewById(R.id.my_provinces_listview);
		adapter = new MyProvincesAdapter(activity, myProvincesList);
		listview.setAdapter(adapter);
		listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View itemClicked,
					int position, long id) {
				ProvinceDTO province = myProvincesList.get(position);
				ProvinceFragment.provinceLatLng = new LatLng(province
						.getLatitude(), province.getLongitude());
				activity.getFragmentManager()
						.beginTransaction()
						.replace(R.id.fragment_container,
								MainActivity.PROVINCE_FRAGMENT, "Province")
						.commit();
			}
		});
	}

	private void sortProvinces() {
		if (myProvincesList != null) {
			Collections.sort(myProvincesList, new Comparator<ProvinceDTO>() {
				@Override
				public int compare(final ProvinceDTO province1,
						final ProvinceDTO province2) {
					double unitSize1 = province1.getUnitSize();
					double unitSize2 = province2.getUnitSize();
					if (unitSize1 < unitSize2)
						return 1;
					else
						return -1;
				}
			});
			for (ProvinceDTO province : myProvincesList) {
				if (province.getType() == ProvinceType.HOME) {
					myProvincesList.remove(province);
					myProvincesList.add(0, province);
					break;
				}
			}
		}
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
