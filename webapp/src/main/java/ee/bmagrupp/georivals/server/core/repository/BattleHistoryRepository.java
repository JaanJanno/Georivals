package ee.bmagrupp.georivals.server.core.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import ee.bmagrupp.georivals.server.core.domain.BattleHistory;
import ee.bmagrupp.georivals.server.core.domain.Player;

/**
 * Returns {@link BattleHistory}'s. That's what the name says.
 * 
 * @author Sander
 *
 */

public interface BattleHistoryRepository extends
		CrudRepository<BattleHistory, Integer> {

	@Query("from BattleHistory as o where (o.attacker.sid = ?1) or (o.defender.sid = ?1)")
	List<BattleHistory> findBySid(String sid);

	/**
	 * Finds all battles where the {@link Player} with this sid has fought.
	 * Returns battles by newest first.
	 * 
	 * @author TKasekamp
	 * @param sid
	 *            Cookie value
	 * @return {@link BattleHistory} list
	 */
	@Query("from BattleHistory as o where (o.attacker.sid = ?1) or (o.defender.sid = ?1) order by date desc")
	List<BattleHistory> findBySidSortByDate(String sid);

}
