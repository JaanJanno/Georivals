package ee.bmagrupp.georivals.server.game;

import java.util.Date;

import ee.bmagrupp.georivals.server.core.domain.HomeOwnership;
import ee.bmagrupp.georivals.server.core.domain.Movement;
import ee.bmagrupp.georivals.server.core.domain.Ownership;
import ee.bmagrupp.georivals.server.core.domain.Unit;

/**
 * Class that will handle the end of {@link Movement}. This may be adding
 * {@link Unit}'s to an existing province or creating a battle.
 * 
 * @author TKasekamp
 *
 */
public interface EndMovementService {

	/**
	 * End this {@link Movement} by either adding the {@link Unit} to an
	 * existing {@link Ownership} or {@link HomeOwnership} or by creating a
	 * battle.
	 * 
	 * @param endDate
	 */
	void handleMovement(Date endDate);

	/**
	 * Get the endDate of the next {@link Movement} from the database.
	 * 
	 * @return {@link Date} when found. null when no movements
	 */
	Date getNextMovement();
}
