package ee.bmagrupp.georivals.mobile.core.location;

import android.app.Activity;
import android.location.Location;
import android.location.LocationListener;
import android.util.Log;
import ee.bmagrupp.georivals.mobile.core.communications.loaders.units.UnitClaimLoader;
import ee.bmagrupp.georivals.mobile.core.communications.loaders.units.UnitClaimUILoader;
import ee.bmagrupp.georivals.mobile.models.ServerResponse;
import ee.bmagrupp.georivals.mobile.models.ServerResult;
import ee.bmagrupp.georivals.mobile.ui.MainActivity;

/**
 * Class for handling the movement of the player. Claims the players units when
 * the player moves and updates the map view.
 * 
 * @author Jaan Janno
 * 
 */

public class LocationChangeUIHandler extends LocationChangeHandler implements
		LocationListener {

	private static Location lastPlayerLocation;
	private static long lastLocationUpdateTime;
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
		setLastPlayerLocation(location);
		setLastLocationUpdateTime(System.currentTimeMillis());

		final double longitude = location.getLongitude();
		final double latitude = location.getLatitude();

		UnitClaimLoader l = new UnitClaimUILoader(MainActivity.sid, latitude,
				longitude, activity) {

			@Override
			public void handleResponseInUI(ServerResponse responseObject) {
				if (responseObject != null
						&& responseObject.getResult() == ServerResult.OK)
					updateMap();
			}

			@Override
			public void handleResponseInBackground(ServerResponse responseObject) {
				Log.v("Location", "ClaimUnits:" + longitude + ":" + latitude
						+ ":" + responseObject);
			}

		};
		l.retrieveResponse();
	}

	/*
	 * Updates map if the map view is visible and server response indicates,
	 * that units were collected.
	 */

	private void updateMap() {
		MainActivity.MAP_FRAGMENT.refreshMap();
	}

	private static void setLastPlayerLocation(Location location) {
		lastPlayerLocation = location;
	}

	private static void setLastLocationUpdateTime(long time) {
		lastLocationUpdateTime = time;
	}

	public static Location getLastPlayerLocation() {
		return lastPlayerLocation;
	}

	public static long getLastLocationUpdateTime() {
		return lastLocationUpdateTime;
	}

}
