package ee.bmagrupp.georivals.mobile.core.location;

import ee.bmagrupp.georivals.mobile.core.communications.loaders.units.UnitClaimLoader;
import ee.bmagrupp.georivals.mobile.models.ServerResponse;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;

/**
 * Class for handling the movement of the player. Claims the players units in
 * the background.
 * 
 * @author Jaan Janno
 * 
 */

public class LocationChangeHandler implements LocationListener {

	private String sid; // Sid of the player to claim units for.

	public LocationChangeHandler() {

	}

	/*
	 * Handles movement of the player in background.
	 */

	public LocationChangeHandler(String sid) {
		this.sid = sid;
	}

	@Override
	public void onLocationChanged(Location location) {
		final double longitude = location.getLongitude();
		final double latitude = location.getLatitude();

		UnitClaimLoader l = new UnitClaimLoader(sid, latitude, longitude) {

			@Override
			public void handleResponseObject(ServerResponse responseObject) {
				Log.v("LocationService", "ClaimUnits:" + longitude + ":"
						+ latitude + ":" + responseObject);
			}

		};
		l.retrieveObject();
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
