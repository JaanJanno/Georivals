package ee.bmagrupp.aardejaht.server.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


import ee.bmagrupp.aardejaht.server.domain.User;

@RestController
@RequestMapping("/user")
public class UserController {
	
	  @RequestMapping(method = RequestMethod.GET, value = "/id/{id}")
	  public ResponseEntity<User> get(@PathVariable String id) {
//	    Speaker speaker = speakerService.findById(id);
	    return new ResponseEntity<User>(new User("sdfadf"), HttpStatus.ACCEPTED);
	  }

	  
	  @RequestMapping(method = RequestMethod.GET, value = "/email/{email}")
	  public ResponseEntity<User> getByEmail(@PathVariable String email) {
//	    Speaker speaker = speakerService.findById(id);
	    return new ResponseEntity<User>(new User("sdfadf"), HttpStatus.ACCEPTED);
	  }
	  
	  //login stuff
}
