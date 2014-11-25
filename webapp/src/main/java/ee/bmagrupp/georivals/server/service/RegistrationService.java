package ee.bmagrupp.georivals.server.service;

import ee.bmagrupp.georivals.server.core.domain.HomeOwnership;
import ee.bmagrupp.georivals.server.core.domain.Player;
import ee.bmagrupp.georivals.server.core.domain.Province;
import ee.bmagrupp.georivals.server.core.domain.Unit;
import ee.bmagrupp.georivals.server.rest.domain.RegistrationDTO;
import ee.bmagrupp.georivals.server.rest.domain.ServerResponse;
import ee.bmagrupp.georivals.server.util.Constants;
import ee.bmagrupp.georivals.server.util.ServerResult;

/**
 * Interface that will handle the authentication and registration of players.
 * 
 * @author TKasekamp
 *
 */
public interface RegistrationService {

	/**
	 * Checks if a player with this username is present in the database.<br>
	 * {@link ServerResponse} with result {@link ServerResult} OK if this
	 * username is available. <br>
	 * If this username exists in the database, a {@link ServerResponse} with
	 * {@link ServerResult} USERNAME_IN_USE is sent.
	 * 
	 * @param dto
	 *            {@link RegistrationDTO} with username that is not null.
	 * @return {@link ServerResponse}
	 */
	public ServerResponse registrationPhase1(RegistrationDTO dto);

	/**
	 * Checks if a player with this username is present in the database.<br>
	 * {@link ServerResponse} with result {@link ServerResult} OK if this
	 * username is available. <br>
	 * If this username exists in the database, a {@link ServerResponse} with
	 * {@link ServerResult} USERNAME_IN_USE is sent.
	 * 
	 * @param userName
	 *            userName to check
	 * @return {@link ServerResponse}
	 */
	public ServerResponse registrationPhase1(String userName);

	/**
	 * Inserts the user specified by the {@link RegistrationDTO} into the
	 * database. The home province is set to the province specified by the
	 * latitude and longitude of the {@link RegistrationDTO}.<br>
	 * {@link ServerResponse} with result {@link ServerResult} OK, if the user
	 * has been saved. value will be the player's sid and id will be the players
	 * id.<br>
	 * If this username exists in the database, a {@link ServerResponse} with
	 * {@link ServerResult} USERNAME_IN_USE is sent.
	 * 
	 * @param dto
	 *            {@link RegistrationDTO} with username, latitude and longitude
	 *            that are not null.
	 * @return {@link ServerResponse}
	 */
	public ServerResponse registrationPhase2(RegistrationDTO dto);

	/**
	 * 
	 * Inserts a new {@link Player} into the database. To do this, the
	 * {@link HomeOwnership} and home {@link Province} must be persisted. The
	 * province repository is checked if the province with homeLat and homeLong
	 * is already present. If not, a new {@link Province} is created. <br>
	 * A number of starting {@link Unit}'s specified in {@link Constants} is
	 * added to the home province.
	 * 
	 * @param username
	 * @param email
	 * @param homeLat
	 * @param homeLong
	 * @return {@link Player}
	 */
	public Player createPlayer(String username, String email, double homeLat,
			double homeLong);
}
