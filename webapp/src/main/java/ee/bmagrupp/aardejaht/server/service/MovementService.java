package ee.bmagrupp.aardejaht.server.service;

import java.util.List;

import ee.bmagrupp.aardejaht.server.rest.domain.MovementSelectionViewDTO;

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
}
