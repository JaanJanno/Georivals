package ee.bmagrupp.aardejaht.ui.fragments;

import java.util.List;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

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
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import ee.bmagrupp.aardejaht.R;
import ee.bmagrupp.aardejaht.core.communications.loaders.province.ProvinceUILoader;
import ee.bmagrupp.aardejaht.models.CameraFOV;
import ee.bmagrupp.aardejaht.models.ProvinceDTO;
import ee.bmagrupp.aardejaht.ui.MainActivity;
import ee.bmagrupp.aardejaht.ui.listeners.ButtonClickListener;
import ee.bmagrupp.aardejaht.ui.listeners.MapClickListener;
import ee.bmagrupp.aardejaht.ui.widgets.TabItem;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
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
	private Location playerLocation;

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
		Typeface font = Typeface.createFromAsset(activity.getAssets(),
				"fonts/Gabriola.ttf");

		TextView chooseHomeLabel = (TextView) activity
				.findViewById(R.id.choose_home_label);
		chooseHomeLabel.setVisibility(View.VISIBLE);
		chooseHomeLabel.setTypeface(font);

		Button setHomeButton = (Button) activity
				.findViewById(R.id.set_home_current);
		setHomeButton.setVisibility(View.VISIBLE);
		setHomeButton.setTypeface(font);
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
				map.clear();
				if (map.getCameraPosition().zoom > 14)
					requestProvinces();
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

	private void drawProvinces(List<ProvinceDTO> provinceList) {
		for (ProvinceDTO province : provinceList) {
			double centerLatitude = province.getLatitude();
			double centerLongitude = province.getLongitude();
			double lengthLatitude = 0.0005;
			double lengthLongitude = 0.001;
			double westernLatitude = centerLatitude - lengthLatitude;
			double southernLongitude = centerLongitude - lengthLongitude;
			double easternLatitude = centerLatitude + lengthLatitude;
			double northernLongitude = centerLongitude + lengthLongitude;
			map.addPolygon(new PolygonOptions()
					.add(new LatLng(westernLatitude, southernLongitude),
							new LatLng(easternLatitude, southernLongitude),
							new LatLng(easternLatitude, northernLongitude),
							new LatLng(westernLatitude, northernLongitude))
					.strokeColor(Color.BLACK)
					.strokeWidth(2.9f)
					.fillColor(
							activity.getResources().getColor(
									R.color.brown_transparent)));
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