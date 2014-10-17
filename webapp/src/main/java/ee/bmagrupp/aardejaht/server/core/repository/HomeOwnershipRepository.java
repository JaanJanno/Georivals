package ee.bmagrupp.aardejaht.server.core.repository;

import org.springframework.data.repository.CrudRepository;

import ee.bmagrupp.aardejaht.server.core.domain.HomeOwnership;

public interface HomeOwnershipRepository extends
		CrudRepository<HomeOwnership, Integer> {

}
