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

import ee.bmagrupp.aardejaht.server.rest.domain.BattleHistoryDTO;
import ee.bmagrupp.aardejaht.server.service.BattleService;

@RestController
@RequestMapping("/battle")
public class BattleController {

	private static final Logger LOG = LoggerFactory
			.getLogger(BattleController.class);

	@Autowired
	BattleService battleServ;

	@RequestMapping(method = RequestMethod.GET, value = "/history")
	public ResponseEntity<List<BattleHistoryDTO>> getBattleHistory(
			@CookieValue(value = "sid") String cookie) {
		LOG.info("Creating battle history for " + cookie);
		List<BattleHistoryDTO> battles = battleServ.getBattles(cookie);
		return new ResponseEntity<List<BattleHistoryDTO>>(battles,
				HttpStatus.OK);
	}
}
