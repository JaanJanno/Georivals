package ee.bmagrupp.aardejaht.server.core.domain;

/**
 * Marks the state of the Unit.
 * 
 * @author TKasekamp
 *
 */
public enum UnitState {
	/**
	 * Owned by a Player.
	 */
	CLAIMED,
	/**
	 * In a Player's province, but not yet claimed by the Player.
	 */
	UNCLAIMED
}
