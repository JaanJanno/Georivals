package ee.bmagrupp.georivals.mobile.ui.fragments;

import java.util.HashMap;
import java.util.List;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
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

import ee.bmagrupp.georivals.mobile.R;
import ee.bmagrupp.georivals.mobile.core.communications.loaders.province.ProvinceUILoader;
import ee.bmagrupp.georivals.mobile.models.map.CameraFOV;
import ee.bmagrupp.georivals.mobile.models.province.ProvinceDTO;
import ee.bmagrupp.georivals.mobile.models.province.ProvinceType;
import ee.bmagrupp.georivals.mobile.ui.MainActivity;
import ee.bmagrupp.georivals.mobile.ui.listeners.ButtonClickListener;
import ee.bmagrupp.georivals.mobile.ui.listeners.MapClickListener;
import ee.bmagrupp.georivals.mobile.ui.widgets.TabItem;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

@SuppressWarnings("deprecation")
public class MapFragment extends com.google.android.gms.maps.MapFragment
		implements TabItem, ConnectionCallbacks, OnConnectionFailedListener,
		LocationListener {
	private final double provinceLatitudeRadius = 0.0005;
	private final double provinceLongitudeRadius = 0.001;
	private final int tabNameId = R.string.map;
	private final int tabIconId = R.drawable.places_icon;

	private GoogleMap map;
	private GoogleApiClient googleApiClient;
	private LocationRequest locationRequest;
	private LocationManager locationManager;
	private MainActivity activity;
	private Resources resources;
	private LatLng lastLatLng;
	private float lastZoom;
	private ButtonClickListener buttonClickListener;
	private MapClickListener mapClickListener;
	private Location playerLocation;
	private HashMap<LatLng, GroundOverlay> drawnProvincesMap = new HashMap<LatLng, GroundOverlay>();
	private List<ProvinceDTO> provinceList;

	private static MapFragment instance;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = super.onCreateView(inflater, container, savedInstanceState);
		setupMap();
		if (MainActivity.choosingHomeProvince) {
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
					MainActivity.REGISTRATION_FRAGMENT
							.showPhase2ConfirmationDialog(new LatLng(
									playerLocation.getLatitude(),
									playerLocation.getLongitude()));
				} else {
					activity.showMessage(resources
							.getString(R.string.error_get_location));
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
		instance = this;
	}

	@Override
	public void onStop() {
		googleApiClient.disconnect();
		super.onStop();
		instance = null;
	}

	@Override
	public void onPause() {
		super.onPause();
		if (MainActivity.toast != null)
			MainActivity.toast.cancel();
	}

	/**
	 * Calls for a map refresh when a MapFragment is open.
	 */

	public static void callMapRefresh() {
		if (instance != null)
			instance.refreshMap();
	}

	private void refreshMap() {
		if (map.getCameraPosition().zoom > 15)
			requestProvinceListData();
		else if (drawnProvincesMap.size() > 0) {
			map.clear();
			drawnProvincesMap.clear();
		}
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

	private void setupMap() {
		if (map == null) {
			activity = (MainActivity) getActivity();
			resources = activity.getResources();
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
				refreshMap();
			}
		});
	}

	private void requestProvinceListData() {
		LatLngBounds bounds = map.getProjection().getVisibleRegion().latLngBounds;
		ProvinceUILoader l = new ProvinceUILoader(MainActivity.sid,
				new CameraFOV(bounds), activity) {

			@Override
			public void handleResponseListInUI(List<ProvinceDTO> responseList) {
				if (responseList != null) {
					provinceList = responseList;
					drawProvinces();
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
		l.retrieveList();
	}

	@SuppressLint("InflateParams")
	private void drawProvinces() {
		HashMap<LatLng, GroundOverlay> newDrawnProvincesMap = new HashMap<LatLng, GroundOverlay>();
		for (ProvinceDTO province : provinceList) {
			double centerLatitude = MainActivity.roundDouble(
					province.getLatitude(), 10000);
			double centerLongitude = MainActivity.roundDouble(
					province.getLongitude(), 1000);
			LatLng centerLatLng = new LatLng(centerLatitude, centerLongitude);

			if (!drawnProvincesMap.containsKey(centerLatLng)) {
				RelativeLayout provinceLayout;
				if (province.isUnderAttack() || !province.isAttackable()) {
					provinceLayout = (RelativeLayout) LayoutInflater.from(
							activity).inflate(R.layout.province_tile_special,
							null);
					setSpecialProvinceLayout(provinceLayout, province);
				} else {
					provinceLayout = (RelativeLayout) LayoutInflater.from(
							activity).inflate(R.layout.province_tile_normal,
							null);
					setNormalProvinceLayout(provinceLayout, province);
				}

				Bitmap provinceBitmap = createBitmap(provinceLayout);

				LatLngBounds provinceBounds = new LatLngBounds(new LatLng(
						centerLatitude - provinceLatitudeRadius,
						centerLongitude - provinceLongitudeRadius), new LatLng(
						centerLatitude + provinceLatitudeRadius,
						centerLongitude + provinceLongitudeRadius));

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

	private void setSpecialProvinceLayout(RelativeLayout provinceLayout,
			ProvinceDTO province) {
		ImageView specialIcon = (ImageView) provinceLayout
				.findViewById(R.id.province_special_icon);
		int provinceColor;
		if (province.isUnderAttack()) {
			specialIcon.setImageResource(R.drawable.province_alert_icon);
			provinceColor = resources.getColor(R.color.green_transparent);
		} else {
			specialIcon.setImageResource(R.drawable.lock_icon);
			provinceColor = resources.getColor(R.color.dark_brown_transparent);
		}

		GradientDrawable provinceBackground = new GradientDrawable();
		provinceBackground.setColor(provinceColor);
		provinceBackground.setStroke(1, Color.BLACK);
		provinceLayout.setBackgroundDrawable(provinceBackground);
	}

	private void setNormalProvinceLayout(RelativeLayout provinceLayout,
			ProvinceDTO province) {
		TextView provinceName = (TextView) provinceLayout
				.findViewById(R.id.province_name);
		int provinceColor;
		if (province.getType() == ProvinceType.BOT) {
			provinceName.setVisibility(View.INVISIBLE);
			provinceColor = resources.getColor(R.color.brown_transparent);
		} else {
			provinceName.setText(province.getProvinceName());
			provinceName.setTypeface(MainActivity.GABRIOLA_FONT);
			if (province.getType() == ProvinceType.HOME
					|| province.getType() == ProvinceType.PLAYER) {
				provinceColor = resources.getColor(R.color.green_transparent);
			} else
				provinceColor = resources
						.getColor(R.color.dark_brown_transparent);
		}

		GradientDrawable provinceBackground = new GradientDrawable();
		provinceBackground.setColor(provinceColor);
		provinceBackground.setStroke(1, Color.BLACK);
		provinceLayout.setBackgroundDrawable(provinceBackground);

		setProvinceUnitCount(provinceLayout, province.getUnitSize());
	}

	private void setProvinceUnitCount(RelativeLayout provinceLayout,
			int unitCount) {
		TextView unitCountTextView = (TextView) provinceLayout
				.findViewById(R.id.province_unit_count);
		unitCountTextView.setText(String.valueOf(unitCount));
		unitCountTextView.setTypeface(MainActivity.GABRIOLA_FONT);
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		params.topMargin = (int) Math
				.abs(2.5 * map.getCameraPosition().target.latitude);
		unitCountTextView.setLayoutParams(params);
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
	public int getTabNameId() {
		return tabNameId;
	}
}