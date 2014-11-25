package ee.bmagrupp.georivals.mobile.core.communications.loaders.profile;

import ee.bmagrupp.georivals.mobile.core.communications.Constants;
import ee.bmagrupp.georivals.mobile.core.communications.loaders.GenericLoader;
import ee.bmagrupp.georivals.mobile.models.profile.ProfileEntry;

/**
 * Class for making a HTTP get request to the server and retrieving ProfileEntry
 * data parsed from JSON to objects. Use this by overriding the
 * handleResponse() method and calling retrieveProfileEntry() method.
 * 
 * @author Jaan Janno
 */

public abstract class ProfileEntryLoader extends
		GenericLoader<ProfileEntry> {

	public ProfileEntryLoader(int userId) {
		super(ProfileEntry.class, Constants.PROFILE + Integer.toString(userId));
	}

}