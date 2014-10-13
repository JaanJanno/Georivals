package ee.bmagrupp.aardejaht.server.rest;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ee.bmagrupp.aardejaht.server.core.old.Treasure;
import ee.bmagrupp.aardejaht.server.repository.TreasureService;

@RestController
@RequestMapping("/treasure")
public class TreasureController {
	private static Logger LOG = LoggerFactory
			.getLogger(TreasureController.class);
	@Autowired
	TreasureService treasureServ;

	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<List<Treasure>> getAll() {
		List<Treasure> treasures = (List<Treasure>) treasureServ.findAll();
		return new ResponseEntity<List<Treasure>>(treasures,
				HttpStatus.ACCEPTED);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/{id}")
	public ResponseEntity<Treasure> getById(@PathVariable int id) {
		Treasure treasure = treasureServ.findById(id);
		return new ResponseEntity<Treasure>(treasure, HttpStatus.ACCEPTED);
	}

}
