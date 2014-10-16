package ee.bmagrupp.aardejaht.server.service;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import ee.bmagrupp.aardejaht.server.core.old.TestUser;

/**
 * This is a temporary repository for Profile view. Will be removed.
 * 
 * @author TKasekamp
 *
 */
public interface UserService extends CrudRepository<TestUser, Long> {

	TestUser findByEmail(String email);

	TestUser findById(int id);

	List<TestUser> findAll();
}
