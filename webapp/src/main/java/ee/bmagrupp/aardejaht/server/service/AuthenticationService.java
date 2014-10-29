package ee.bmagrupp.aardejaht.server.service;

import ee.bmagrupp.aardejaht.server.rest.domain.RegistrationDTO;
import ee.bmagrupp.aardejaht.server.rest.domain.ServerResponse;
import ee.bmagrupp.aardejaht.server.util.ServerResult;

/**
 * Interface that will handle the authentication and registration of players.
 * 
 * @author TKasekamp
 *
 */
public interface AuthenticationService {

	/**
	 * Checks if a player with this username is present in the database.
	 * 
	 * @param registration
	 *            {@link RegistrationDTO} with username that is not null.
	 * @return {@link ServerResponse} with result {@link ServerResult} OK if
	 *         this username is available. <br>
	 *         Otherwise a {@link ServerResponse} with the value as an error
	 *         message.
	 */
	public ServerResponse registrationPhase1(RegistrationDTO registration);

	public ServerResponse registrationPhase2(RegistrationDTO registration);
}
