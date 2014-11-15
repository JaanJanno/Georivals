package ee.bmagrupp.georivals.server.service;

import java.util.List;

import ee.bmagrupp.georivals.server.core.domain.HomeOwnership;
import ee.bmagrupp.georivals.server.core.domain.Movement;
import ee.bmagrupp.georivals.server.core.domain.Ownership;
import ee.bmagrupp.georivals.server.core.domain.Player;
import ee.bmagrupp.georivals.server.core.domain.Province;
import ee.bmagrupp.georivals.server.core.domain.Unit;
import ee.bmagrupp.georivals.server.rest.domain.BeginMovementDTO;
import ee.bmagrupp.georivals.server.rest.domain.BeginMovementResponse;
import ee.bmagrupp.georivals.server.rest.domain.MovementSelectionViewDTO;
import ee.bmagrupp.georivals.server.rest.domain.MovementViewDTO;
import ee.bmagrupp.georivals.server.rest.domain.ServerResponse;
import ee.bmagrupp.georivals.server.util.ServerResult;

/**
 * Everything to do with moving units around.
 * 
 * @author TKasekamp
 *
 */
public interface MovementService {

	/**
	 * Creates a list of units for this player that:<br>
	 * 1) Aren't moving. 2) Have a size greater then 1 if they are based in a
	 * non-home province. 3) Aren't under attack (this may not be true).
	 * 
	 * @param cookie
	 *            Cookie value
	 * @return List of {@link MovementSelectionViewDTO}
	 */
	List<MovementSelectionViewDTO> getMyUnits(String cookie);

	/**
	 * Begins a Movement with the selected units.
	 * 
	 * @param lat
	 *            Destination {@link Province} latitude
	 * @param lon
	 *            Destination {@link Province} longitude
	 * @param beginMoveList
	 * @param cookie
	 *            Cookie value
	 * @return {@link BeginMovementResponse}
	 */
	BeginMovementResponse moveUnitsTo(String lat, String lon,
			List<BeginMovementDTO> beginMoveList, String cookie);

	/**
	 * All current {@link Unit} {@link Movement}'s for this {@link Player}.
	 * 
	 * @param cookie
	 *            Cookie value
	 * @return List of {@link MovementViewDTO}
	 */
	List<MovementViewDTO> getMyMovements(String cookie);

	/**
	 * Claims the new {@link Unit}'s at this location. If successful, will
	 * return {@link ServerResult#OK} and {@link ServerResponse#getId()} as the
	 * number of claimed units. The {@link Ownership#getLastVisit()} or
	 * {@link HomeOwnership#getLastVisit()} will be updated to current date. If
	 * unsuccessful, will return {@link ServerResult#FAIL}.
	 * 
	 * @param lat
	 *            {@link Province} latitude
	 * @param lon
	 *            {@link Province} longitude
	 * @param cookie
	 *            Cookie value
	 * @return {@link ServerResponse}
	 */
	ServerResponse claimUnits(String lat, String lon, String cookie);
}
