package ee.bmagrupp.aardejaht.server.core.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import ee.bmagrupp.aardejaht.server.core.domain.Player;
import ee.bmagrupp.aardejaht.server.core.domain.Province;

/**
 * @author TKasekamp
 */
public interface PlayerRepository extends CrudRepository<Player, Integer> {

	Player findByEmail(String email);

	Player findBySid(String sid);

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
