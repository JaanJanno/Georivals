package ee.bmagrupp.georivals.mobile.core.communications.loaders.registration;

import ee.bmagrupp.georivals.mobile.core.communications.Constants;
import ee.bmagrupp.georivals.mobile.core.communications.loaders.GenericObjectLoader;
import ee.bmagrupp.georivals.mobile.models.ServerResponse;

/**
 * Class for handling the first phase of registration.
 * 
 * @author Jaan Janno
 * 
 */

public abstract class RegistrationPhase1Poster extends
		GenericObjectLoader<ServerResponse> {

	/**
	 * 
	 * @param userName User name the user wants to select.
	 */

	public RegistrationPhase1Poster(String userName) {
		super(ServerResponse.class, Constants.REGISTRATION_PHASE1);
		addParamter("userName", userName);
	}

	/**
	 * Override this to handle registration response from server.
	 */

	@Override
	abstract public void handleResponseObject(ServerResponse responseObject);
}
