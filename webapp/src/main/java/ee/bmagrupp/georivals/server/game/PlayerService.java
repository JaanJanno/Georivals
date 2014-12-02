package ee.bmagrupp.georivals.server.game;

import ee.bmagrupp.georivals.server.core.domain.Player;

public interface PlayerService {

	/**
	 * Returns the total unit count of this player. Both stationary and moving.
	 * A placeholder method until I can write a proper database query.
	 * 
	 * @param player
	 *            {@link Player}
	 * @return
	 */
	public int calculatePlayerStrength(Player player);
}
