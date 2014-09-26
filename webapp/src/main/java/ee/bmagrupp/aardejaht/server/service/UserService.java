package ee.bmagrupp.aardejaht.server.service;

import java.util.List;

import ee.bmagrupp.aardejaht.server.domain.User;

public interface UserService {

	public User getUserById(int ID);
	public List<User> getUsers();
}
