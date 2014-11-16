package ee.bmagrupp.georivals.server.core.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import ee.bmagrupp.georivals.server.core.domain.Movement;
import ee.bmagrupp.georivals.server.core.domain.Player;
import ee.bmagrupp.georivals.server.core.domain.Province;

/**
 * @author TKasekamp
 */
public interface MovementRepository extends CrudRepository<Movement, Integer> {

	/**
	 * Finds all the {@link Movement}'s where the destination {@link Province}
	 * has this id.
	 * 
	 * @param provinceId
	 * @return {@code List<Movement>}
	 */
	@Query("from Movement m where m.destination.id = ?1")
	List<Movement> findByDestination(int provinceId);

	/**
	 * Checks if the {@link Province} with this id is the destination of any
	 * {@link Movement}.
	 * 
	 * @param provinceId
	 * @return {@code true} if destination. {@code false} otherwise.
	 */
	@Query("select case when (count(m) > 0) then true else false end from Movement m where m.destination.id = ?1")
	boolean checkIfDestination(int provinceId);

	/**
	 * Finds all the {@link Movement}'s of the {@link Player} with this sid.
	 * 
	 * @param sid
	 *            {@link Player#getSid()}
	 * @return {@code List<Movement>}
	 */
	@Query("from Movement m where m.player.sid = ?1")
	List<Movement> findByPlayerSid(String sid);

}
