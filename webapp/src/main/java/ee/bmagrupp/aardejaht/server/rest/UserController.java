package ee.bmagrupp.aardejaht.server.rest;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ee.bmagrupp.aardejaht.server.core.old.User;
import ee.bmagrupp.aardejaht.server.repository.UserService;

@RestController
@RequestMapping("/user")
public class UserController {

	private static Logger LOG = LoggerFactory.getLogger(UserController.class);
	@Autowired
	UserService userServ;

	@RequestMapping(method = RequestMethod.GET, value = "/id/{id}")
	public ResponseEntity<User> getById(@PathVariable int id) {
		User user = userServ.findById(id);
		return new ResponseEntity<User>(user, HttpStatus.ACCEPTED);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/email/{email}")
	public ResponseEntity<User> getByEmail(@PathVariable String email) {
		User user = userServ.findByEmail(email);
		return new ResponseEntity<User>(user, HttpStatus.ACCEPTED);
	}

	@RequestMapping(method = RequestMethod.POST, value = "/{email}/{username}")
	public ResponseEntity<User> insertUser(@PathVariable String email,
			@PathVariable String username) {
		User user = new User(email, username);
		userServ.save(user);
		return new ResponseEntity<User>(user, HttpStatus.ACCEPTED);
	}

	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<List<User>> getAll() {
		List<User> users = (List<User>) userServ.findAll();
		return new ResponseEntity<List<User>>(users, HttpStatus.ACCEPTED);
	}

	// login stuff
}
