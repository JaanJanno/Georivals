package ee.bmagrupp.georivals.server.rest.domain;

public enum BattleType {
	/**
	 * Player attacked and won.
	 */
	ATTACK_PLAYER_WON,
	/**
	 * Player attacked and lost.
	 */
	ATTACK_PLAYER_LOST,
	/**
	 * Player defended and won.
	 */
	DEFENCE_PLAYER_WON,
	/**
	 * Player defended and lost.
	 */
	DEFENCE_PLAYER_LOST
}
