package ee.bmagrupp.georivals.server.core.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import ee.bmagrupp.georivals.server.core.domain.HomeOwnership;
import ee.bmagrupp.georivals.server.core.domain.Ownership;
import ee.bmagrupp.georivals.server.core.domain.Province;
import ee.bmagrupp.georivals.server.core.domain.Unit;

/**
 * Returns {@link Province}'s. That's what the name says.
 * 
 * @author TKasekamp
 *
 */
public interface ProvinceRepository extends CrudRepository<Province, Integer> {

	/**
	 * Finds a province with these exact coordinates.
	 * 
	 * @param latitude
	 *            For Tartu something like 58.37
	 * @param longitude
	 *            For Tartu something like 26.72
	 * @return {@link Province} if found. null if not found
	 */
	@Query("from Province p where (p.latitude between ?1-0.00001 and ?1+0.00001) and (p.longitude between ?2-0.0001 and ?2+0.0001)")
	Province findWithLatLong(double latitude, double longitude);

	/**
	 * Finds the {@link Province} where the {@link Unit} with this id is. ONLY
	 * LOOKS AT {@link Ownership}. To search in {@link HomeOwnership}, use
	 * {@link #findHomeUnitLocation(int)}
	 * 
	 * @param unitId
	 * @return {@link Province}
	 */
	@Query("select o.province from Ownership o left join o.units u where u.id = ?1")
	Province findUnitLocation(int unitId);

	/**
	 * Finds the {@link Province} where the {@link Unit} with this id is. ONLY
	 * LOOKS AT {@link HomeOwnership}. To search in {@link Ownership}, use
	 * {@link #findUnitLocation(int))}
	 * 
	 * @param unitId
	 * @return {@link Province}
	 */
	@Query("select o.province from HomeOwnership o left join o.units u where u.id = ?1")
	Province findHomeUnitLocation(int unitId);

}
