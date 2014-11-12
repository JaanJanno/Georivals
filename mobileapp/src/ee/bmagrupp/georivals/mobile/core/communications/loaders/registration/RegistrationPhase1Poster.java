package ee.bmagrupp.georivals.mobile.core.communications.loaders.registration;

import ee.bmagrupp.georivals.mobile.core.communications.Constants;
import ee.bmagrupp.georivals.mobile.core.communications.loaders.GenericObjectPostLoader;
import ee.bmagrupp.georivals.mobile.models.RegistrationDTO;
import ee.bmagrupp.georivals.mobile.models.RegistrationResponse;

/**
 * Class for handling the first phase of registration.
 * @author Jaan Janno
 *
 */

public abstract class RegistrationPhase1Poster extends GenericObjectPostLoader<RegistrationResponse>{

	/**
	 * 
	 * @param post Info of registration. Provides name and email.
	 */
	
	public RegistrationPhase1Poster(RegistrationDTO post) {
		super(RegistrationResponse.class, post, Constants.REGISTRATION_PHASE1);
	}
	
	/**
	 * Override this to handle registration response from server.
	 */

	@Override
	abstract public void handleResponseObject(RegistrationResponse responseObject);
}