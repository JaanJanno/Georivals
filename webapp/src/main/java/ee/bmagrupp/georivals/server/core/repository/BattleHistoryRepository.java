package ee.bmagrupp.georivals.server.core.repository;

import org.springframework.data.repository.CrudRepository;

import ee.bmagrupp.georivals.server.core.domain.BattleHistory;

/**
 * Returns {@link BattleHistory}'s. That's what the name says.
 * 
 * @author Sander
 *
 */

public interface BattleHistoryRepository extends CrudRepository<BattleHistory,Integer> {

	
	
}
