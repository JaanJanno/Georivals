package ee.bmagrupp.aardejaht.server.core.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import ee.bmagrupp.aardejaht.server.core.domain.Province;

public interface ProvinceRepository extends CrudRepository<Province, Integer> {

	@Query("from Province p where p.latitude = ?1 and p.longitude = ?2")
	Province findWithLatLong(double latitude, double longitude);

}
