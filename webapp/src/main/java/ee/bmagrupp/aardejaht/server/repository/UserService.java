package ee.bmagrupp.aardejaht.server.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import ee.bmagrupp.aardejaht.server.core.old.TestUser;

public interface UserService extends CrudRepository<TestUser, Long> {

	TestUser findByEmail(String email);

	TestUser findById(int id);

	List<TestUser> findAll();
}
