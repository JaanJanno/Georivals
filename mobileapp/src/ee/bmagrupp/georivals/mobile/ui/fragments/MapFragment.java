package ee.bmagrupp.georivals.mobile.ui.fragments;

import java.util.HashMap;
import java.util.List;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.GroundOverlay;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import ee.bmagrupp.aardejaht.R;
import ee.bmagrupp.georivals.mobile.core.communications.loaders.province.ProvinceUILoader;
import ee.bmagrupp.georivals.mobile.models.CameraFOV;
import ee.bmagrupp.georivals.mobile.models.ProvinceDTO;
import ee.bmagrupp.georivals.mobile.ui.MainActivity;
import ee.bmagrupp.georivals.mobile.ui.listeners.ButtonClickListener;
import ee.bmagrupp.georivals.mobile.ui.listeners.MapClickListener;
import ee.bmagrupp.georivals.mobile.ui.widgets.TabItem;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

public class MapFragment extends com.google.android.gms.maps.MapFragment
		implements TabItem, ConnectionCallbacks, OnConnectionFailedListener,
		LocationListener {
	private final double PROVINCE_LATITUDE_RADIUS = 0.0005;
	private final double PROVINCE_LONGITUDE_RADIUS = 0.001;
	private String tabName = "Map";
	private int tabIconId = R.drawable.places_icon;
	private GoogleMap map;
	private GoogleApiClient googleApiClient;
	private LocationRequest locationRequest;
	private LocationManager locationManager;
	private MainActivity activity;
	private LatLng lastLatLng;
	private float lastZoom;
	private ButtonClickListener buttonClickListener;
	private MapClickListener mapClickListener;
	private Location playerLocation;
	private HashMap<LatLng, GroundOverlay> drawnProvincesMap = new HashMap<LatLng, GroundOverlay>();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = super.onCreateView(inflater, container, savedInstanceState);
		setupMap();
		if (activity.choosingHomeProvince) {
			addSetHomeViews((ViewGroup) v);
		}
		return v;
	}

	private void addSetHomeViews(ViewGroup v) {
		TextView chooseHomeLabel = (TextView) activity
				.findViewById(R.id.choose_home_label);
		chooseHomeLabel.setVisibility(View.VISIBLE);
		chooseHomeLabel.setTypeface(MainActivity.GABRIOLA_FONT);

		Button setHomeButton = (Button) activity
				.findViewById(R.id.set_home_current);
		setHomeButton.setVisibility(View.VISIBLE);
		setHomeButton.setTypeface(MainActivity.GABRIOLA_FONT);
		setHomeButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (playerLocation != null) {
					activity.getRegistrationFragment()
							.showPhase2ConfirmationDialog(
									playerLocation.getLatitude(),
									playerLocation.getLongitude());
				} else {
					activity.showMessage("Get your location first!");
				}
			}
		});
	}

	@Override
	public void onDestroyView() {
		lastLatLng = map.getCameraPosition().target;
		lastZoom = map.getCameraPosition().zoom;
		if (MainActivity.toast != null)
			MainActivity.toast.cancel();
		drawnProvincesMap.clear();
		super.onDestroyView();
	}

	@Override
	public void onStart() {
		super.onStart();
		googleApiClient.connect();
	}

	@Override
	public void onStop() {
		googleApiClient.disconnect();
		super.onStop();
	}

	@Override
	public void onPause() {
		super.onPause();
		if (MainActivity.toast != null)
			MainActivity.toast.cancel();
	}

	@Override
	public void onConnected(Bundle bundle) {
		locationRequest = LocationRequest.create();
		locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
		locationRequest.setInterval(1000);
		locationRequest.setFastestInterval(500);
		LocationServices.FusedLocationApi.requestLocationUpdates(
				googleApiClient, locationRequest, this);
	}

	@Override
	public void onConnectionSuspended(int i) {
		Log.d("DEBUG", "Google Api Client connection has been suspended.");
	}

	@Override
	public void onConnectionFailed(ConnectionResult connectionResult) {
		Log.d("DEBUG", "Google Api Client connection has failed.");
	}

	@Override
	public void onLocationChanged(Location location) {
		if (MainActivity.toast != null)
			MainActivity.toast.cancel();
		playerLocation = location;
	}

	// currently not in use, but this will be used later
	public void focusOnLocation(Location location) {
		float currentZoom = map.getCameraPosition().zoom;
		LatLng latLng = new LatLng(location.getLatitude(),
				location.getLongitude());
		map.moveCamera(CameraUpdateFactory.newLatLng(latLng));
		CameraPosition cameraPosition = new CameraPosition.Builder()
				.target(latLng).zoom(currentZoom).bearing(0).tilt(0).build();
		CameraUpdate cameraUpdate = CameraUpdateFactory
				.newCameraPosition(cameraPosition);
		map.animateCamera(cameraUpdate);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void setupMap() {
		if (map == null) {
			activity = (MainActivity) getActivity();
			locationManager = (LocationManager) activity
					.getSystemService(Context.LOCATION_SERVICE);
			googleApiClient = new GoogleApiClient.Builder(activity)
					.addApi(LocationServices.API).addConnectionCallbacks(this)
					.addOnConnectionFailedListener(this).build();
			buttonClickListener = new ButtonClickListener(activity,
					locationManager);
			mapClickListener = new MapClickListener((MainActivity) activity);
			map = this.getMap();

			if (map != null) {
				map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(
						59.437046, 24.753742), 17.0f));
				setMapSettings();
			}
		} else {
			map = this.getMap();
			map.moveCamera(CameraUpdateFactory.newLatLngZoom(lastLatLng,
					lastZoom));
			setMapSettings();
		}
	}

	private void setMapSettings() {
		map.setMyLocationEnabled(true);
		map.setOnMyLocationButtonClickListener(buttonClickListener);
		map.setOnMapClickListener(mapClickListener);
		map.setOnCameraChangeListener(new OnCameraChangeListener() {
			@Override
			public void onCameraChange(CameraPosition position) {
				if (map.getCameraPosition().zoom > 15)
					requestProvinces();
				else if (drawnProvincesMap.size() > 0) {
					map.clear();
					drawnProvincesMap.clear();
				}
			}
		});
	}

	private void requestProvinces() {
		LatLngBounds bounds = map.getProjection().getVisibleRegion().latLngBounds;
		ProvinceUILoader l = new ProvinceUILoader(activity.SID, new CameraFOV(
				bounds), activity) {

			@Override
			public void handleResponseListInUI(List<ProvinceDTO> responseList) {
				drawProvinces(responseList);
			}

			@Override
			public void handleResponseListInBackground(
					List<ProvinceDTO> responseList) {

			}

		};
		l.retrieveList();
	}

	@SuppressLint("InflateParams")
	private void drawProvinces(List<ProvinceDTO> provinceList) {
		HashMap<LatLng, GroundOverlay> newDrawnProvincesMap = new HashMap<LatLng, GroundOverlay>();
		for (ProvinceDTO province : provinceList) {
			double centerLatitude = roundDouble(province.getLatitude(), 10000);
			double centerLongitude = roundDouble(province.getLongitude(), 1000);
			LatLng centerLatLng = new LatLng(centerLatitude, centerLongitude);

			if (!drawnProvincesMap.containsKey(centerLatLng)) {
				RelativeLayout provinceLayout = (RelativeLayout) LayoutInflater
						.from(activity).inflate(R.layout.province1, null);

				setProvinceBackgroundAndTitle(provinceLayout, province);
				setProvinceUnitCount(provinceLayout, province.getUnitCount());
				Bitmap provinceBitmap = createBitmap(provinceLayout);

				LatLngBounds provinceBounds = new LatLngBounds(new LatLng(
						centerLatitude - PROVINCE_LATITUDE_RADIUS,
						centerLongitude - PROVINCE_LONGITUDE_RADIUS),
						new LatLng(centerLatitude + PROVINCE_LATITUDE_RADIUS,
								centerLongitude + PROVINCE_LONGITUDE_RADIUS));

				GroundOverlay provinceOverlay = createGroundOverlay(
						provinceBitmap, provinceBounds);

				newDrawnProvincesMap.put(centerLatLng, provinceOverlay);
			} else {
				newDrawnProvincesMap.put(centerLatLng,
						drawnProvincesMap.get(centerLatLng));
			}
		}

		removeOldProvinces(newDrawnProvincesMap);
	}

	private double roundDouble(double roundable, int precision) {
		double rounded = Math.round(roundable * precision);
		return rounded / precision;
	}

	@SuppressWarnings("deprecation")
	private void setProvinceBackgroundAndTitle(RelativeLayout provinceLayout,
			ProvinceDTO province) {
		int ownerId = province.getPlayerId();
		TextView provinceName = (TextView) provinceLayout
				.findViewById(R.id.province_name);
		int provinceColor;
		if (ownerId == 0) {
			provinceName.setVisibility(View.INVISIBLE);
			provinceColor = activity.getResources().getColor(
					R.color.brown_transparent);
		} else {
			provinceName.setText(province.getName());
			provinceName.setTypeface(MainActivity.GABRIOLA_FONT);
			if (ownerId == activity.userId)
				provinceColor = activity.getResources().getColor(
						R.color.green_transparent);
			else
				provinceColor = activity.getResources().getColor(
						R.color.dark_brown_transparent);
		}

		GradientDrawable provinceBackground = new GradientDrawable();
		provinceBackground.setColor(provinceColor);
		provinceBackground.setStroke(1, Color.BLACK);
		provinceLayout.setBackgroundDrawable(provinceBackground);
	}

	private void setProvinceUnitCount(RelativeLayout provinceLayout,
			int unitCount) {
		TextView unitCountTextView = (TextView) provinceLayout
				.findViewById(R.id.province_unit_count);
		unitCountTextView.setText(String.valueOf(unitCount));
		unitCountTextView.setTypeface(MainActivity.GABRIOLA_FONT);
	}

	private Bitmap createBitmap(View view) {
		view.setDrawingCacheEnabled(true);
		view.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT));
		view.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
				MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
		view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
		view.buildDrawingCache();
		return view.getDrawingCache();
	}

	private GroundOverlay createGroundOverlay(Bitmap bitmap, LatLngBounds bounds) {
		GroundOverlayOptions overlayOptions = new GroundOverlayOptions().image(
				BitmapDescriptorFactory.fromBitmap(bitmap)).positionFromBounds(
				bounds);
		return map.addGroundOverlay(overlayOptions);
	}

	private void removeOldProvinces(
			HashMap<LatLng, GroundOverlay> newDrawnProvincesMap) {
		for (LatLng provinceLatLng : drawnProvincesMap.keySet()) {
			if (!newDrawnProvincesMap.containsKey(provinceLatLng))
				drawnProvincesMap.get(provinceLatLng).remove();
		}
		drawnProvincesMap = newDrawnProvincesMap;
	}

	public GoogleApiClient getGoogleApiClient() {
		return googleApiClient;
	}

	public LocationRequest getLocationRequest() {
		return locationRequest;
	}

	public LocationManager getLocationManager() {
		return locationManager;
	}

	public LatLng getLastLatLng() {
		return lastLatLng;
	}

	public float getLastZoom() {
		return lastZoom;
	}

	public ButtonClickListener getButtonClickListener() {
		return buttonClickListener;
	}

	@Override
	public int getTabIconId() {
		return tabIconId;
	}

	@Override
	public String getTabName() {
		return tabName;
	}
}