package ee.bmagrupp.georivals.mobile.core.communications.loaders.profile;

import ee.bmagrupp.georivals.mobile.core.communications.Constants;
import ee.bmagrupp.georivals.mobile.core.communications.loaders.GenericObjectLoader;
import ee.bmagrupp.georivals.mobile.models.profile.ProfileEntry;

/**
 * Class for making a HTTP get request to the server and retrieving ProfileEntry
 * data parsed from JSON to objects. Use this by overriding the
 * handleResponseList() method and calling retrieveProfileEntry() method.
 * 
 * @author Jaan Janno
 */

public abstract class ProfileEntryLoader extends
		GenericObjectLoader<ProfileEntry> {

	public ProfileEntryLoader(int userId) {
		super(ProfileEntry.class, Constants.PROFILE + Integer.toString(userId));
	}

	/**
	 * Override this method to define the behavior after response has been
	 * retrieved. Remember this method doesn't run on the UI thread!
	 */

	@Override
	public abstract void handleResponseObject(ProfileEntry responseObject);

}