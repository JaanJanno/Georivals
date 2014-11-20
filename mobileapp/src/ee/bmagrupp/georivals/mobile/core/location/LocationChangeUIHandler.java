package ee.bmagrupp.georivals.mobile.core.location;

import ee.bmagrupp.georivals.mobile.core.communications.loaders.units.UnitClaimLoader;
import ee.bmagrupp.georivals.mobile.core.communications.loaders.units.UnitClaimUILoader;
import ee.bmagrupp.georivals.mobile.models.ServerResponse;
import ee.bmagrupp.georivals.mobile.models.ServerResult;
import ee.bmagrupp.georivals.mobile.ui.MainActivity;
import ee.bmagrupp.georivals.mobile.ui.fragments.MapFragment;
import android.app.Activity;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;

/**
 * Class for handling the movement of the player. Claims the players units when
 * the player moves and updates the map view.
 * 
 * @author Jaan Janno
 * 
 */

public class LocationChangeUIHandler implements LocationListener {

	private Activity activity;

	/**
	 * 
	 * @param activity
	 *            Android activity that is modified.
	 */

	public LocationChangeUIHandler(Activity activity) {
		this.activity = activity;
	}

	/*
	 * Handles movement of the player. Sends movement data to server and updates
	 * map when needed.
	 */

	@Override
	public void onLocationChanged(Location location) {
		final double longitude = location.getLongitude();
		final double latitude = location.getLatitude();

		UnitClaimLoader l = new UnitClaimUILoader(MainActivity.sid, latitude,
				longitude, activity) {

			@Override
			public void handleResponseObjectInUI(ServerResponse responseObject) {
				if (responseObject.getResult() == ServerResult.OK)
					updateMap();
			}

			@Override
			public void handleResponseObjectInBackground(
					ServerResponse responseObject) {
				Log.v("Location", "ClaimUnits:" + longitude + ":" + latitude
						+ ":" + responseObject);
			}

		};
		l.retrieveObject();
	}

	/*
	 * Updates map if the map view is visible and server response indicates,
	 * that units were collected.
	 */

	private void updateMap() {
		MapFragment.callMapRefresh();
	}

	@Override
	public void onProviderDisabled(String provider) {

	}

	@Override
	public void onProviderEnabled(String provider) {

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {

	}

}
