package ee.bmagrupp.georivals.server.rest;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ee.bmagrupp.georivals.server.rest.domain.BeginMovementDTO;
import ee.bmagrupp.georivals.server.rest.domain.BeginMovementResponse;
import ee.bmagrupp.georivals.server.rest.domain.MovementSelectionViewDTO;
import ee.bmagrupp.georivals.server.rest.domain.MovementViewDTO;
import ee.bmagrupp.georivals.server.rest.domain.ServerResponse;
import ee.bmagrupp.georivals.server.service.MovementService;

@RestController
@RequestMapping("/movement")
public class MovementController {

	private static final Logger LOG = LoggerFactory
			.getLogger(MovementController.class);

	@Autowired
	MovementService moveServ;

	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<List<MovementViewDTO>> getMyMovements(
			@CookieValue(value = "sid") String cookie) {
		LOG.info("Creating list of player movements for " + cookie);
		if (cookie.equals("")) {
			return new ResponseEntity<List<MovementViewDTO>>(
					HttpStatus.FORBIDDEN);
		}
		List<MovementViewDTO> movements = moveServ.getMyMovements(cookie);
		return new ResponseEntity<List<MovementViewDTO>>(movements,
				HttpStatus.OK);

	}

	@RequestMapping(method = RequestMethod.GET, value = "/myunits")
	public ResponseEntity<List<MovementSelectionViewDTO>> getMyUnits(
			@CookieValue(value = "sid") String cookie) {
		LOG.info("Creating list of units to move for " + cookie);
		if (cookie.equals("")) {
			return new ResponseEntity<List<MovementSelectionViewDTO>>(
					HttpStatus.FORBIDDEN);
		}
		List<MovementSelectionViewDTO> units = moveServ.getMyUnits(cookie);
		return new ResponseEntity<List<MovementSelectionViewDTO>>(units,
				HttpStatus.OK);

	}

	@RequestMapping(method = RequestMethod.GET, value = "/myunits/exclude")
	public ResponseEntity<List<MovementSelectionViewDTO>> getMyUnits(
			@RequestParam(value = "latitude") String latitude,
			@RequestParam(value = "longitude") String longitude,
			@CookieValue(value = "sid") String cookie) {
		LOG.info("MYUNITS: Creating list of units to move for " + cookie);
		LOG.info("MYUNITS: excluding " + latitude + " and " + longitude);
		if (cookie.equals("")) {
			return new ResponseEntity<List<MovementSelectionViewDTO>>(
					HttpStatus.FORBIDDEN);
		}
		List<MovementSelectionViewDTO> units = moveServ.getMyUnits(latitude,
				longitude, cookie);
		return new ResponseEntity<List<MovementSelectionViewDTO>>(units,
				HttpStatus.OK);

	}

	@RequestMapping(method = RequestMethod.POST, value = "/to")
	public ResponseEntity<BeginMovementResponse> moveTo(
			@RequestParam(value = "latitude") String latitude,
			@RequestParam(value = "longitude") String longitude,
			@RequestBody List<BeginMovementDTO> beginMoveList,
			@CookieValue(value = "sid") String cookie) {
		LOG.info("Moving units for " + cookie);
		LOG.info(beginMoveList.toString());
		if (cookie.equals("")) {
			return new ResponseEntity<BeginMovementResponse>(
					HttpStatus.FORBIDDEN);
		}
		BeginMovementResponse response = moveServ.moveUnitsTo(latitude,
				longitude, beginMoveList, cookie);
		return new ResponseEntity<BeginMovementResponse>(response,
				HttpStatus.OK);

	}

	@RequestMapping(method = RequestMethod.POST, value = "/claim")
	public ResponseEntity<ServerResponse> claimUnits(
			@RequestParam(value = "latitude") String latitude,
			@RequestParam(value = "longitude") String longitude,
			@CookieValue(value = "sid") String cookie) {
		LOG.info("Claim units at " + latitude + ", " + longitude + " for "
				+ cookie);
		if (cookie.equals("")) {
			return new ResponseEntity<ServerResponse>(HttpStatus.FORBIDDEN);
		}
		ServerResponse response = moveServ.claimUnits(latitude, longitude,
				cookie);
		return new ResponseEntity<ServerResponse>(response, HttpStatus.OK);

	}

}
