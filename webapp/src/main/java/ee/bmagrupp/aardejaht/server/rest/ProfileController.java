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

import ee.bmagrupp.aardejaht.server.rest.domain.PlayerProfile;
import ee.bmagrupp.aardejaht.server.service.ProfileService;

@RestController
@RequestMapping("/profile")
public class ProfileController {

	private static Logger LOG = LoggerFactory
			.getLogger(ProfileController.class);
	@Autowired
	ProfileService profServ;

	@RequestMapping(method = RequestMethod.GET, value = "/id/{id}")
	public ResponseEntity<PlayerProfile> getById(@PathVariable int id) {

		LOG.info("find player");
		PlayerProfile user = profServ.getPlayer(id);
		return new ResponseEntity<PlayerProfile>(user, HttpStatus.ACCEPTED);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/special")
	public String createSample() {
		LOG.info("create sample data");
		// profServ.createSampleData();
		return "This is a legacy method for generating sample data. Go to ProfileController to activate it.";
	}

	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<List<PlayerProfile>> getAll() {
		LOG.info("Get all player profiles");
		List<PlayerProfile> players = profServ.findAll();
		return new ResponseEntity<List<PlayerProfile>>(players,
				HttpStatus.ACCEPTED);
	}
}
