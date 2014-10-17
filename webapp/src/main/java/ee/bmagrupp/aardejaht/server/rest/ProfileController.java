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

import ee.bmagrupp.aardejaht.server.core.domain.Player;
import ee.bmagrupp.aardejaht.server.core.old.TestUser;
import ee.bmagrupp.aardejaht.server.rest.domain.PlayerProfile;
import ee.bmagrupp.aardejaht.server.service.ProfileService;
import ee.bmagrupp.aardejaht.server.service.UserService;

@RestController
@RequestMapping("/profile")
public class ProfileController {

	private static Logger LOG = LoggerFactory.getLogger(UserController.class);
	@Autowired
	ProfileService profServ;

	@RequestMapping(method = RequestMethod.GET, value = "/{id}")
	public ResponseEntity<PlayerProfile> getById(@PathVariable int id) {

		LOG.info("find player");
		PlayerProfile user = profServ.getPlayer(id);
		return new ResponseEntity<PlayerProfile>(user, HttpStatus.ACCEPTED);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/special")
	public String createSample() {
		LOG.info("create data");
		profServ.createSampleData();
		return "tere";
	}

	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<List<Player>> getAll() {
		LOG.info("all players");
		List<Player> players = profServ.findAll();
		return new ResponseEntity<List<Player>>(players, HttpStatus.ACCEPTED);
	}
}
