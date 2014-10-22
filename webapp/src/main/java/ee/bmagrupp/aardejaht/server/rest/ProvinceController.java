package ee.bmagrupp.aardejaht.server.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ee.bmagrupp.aardejaht.server.service.ProvinceService;

@RestController
@RequestMapping("/province")
public class ProvinceController {

	private static Logger LOG = LoggerFactory
			.getLogger(ProfileController.class);

	@Autowired
	ProvinceService provServ;

	@RequestMapping(method = RequestMethod.GET)
	public String getAll() {
		LOG.info("Province test method for Sander");
		provServ.testStuff();
		return "check the log for the shit you've done";
	}
}
