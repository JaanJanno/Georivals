package ee.bmagrupp.aardejaht.server.repository;

import org.springframework.data.repository.CrudRepository;

import ee.bmagrupp.aardejaht.server.core.old.Trek;

public interface TrekService extends CrudRepository<Trek, Long> {

	Trek findById(int id);

}
