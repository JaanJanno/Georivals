package ee.bmagrupp.georivals.mobile.core.communications.loaders.registration;

import ee.bmagrupp.georivals.mobile.core.communications.Constants;
import ee.bmagrupp.georivals.mobile.core.communications.loaders.GenericPostLoader;
import ee.bmagrupp.georivals.mobile.models.ServerResponse;
import ee.bmagrupp.georivals.mobile.models.registration.RegistrationDTO;

/**
 * Class for handling the second phase of registration.
 * 
 * @author Jaan Janno
 * 
 */

public abstract class RegistrationPhase2Poster extends
		GenericPostLoader<ServerResponse> {

	/**
	 * 
	 * @param post
	 *            Info of registration. Provides name and email and selected
	 *            home province latitude and longitude.
	 */

	public RegistrationPhase2Poster(RegistrationDTO post) {
		super(ServerResponse.class, post, Constants.REGISTRATION_PHASE2);
	}

}
