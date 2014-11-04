package ee.bmagrupp.aardejaht.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import ee.bmagrupp.aardejaht.R;
import ee.bmagrupp.aardejaht.ui.MainActivity;
import ee.bmagrupp.aardejaht.ui.listeners.ButtonClickListener;
import ee.bmagrupp.aardejaht.ui.listeners.MapClickListener;
import ee.bmagrupp.aardejaht.ui.widgets.TabItem;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

public class MapFragment extends com.google.android.gms.maps.MapFragment
		implements TabItem, ConnectionCallbacks, OnConnectionFailedListener,
		LocationListener {
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

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = super.onCreateView(inflater, container, savedInstanceState);
		setupMap();
		addSkin((ViewGroup) v);
		return v;
	}

	private void addSkin(ViewGroup v) {
		ImageView skin = new ImageView(activity);
		skin.setImageResource(R.color.brown_transparent);
		skin.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT));
		v.addView(skin);
	}

	@Override
	public void onDestroyView() {
		lastLatLng = map.getCameraPosition().target;
		lastZoom = map.getCameraPosition().zoom;
		if (MainActivity.toast != null)
			MainActivity.toast.cancel();
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
						59.437046, 24.753742), 16.0f));
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
				if (map.getCameraPosition().zoom > 14) {
					map.clear();
					drawProvinces();
				} else
					map.clear();
			}
		});
	}

	private void drawProvinces() {
		double lengthLatitude = 0.001;
		double lengthLongitude = 0.002;
		LatLngBounds bounds = map.getProjection().getVisibleRegion().latLngBounds;
		LatLng SW = bounds.southwest;
		LatLng NE = bounds.northeast;
		double SWlatitude = Math.floor(SW.latitude * 1000) / 1000;
		double SWlongitude = Math.floor(SW.longitude * 1000) / 1000;
		double NElatitude = Math.ceil(NE.latitude * 1000) / 1000;
		double NElongitude = Math.ceil(NE.longitude * 1000) / 1000;

		if ((SWlongitude * 1000.0) % 2 != 0)
			SWlongitude -= lengthLatitude;
		if ((NElongitude * 1000.0) % 2 != 0)
			NElongitude += lengthLatitude;

		double currentLatitude = SWlatitude;
		double currentLongitude = SWlongitude;
		while (currentLatitude < NElatitude) {
			while (currentLongitude < NElongitude) {
				map.addPolyline(new PolylineOptions().add(
						new LatLng(currentLatitude, currentLongitude),
						new LatLng(currentLatitude, currentLongitude
								+ lengthLongitude),
						new LatLng(currentLatitude + lengthLatitude,
								currentLongitude + lengthLongitude))
						.width(2.9f));
				currentLongitude += lengthLongitude;
			}
			currentLatitude = currentLatitude + lengthLatitude;
			currentLongitude = SWlongitude;

		}

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