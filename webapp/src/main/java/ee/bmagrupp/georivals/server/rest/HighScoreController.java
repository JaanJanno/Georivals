package ee.bmagrupp.georivals.server.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ee.bmagrupp.georivals.server.rest.domain.HighScoreEntry;
import ee.bmagrupp.georivals.server.service.HighScoreService;

@RestController
@RequestMapping("/highscore")
public class HighScoreController {

	@Autowired
	HighScoreService highScoreServ;

	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<List<HighScoreEntry>> getAll() {
		List<HighScoreEntry> highScores = (List<HighScoreEntry>) highScoreServ
				.findAll();
		return new ResponseEntity<List<HighScoreEntry>>(highScores,
				HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/{id}")
	public ResponseEntity<HighScoreEntry> getById(@PathVariable int id) {
		HighScoreEntry highscore = highScoreServ.findById(id);
		return new ResponseEntity<HighScoreEntry>(highscore, HttpStatus.OK);
	}
}
