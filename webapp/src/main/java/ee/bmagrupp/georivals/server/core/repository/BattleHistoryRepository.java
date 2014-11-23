package ee.bmagrupp.georivals.server.core.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import ee.bmagrupp.georivals.server.core.domain.BattleHistory;

/**
 * Returns {@link BattleHistory}'s. That's what the name says.
 * 
 * @author Sander
 *
 */

public interface BattleHistoryRepository extends CrudRepository<BattleHistory,Integer> {
	
	@Query("from BattleHistory as o where (o.attacker.sid = ?1) or (o.defender.sid = ?1)")
	List<BattleHistory> findBySid(String sid);
	
}
