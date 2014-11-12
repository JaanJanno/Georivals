package ee.bmagrupp.georivals.server.service;

import java.util.List;

import ee.bmagrupp.georivals.server.core.domain.Movement;
import ee.bmagrupp.georivals.server.core.domain.Player;
import ee.bmagrupp.georivals.server.core.domain.Province;
import ee.bmagrupp.georivals.server.core.domain.Unit;
import ee.bmagrupp.georivals.server.rest.domain.BeginMovementDTO;
import ee.bmagrupp.georivals.server.rest.domain.BeginMovementResponse;
import ee.bmagrupp.georivals.server.rest.domain.MovementSelectionViewDTO;
import ee.bmagrupp.georivals.server.rest.domain.MovementViewDTO;

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
}
