package ee.bmagrupp.aardejaht.server.rest;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ee.bmagrupp.aardejaht.server.domain.Treasure;
import ee.bmagrupp.aardejaht.server.domain.Trek;
import ee.bmagrupp.aardejaht.server.repository.TrekService;
import ee.bmagrupp.aardejaht.server.repository.UserService;

@RestController
@RequestMapping("/trek")
public class TrekController {

	private static Logger LOG = LoggerFactory.getLogger(TrekController.class);
	@Autowired
	TrekService trekServ;

	@Autowired
	UserService userServ;

	@RequestMapping(method = RequestMethod.GET, value = "/{id}")
	public ResponseEntity<Trek> getById(@PathVariable int id) {
		Trek trek = trekServ.findById(id);
		return new ResponseEntity<Trek>(trek, HttpStatus.ACCEPTED);
	}

	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<List<Trek>> getAll() {
		List<Trek> treks = (List<Trek>) trekServ.findAll();
		return new ResponseEntity<List<Trek>>(treks, HttpStatus.ACCEPTED);
	}

	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<Trek> startTrek(@RequestBody Treasure treasure) {
		Trek trek = new Trek(userServ.findById(1), treasure);
		trekServ.save(trek);
		return new ResponseEntity<Trek>(trek, HttpStatus.ACCEPTED);
	}

	@RequestMapping(method = RequestMethod.POST, value = "/end")
	public ResponseEntity<Trek> endtTrek(@RequestBody Trek trekOld) {
		// If database query is too slow, then the endtime set must be brought
		// forward
		Trek trek = trekServ.findById(trekOld.getId());
		trek.setEndTime(new Date());
		trek.setDifference(0);
		trek.setScore(0);
		trekServ.save(trek);
		return new ResponseEntity<Trek>(trek, HttpStatus.ACCEPTED);
	}
}
