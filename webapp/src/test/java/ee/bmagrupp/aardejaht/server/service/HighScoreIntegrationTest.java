package ee.bmagrupp.aardejaht.server.service;

import java.util.List;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

import ee.bmagrupp.aardejaht.server.Application;
import ee.bmagrupp.aardejaht.server.rest.domain.HighScoreEntry;
import static org.junit.Assert.*;

/**
 * Integration tests for {@link HighScoreService}.
 * 
 * @author TKasekamp
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@ActiveProfiles("test")
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
		TransactionalTestExecutionListener.class })
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
@Transactional
public class HighScoreIntegrationTest {

	@Autowired
	HighScoreService highScoreServ;

	@Test
	public void highScoreTestAll() {
		List<HighScoreEntry> highScores = highScoreServ.findAll();

		assertEquals("There should be 6 highscores", 6, highScores.size());

	}

	/**
	 * Test for player Mr. TK. No units at home. 2 provinces. Total 23 units.
	 */
	@Test
	public void highScoreTestTK() {
		HighScoreEntry highScore = highScoreServ.findById(1);

		assertEquals("The id", 1, highScore.getId());
		assertEquals("Average units. TotalUnits/Provinces", 11.5,
				highScore.getAverageUnits(), 0.1);
		assertEquals("Number of provinces", 2, highScore.getTerritoriesOwned());
		assertEquals("The name", "Mr. TK", highScore.getUsername());

	}

	/**
	 * Test for player JohnnyZQ. 1 province. 6 units. 10 units at home.
	 */
	@Test
	public void highScoreTestJohnny() {
		HighScoreEntry highScore = highScoreServ.findById(5);

		assertEquals("The id", 5, highScore.getId());
		assertEquals("Average units. TotalUnits/Provinces", 11.0,
				highScore.getAverageUnits(), 0.1);
		assertEquals("Number of provinces", 1, highScore.getTerritoriesOwned());
		assertEquals("The name", "JohnnyZQ", highScore.getUsername());

	}

	/**
	 * Test for player KingJaan. 0 provinces. 0 units. 0 units at home.
	 */
	@Test
	public void highScoreTestKing() {
		HighScoreEntry highScore = highScoreServ.findById(6);

		assertEquals("The id", 6, highScore.getId());
		assertEquals("Average units. TotalUnits/Provinces", 0.0,
				highScore.getAverageUnits(), 0.1);
		assertEquals("Number of provinces", 0, highScore.getTerritoriesOwned());
		assertEquals("The name", "King Jaan", highScore.getUsername());

	}

}
