package ee.bmagrupp.georivals.server.core.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import ee.bmagrupp.georivals.server.core.domain.HomeOwnership;
import ee.bmagrupp.georivals.server.core.domain.Ownership;
import ee.bmagrupp.georivals.server.core.domain.Player;
import ee.bmagrupp.georivals.server.core.domain.Province;
import ee.bmagrupp.georivals.server.core.domain.Unit;

public interface HomeOwnershipRepository extends
		CrudRepository<HomeOwnership, Integer> {

	/**
	 * Finds the {@link HomeOwnership} where the {@link Unit} with this id is.
	 * ONLY LOOKS AT {@link HomeOwnership}. To search in {@link Ownership}, use
	 * {@link OwnershipRepository#findUnitLocation(int)}
	 * 
	 * @param unitId
	 * @return {@link HomeOwnership}
	 */
	@Query("from HomeOwnership o left join o.units u where u.id = ?1")
	HomeOwnership findHomeUnitLocation(int unitId);

	/**
	 * Finds the {@link HomeOwnership} with this {@link Province}. Also checks
	 * that this {@link HomeOwnership} actually belongs to the {@link Player}
	 * with this sid. To check if a {@link Ownership} belongs to a
	 * {@link Player}, look at
	 * {@link OwnershipRepository#findProvinceOfPlayer(double, double, String)}<br>
	 * 
	 * This query might be a bit inefficient.
	 * 
	 * @param latitude
	 * @param longitude
	 * @param cookie
	 *            SID of {@link Player}
	 * @return {@link HomeOwnership} if found
	 */
	@Query("select home from Player p where p.sid = ?3 and p.home.province.longitude =?2 and p.home.province.latitude = ?1")
	HomeOwnership findHomeProvinceOfPlayer(double latitude, double longitude,
			String cookie);
}
