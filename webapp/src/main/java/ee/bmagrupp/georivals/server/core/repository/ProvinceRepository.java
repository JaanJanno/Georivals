package ee.bmagrupp.georivals.server.core.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import ee.bmagrupp.georivals.server.core.domain.Province;

/**
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
	@Query("from Province p where p.latitude = ?1 and p.longitude = ?2")
	Province findWithLatLong(double latitude, double longitude);

}
