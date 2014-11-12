package ee.bmagrupp.aardejaht.server.service;

import java.util.List;

import ee.bmagrupp.aardejaht.server.rest.domain.BattleHistoryDTO;

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
