package ee.bmagrupp.aardejaht.server.service;

import ee.bmagrupp.aardejaht.server.rest.domain.RegistrationDTO;
import ee.bmagrupp.aardejaht.server.rest.domain.RegistrationResponse;
import ee.bmagrupp.aardejaht.server.util.ServerResult;

/**
 * Interface that will handle the authentication and registration of players.
 * 
 * @author TKasekamp
 *
 */
public interface AuthenticationService {

	/**
	 * Checks if a player with this username is present in the database.<br>
	 * {@link RegistrationResponse} with result {@link ServerResult} OK if this
	 * username is available. <br>
	 * If this username exists in the database, a {@link RegistrationResponse}
	 * with {@link ServerResult} USERNAME_IN_USE is sent.
	 * 
	 * @param dto
	 *            {@link RegistrationDTO} with username that is not null.
	 * @return {@link RegistrationResponse}
	 */
	public RegistrationResponse registrationPhase1(RegistrationDTO dto);

	/**
	 * Inserts the user specified by the {@link RegistrationDTO} into the
	 * database. The home province is set to the province specified by the
	 * latitude and longitude of the {@link RegistrationDTO}.<br>
	 * {@link RegistrationResponse} with result {@link ServerResult} OK, if the
	 * user has been saved. value will be the player's sid and id will be the
	 * players id.<br>
	 * If this username exists in the database, a {@link RegistrationResponse}
	 * with {@link ServerResult} USERNAME_IN_USE is sent.
	 * 
	 * @param dto
	 *            {@link RegistrationDTO} with username, latitude and longitude
	 *            that are not null.
	 * @return {@link RegistrationResponse}
	 */
	public RegistrationResponse registrationPhase2(RegistrationDTO dto);
}
