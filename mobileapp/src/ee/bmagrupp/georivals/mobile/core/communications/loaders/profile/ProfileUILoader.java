package ee.bmagrupp.georivals.mobile.core.communications.loaders.profile;

import android.app.Activity;
import ee.bmagrupp.georivals.mobile.core.communications.uiloaderinterface.UILoadable;
import ee.bmagrupp.georivals.mobile.core.communications.uiloaderinterface.UILoader;
import ee.bmagrupp.georivals.mobile.models.profile.ProfileEntry;

/**
 * Class for loading a profile entry from server and handling them on UI.<br>
 * 
 * Override the response handling methods to use it. Call retrieveResponse to
 * send the query to server.
 * 
 * @author Jaan Janno
 * 
 */

public abstract class ProfileUILoader extends ProfileEntryLoader implements
		UILoadable<ProfileEntry> {

	Activity activity;

	/**
	 * 
	 * @param userId
	 *            User identifier sent in cookie.
	 * @param activity
	 *            Android activity that is modified.
	 */

	public ProfileUILoader(int userId, Activity activity) {
		super(userId);
		this.activity = activity;
	}

	/**
	 * Handles response on both UI and in background if so defined. Usually
	 * called by the object itself after getting a response. Useless to call
	 * manually.
	 */

	@Override
	public void handleResponse(ProfileEntry response) {
		UILoader.load(response, this, activity);
	}

}
