package ee.bmagrupp.aardejaht.server.rest;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ee.bmagrupp.aardejaht.server.rest.domain.MovementSelectionViewDTO;
import ee.bmagrupp.aardejaht.server.service.MovementService;

@RestController
@RequestMapping("/movement")
public class MovementController {

	private static final Logger LOG = LoggerFactory
			.getLogger(MovementController.class);

	@Autowired
	MovementService moveServ;

	@RequestMapping(method = RequestMethod.GET, value = "/myunits")
	public ResponseEntity<List<MovementSelectionViewDTO>> getMyUnits(
			@CookieValue(value = "sid") String cookie) {
		LOG.info("Creating list of units to move");
		LOG.info(cookie);
		List<MovementSelectionViewDTO> units = moveServ.getMyUnits(cookie);
		return new ResponseEntity<List<MovementSelectionViewDTO>>(units,
				HttpStatus.OK);

	}

}
