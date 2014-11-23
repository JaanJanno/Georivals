package ee.bmagrupp.georivals.mobile.core.location.service;

import ee.bmagrupp.georivals.mobile.core.location.LocationChangeHandler;
import ee.bmagrupp.georivals.mobile.ui.MainActivity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.os.IBinder;
import android.preference.PreferenceManager;

/**
 * Background service, that claims units for the player in the background.
 * 
 * @author Jaan Janno
 * 
 */

public class LocationService extends Service {

	@Override
	public void onCreate() {

	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		LocationManager l = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		l.requestLocationUpdates("gps", MainActivity.UNIT_CLAIM_INTERVAL,
				MainActivity.UNIT_CLAIM_MIN_DISTANCE, getListener());
		return START_STICKY;
	}

	/*
	 * Creates a listener that claims units for playey with given sid.
	 */

	private LocationChangeHandler getListener() {
		String sid = getPlayerSid();
		return new LocationChangeHandler(sid);
	}

	/*
	 * Gets the player's sid from shared preferences.
	 */

	private String getPlayerSid() {
		SharedPreferences manager = PreferenceManager
				.getDefaultSharedPreferences(this);
		return manager.getString("sid", "");
	}

	/**
	 * No binding supported.
	 */

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

}