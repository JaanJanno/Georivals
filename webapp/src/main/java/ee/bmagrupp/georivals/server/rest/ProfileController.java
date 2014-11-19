package ee.bmagrupp.georivals.server.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ee.bmagrupp.georivals.server.rest.domain.PlayerProfile;
import ee.bmagrupp.georivals.server.service.ProfileService;

@RestController
@RequestMapping("/profile")
public class ProfileController {

	private static Logger LOG = LoggerFactory
			.getLogger(ProfileController.class);
	@Autowired
	ProfileService profServ;

	@RequestMapping(method = RequestMethod.GET, value = "/id/{id}")
	public ResponseEntity<PlayerProfile> getById(@PathVariable int id) {

		LOG.debug("find player " + id);
		PlayerProfile player = profServ.getPlayerProfile(id);
		return new ResponseEntity<PlayerProfile>(player, HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<PlayerProfile> getProfileWithCookie(
			@CookieValue(value = "sid") String cookie) {
		LOG.debug("Get player with cookie " + cookie);
		if (cookie.equals("")) {
			return new ResponseEntity<PlayerProfile>(HttpStatus.FORBIDDEN);
		}
		PlayerProfile player = profServ.getPlayerProfile(cookie);
		return new ResponseEntity<PlayerProfile>(player, HttpStatus.OK);
	}
}
