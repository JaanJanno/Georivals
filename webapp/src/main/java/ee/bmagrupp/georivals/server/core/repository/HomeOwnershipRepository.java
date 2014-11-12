package ee.bmagrupp.georivals.server.core.repository;

import org.springframework.data.repository.CrudRepository;

import ee.bmagrupp.georivals.server.core.domain.HomeOwnership;

public interface HomeOwnershipRepository extends
		CrudRepository<HomeOwnership, Integer> {

}
