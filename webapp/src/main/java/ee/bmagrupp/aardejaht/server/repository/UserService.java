package ee.bmagrupp.aardejaht.server.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import ee.bmagrupp.aardejaht.server.core.old.User;

public interface UserService extends CrudRepository<User, Long> {

	User findByEmail(String email);

	User findById(int id);

	List<User> findAll();
}
