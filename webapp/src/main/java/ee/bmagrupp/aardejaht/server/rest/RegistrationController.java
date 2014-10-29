package ee.bmagrupp.aardejaht.server.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ee.bmagrupp.aardejaht.server.rest.domain.RegistrationDTO;
import ee.bmagrupp.aardejaht.server.rest.domain.ServerResponse;
import ee.bmagrupp.aardejaht.server.service.AuthenticationService;

@RestController
@RequestMapping("/registration")
public class RegistrationController {

	private static Logger LOG = LoggerFactory
			.getLogger(RegistrationController.class);

	@Autowired
	AuthenticationService authServ;

	@RequestMapping(method = RequestMethod.POST, value = "/phase1")
	public ResponseEntity<ServerResponse> registrationPhase1(
			@RequestBody RegistrationDTO registration) {
		LOG.info("Registration phase 1");
		ServerResponse response = authServ.registrationPhase1(registration);
		return new ResponseEntity<ServerResponse>(response, HttpStatus.OK);
	}

}
