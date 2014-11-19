package ee.bmagrupp.georivals.server.rest;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import ee.bmagrupp.georivals.server.rest.domain.CameraFOV;
import ee.bmagrupp.georivals.server.rest.domain.ProvinceDTO;
import ee.bmagrupp.georivals.server.rest.domain.ServerResponse;
import ee.bmagrupp.georivals.server.service.ProvinceService;

@RestController
@RequestMapping("/province")
public class ProvinceController {

	private static Logger LOG = LoggerFactory
			.getLogger(ProvinceController.class);

	@Autowired
	ProvinceService provServ;

	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<List<ProvinceDTO>> getProvinces(
			@RequestBody CameraFOV fov,
			@CookieValue(value = "sid", defaultValue = "cookie") String cookie) {
		LOG.info("All provinces for " + cookie + " with " + fov.toJson());
		List<ProvinceDTO> provs = provServ.getProvinces(fov, cookie);
		LOG.info("The number of provs to return " + provs.size());
		return new ResponseEntity<List<ProvinceDTO>>(provs, HttpStatus.ACCEPTED);
	}

	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<ProvinceDTO> getProvince(
			@RequestParam(value = "latitude") String latitude,
			@RequestParam(value = "longitude") String longitude,
			@CookieValue(value = "sid", defaultValue = "cookie") String cookie) {

		LOG.info("One province lat: " + latitude + ", long:" + longitude);
		LOG.info(cookie);
		ProvinceDTO prov = provServ.getProvince(latitude, longitude, cookie);
		return new ResponseEntity<ProvinceDTO>(prov, HttpStatus.OK);

	}

	@RequestMapping(method = RequestMethod.GET, value = "/myprovinces")
	public ResponseEntity<List<ProvinceDTO>> getMyProvinces(
			@CookieValue(value = "sid") String cookie) {
		if (cookie.equals("")) {
			return new ResponseEntity<List<ProvinceDTO>>(HttpStatus.FORBIDDEN);
		}
		LOG.info("My provinces for user with cookie " + cookie);
		List<ProvinceDTO> provs = provServ.getMyProvinces(cookie);
		return new ResponseEntity<List<ProvinceDTO>>(provs, HttpStatus.OK);

	}

	@RequestMapping(method = RequestMethod.POST, value = "/changehome")
	public ResponseEntity<ServerResponse> changeHomeProvince(
			@RequestParam(value = "latitude") String latitude,
			@RequestParam(value = "longitude") String longitude,
			@CookieValue(value = "sid") String cookie) {
		if (cookie.equals("")) {
			return new ResponseEntity<ServerResponse>(HttpStatus.FORBIDDEN);
		}
		LOG.info("Change home province to lat: " + latitude + ", long:"
				+ longitude + " for player " + cookie);
		ServerResponse response = provServ.changeHomeProvince(latitude,
				longitude, cookie);
		return new ResponseEntity<ServerResponse>(response, HttpStatus.OK);

	}

	@RequestMapping(method = RequestMethod.POST, value = "/rename")
	public ResponseEntity<ServerResponse> renameProvince(
			@RequestParam(value = "latitude") String latitude,
			@RequestParam(value = "longitude") String longitude,
			@RequestParam(value = "newname") String newName,
			@CookieValue(value = "sid") String cookie) {
		if (cookie.equals("")) {
			return new ResponseEntity<ServerResponse>(HttpStatus.FORBIDDEN);
		}
		LOG.info("Change province name to " + newName
				+ " for province with lat: " + latitude + ", long:" + longitude
				+ " for player " + cookie);
		ServerResponse response = provServ.renameProvince(latitude, longitude,
				newName, cookie);
		return new ResponseEntity<ServerResponse>(response, HttpStatus.OK);

	}

	@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Bad parameters")
	@ExceptionHandler(NumberFormatException.class)
	public void handleNumberFormatException() {
	}

}
