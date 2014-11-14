package ee.bmagrupp.georivals.server.core.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import ee.bmagrupp.georivals.server.core.domain.Ownership;
import ee.bmagrupp.georivals.server.core.domain.Player;
import ee.bmagrupp.georivals.server.core.domain.Province;

/**
 * @author TKasekamp
 */
public interface PlayerRepository extends CrudRepository<Player, Integer> {

	Player findByEmail(String email);

	Player findBySid(String sid);

	/**
	 * Finds the {@link Player} who is connected to this {@link Ownership}.
	 * 
	 * @param ownershipId
	 * @return {@link Player}
	 */
	@Query("from Player p left join p.ownedProvinces op where op.id = ?1")
	Player findOwner(int ownershipId);

	Player findByUserName(String userName);

	/**
	 * Finds the {@link Player} who owns the {@link Province} specified by the
	 * id.
	 * 
	 * @param provinceId
	 * @return {@link Player}
	 */
	@Query("from Player p left join p.ownedProvinces op left join op.province prov where prov.id = ?1")
	Player findOwnerOfProvince(int provinceId);
}
