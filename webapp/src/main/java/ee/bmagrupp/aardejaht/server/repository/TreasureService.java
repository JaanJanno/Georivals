package ee.bmagrupp.aardejaht.server.repository;

import org.springframework.data.repository.CrudRepository;

import ee.bmagrupp.aardejaht.server.domain.Treasure;

public interface TreasureService extends CrudRepository<Treasure, Long> {

	Treasure findById(int id);

}
