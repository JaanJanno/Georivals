package ee.bmagrupp.georivals.server.service;

import java.util.List;

import ee.bmagrupp.georivals.server.rest.domain.BattleHistoryDTO;

/**
 * @author TKasekamp
 */
public interface BattleService {

	/**
	 * Get the history of battles for this player.
	 * 
	 * @param cookie
	 * @return List of {@link BattleHistoryDTO}
	 */
	List<BattleHistoryDTO> getBattles(String cookie);
}
