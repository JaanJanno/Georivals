package ee.bmagrupp.aardejaht.server.rest;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ee.bmagrupp.aardejaht.server.rest.domain.Province;
import ee.bmagrupp.aardejaht.server.service.ProvinceService;

@RestController
@RequestMapping("/province")
public class ProvinceController {

	private static Logger LOG = LoggerFactory
			.getLogger(ProfileController.class);

	@Autowired
	ProvinceService provServ;

	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<List<Province>> getAll() {
		LOG.info("All provinces");
		List<Province> provs = provServ.getAll();
		return new ResponseEntity<List<Province>>(provs, HttpStatus.ACCEPTED);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/test")
	public String getTest() {
		LOG.info("Province test method for Sander");
		provServ.testStuff();
		return "check the log for the shit you've done";
	}

}
