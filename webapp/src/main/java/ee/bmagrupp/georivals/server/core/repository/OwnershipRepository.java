package ee.bmagrupp.georivals.server.core.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import ee.bmagrupp.georivals.server.core.domain.HomeOwnership;
import ee.bmagrupp.georivals.server.core.domain.Ownership;
import ee.bmagrupp.georivals.server.core.domain.Player;
import ee.bmagrupp.georivals.server.core.domain.Province;
import ee.bmagrupp.georivals.server.core.domain.Unit;

/**
 * @author TKasekamp
 */
public interface OwnershipRepository extends CrudRepository<Ownership, Integer> {

	/**
	 * Query for finding owned provinces between these coordinates. Example
	 * query<br>
	 * <code>from Ownership as o where (o.province.latitude between 58.37 and 58.40) and (o.province.longitude between 26.72 and 26.75)</code>
	 * .
	 * 
	 * @param lat1
	 *            First and smaller latitude coordinate
	 * @param long1
	 *            First and smaller longitude coordinate
	 * @param lat2
	 *            Second and bigger latitude coordinate
	 * @param long2
	 *            Second and bigger longitude coordinate
	 * @return List of {@link Ownership} that have {@link Province} between
	 *         these coordinates
	 */
	@Query("from Ownership as o where (o.province.latitude between ?1 and ?3) and (o.province.longitude between ?2 and ?4)")
	List<Ownership> findBetween(double lat1, double long1, double lat2,
			double long2);

	/**
	 * Returns the {@link Ownership} of the {@link Province} with this id.
	 * 
	 * @param provinceId
	 * @return {@link Ownership}
	 */
	@Query("from Ownership as o where o.province.id = ?1")
	Ownership findByProvinceId(int provinceId);

	/**
	 * Finds the {@link Ownership} where the {@link Unit} with this id is. ONLY
	 * LOOKS AT {@link Ownership}. To search in {@link HomeOwnership}, look at
	 * {@link HomeOwnershipRepository#findHomeUnitLocation(int)}
	 * 
	 * @param unitId
	 * @return {@link Ownership}. null if not found
	 */
	@Query("from Ownership o left join o.units u where u.id = ?1")
	Ownership findUnitLocation(int unitId);

	/**
	 * Finds the {@link Ownership} with this {@link Province}. Also checks that
	 * this {@link Ownership} actually belongs to the {@link Player} with this
	 * sid. To check if a {@link HomeOwnership} belongs to a {@link Player},
	 * look at
	 * {@link HomeOwnershipRepository#findHomeProvinceOfPlayer(double, double, String)}
	 * <br>
	 * 
	 * This query might be a bit inefficient.
	 * 
	 * @param latitude
	 * @param longitude
	 * @param cookie
	 *            SID of {@link Player}
	 * @return {@link Ownership}. null if not found
	 */
	@Query("select op from Player p left join p.ownedProvinces op left join op.province prov where p.sid = ?3 and prov.longitude =?2 and prov.latitude = ?1")
	Ownership findProvinceOfPlayer(double latitude, double longitude,
			String cookie);
}
