package ee.bmagrupp.georivals.mobile.core.communications.loaders.registration;

import ee.bmagrupp.georivals.mobile.core.communications.Constants;
import ee.bmagrupp.georivals.mobile.core.communications.loaders.GenericObjectPostLoader;
import ee.bmagrupp.georivals.mobile.models.ServerResponse;
import ee.bmagrupp.georivals.mobile.models.registration.RegistrationDTO;

/**
 * Class for handling the first phase of registration.
 * @author Jaan Janno
 *
 */

public abstract class RegistrationPhase1Poster extends GenericObjectPostLoader<ServerResponse>{

	/**
	 * 
	 * @param post Info of registration. Provides name and email.
	 */
	
	public RegistrationPhase1Poster(RegistrationDTO post) {
		super(ServerResponse.class, post, Constants.REGISTRATION_PHASE1);
	}
	
	/**
	 * Override this to handle registration response from server.
	 */

	@Override
	abstract public void handleResponseObject(ServerResponse responseObject);
}
