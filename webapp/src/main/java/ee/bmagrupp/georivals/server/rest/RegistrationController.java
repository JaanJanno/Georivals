package ee.bmagrupp.georivals.server.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ee.bmagrupp.georivals.server.rest.domain.RegistrationDTO;
import ee.bmagrupp.georivals.server.rest.domain.ServerResponse;
import ee.bmagrupp.georivals.server.service.RegistrationService;
import ee.bmagrupp.georivals.server.util.ServerResult;

@RestController
@RequestMapping("/registration")
public class RegistrationController {

	private static Logger LOG = LoggerFactory
			.getLogger(RegistrationController.class);

	@Autowired
	RegistrationService reghServ;

	@RequestMapping(method = RequestMethod.POST, value = "/phase1")
	public ResponseEntity<ServerResponse> registrationPhase1(
			@RequestBody RegistrationDTO registration) {
		LOG.info("Registration phase 1" + registration.toJson());
		ServerResponse response = reghServ
				.registrationPhase1(registration);
		return new ResponseEntity<ServerResponse>(response, HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.POST, value = "/phase2")
	public ResponseEntity<ServerResponse> registrationPhase2(
			@RequestBody RegistrationDTO registration) {
		LOG.info("Registration phase 2 " + registration.toJson());
		ServerResponse response = reghServ
				.registrationPhase2(registration);
		if (response.getResult() == ServerResult.OK) {
			return new ResponseEntity<ServerResponse>(response,
					HttpStatus.CREATED);
		} else {
			return new ResponseEntity<ServerResponse>(response,
					HttpStatus.OK);
		}

	}

}
