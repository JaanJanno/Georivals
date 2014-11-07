package ee.bmagrupp.aardejaht.server.rest;

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

import ee.bmagrupp.aardejaht.server.rest.domain.CameraFOV;
import ee.bmagrupp.aardejaht.server.rest.domain.ProvinceDTO;
import ee.bmagrupp.aardejaht.server.rest.domain.ProvinceViewDTO;
import ee.bmagrupp.aardejaht.server.service.ProvinceService;

@RestController
@RequestMapping("/province")
public class ProvinceController {

	private static Logger LOG = LoggerFactory
			.getLogger(ProfileController.class);

	@Autowired
	ProvinceService provServ;

	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<List<ProvinceDTO>> getProvinces(
			@RequestBody CameraFOV fov,
			@CookieValue(value = "sid", defaultValue = "cookie") String cookie) {
		LOG.info("All provinces");
		LOG.info(fov.toJson());
		LOG.info(cookie);
		List<ProvinceDTO> provs = provServ.getProvinces(fov, cookie);
		LOG.info("The number of provs to return " + provs.size());
		return new ResponseEntity<List<ProvinceDTO>>(provs, HttpStatus.ACCEPTED);
	}

	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<ProvinceViewDTO> getProvince(
			@RequestParam(value = "latitude") String latitude,
			@RequestParam(value = "longitude") String longitude,
			@CookieValue(value = "sid", defaultValue = "cookie") String cookie) {

		LOG.info("One province lat: " + latitude + ", long:" + longitude);
		LOG.info(cookie);
		ProvinceViewDTO prov = provServ
				.getProvince(latitude, longitude, cookie);
		return new ResponseEntity<ProvinceViewDTO>(prov, HttpStatus.OK);

	}

}
