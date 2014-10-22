package ee.bmagrupp.aardejaht.server.service;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;









import ee.bmagrupp.aardejaht.server.Application;
import ee.bmagrupp.aardejaht.server.core.repository.PlayerRepository;
import ee.bmagrupp.aardejaht.server.rest.domain.HighScoreEntry;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.*;

/**
 * Integration tests for {@link HighScoreService}.
 * @author TKasekamp
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@Profile(value = "test")
public class HighScoreIntegrationTest {
	
	@Autowired
	HighScoreService highScoreServ;
	
	
	@Test
	public void highScoreTest() {
		List<HighScoreEntry> highScores = highScoreServ.findAll();
		
		assertEquals("There should be 6 highscores", 6, highScores.size());
		
	}


}
