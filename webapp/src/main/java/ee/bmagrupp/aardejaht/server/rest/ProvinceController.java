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
import org.springframework.web.bind.annotation.RestController;

import ee.bmagrupp.aardejaht.server.rest.domain.CameraFOV;
import ee.bmagrupp.aardejaht.server.rest.domain.Province;
import ee.bmagrupp.aardejaht.server.service.ProvinceService;

@RestController
@RequestMapping("/province")
public class ProvinceController {

	private static Logger LOG = LoggerFactory
			.getLogger(ProfileController.class);

	@Autowired
	ProvinceService provServ;

	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<List<Province>> getProvinces(
			@RequestBody CameraFOV fov,
			@CookieValue(value = "sid", defaultValue = "cookie") String cookie) {
		LOG.debug("All provinces");
		LOG.debug(fov.toJson());
		LOG.debug(cookie);
		List<Province> provs = provServ.getProvinces(fov, cookie);
		return new ResponseEntity<List<Province>>(provs, HttpStatus.ACCEPTED);
	}

}
