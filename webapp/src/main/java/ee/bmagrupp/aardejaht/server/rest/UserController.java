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

import ee.bmagrupp.aardejaht.server.core.old.TestUser;
import ee.bmagrupp.aardejaht.server.service.UserService;

@RestController
@RequestMapping("/user")
public class UserController {

	private static Logger LOG = LoggerFactory.getLogger(UserController.class);
	@Autowired
	UserService userServ;

	@RequestMapping(method = RequestMethod.GET, value = "/id/{id}")
	public ResponseEntity<TestUser> getById(@PathVariable int id) {
		TestUser user = userServ.findById(id);
		return new ResponseEntity<TestUser>(user, HttpStatus.ACCEPTED);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/email/{email}")
	public ResponseEntity<TestUser> getByEmail(@PathVariable String email) {
		TestUser user = userServ.findByEmail(email);
		return new ResponseEntity<TestUser>(user, HttpStatus.ACCEPTED);
	}

	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<List<TestUser>> getAll() {
		List<TestUser> users = (List<TestUser>) userServ.findAll();
		return new ResponseEntity<List<TestUser>>(users, HttpStatus.ACCEPTED);
	}

	// login stuff
}
