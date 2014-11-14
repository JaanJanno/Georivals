package ee.bmagrupp.georivals.server.core.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import ee.bmagrupp.georivals.server.core.domain.HomeOwnership;
import ee.bmagrupp.georivals.server.core.domain.Ownership;
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
}
