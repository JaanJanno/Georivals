package ee.bmagrupp.georivals.mobile.core.communications.loaders.registration;

import ee.bmagrupp.georivals.mobile.core.communications.Constants;
import ee.bmagrupp.georivals.mobile.core.communications.loaders.GenericObjectPostLoader;
import ee.bmagrupp.georivals.mobile.models.RegistrationDTO;
import ee.bmagrupp.georivals.mobile.models.RegistrationResponse;

/**
 * Class for handling the second phase of registration.
 * @author Jaan Janno
 *
 */

public abstract class RegistrationPhase2Poster extends GenericObjectPostLoader<RegistrationResponse>{

	/**
	 * 
	 * @param post Info of registration. Provides name and email and
	 * selected home province latitude and longitude.
	 */
	
	public RegistrationPhase2Poster(RegistrationDTO post) {
		super(RegistrationResponse.class, post, Constants.REGISTRATION_PHASE2);
	}
	
	/**
	 * Override this to handle registration response from server.
	 */

	@Override
	abstract public void handleResponseObject(RegistrationResponse responseObject);
}
