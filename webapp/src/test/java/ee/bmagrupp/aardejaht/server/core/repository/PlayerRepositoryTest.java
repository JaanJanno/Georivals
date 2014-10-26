package ee.bmagrupp.aardejaht.server.core.repository;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import ee.bmagrupp.aardejaht.server.Application;
import ee.bmagrupp.aardejaht.server.core.domain.Player;

/**
 * Tests for {@link PlayerRepository}
 * 
 * @author TKasekamp
 *
 */
@SpringApplicationConfiguration(classes = Application.class)
@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
public class PlayerRepositoryTest {

	@Autowired
	PlayerRepository playerRepo;

	@Test
	public void allPlayers() {
		List<Player> players = (List<Player>) playerRepo.findAll();

		assertEquals("There should be 6 Players", 6, players.size());
	}

	@Test
	public void singlePlayer() {
		Player player = playerRepo.findOne(1);

		assertEquals("Player id", 1, player.getId());
	}

	@Test
	public void ownerTest() {
		Player player = playerRepo.findOwner(1);

		assertEquals("Player owner", 1, player.getId());

		// Test for no such ownership
		Player player2 = playerRepo.findOwner(20);

		assertEquals("Player owner", null, player2);
	}

	/**
	 * Testing the authentication stuff.
	 */
	@Test
	public void authTest() {
		// Player 1 - Mr. TK
		Player p = playerRepo.findBySid("HDpVys");

		assertEquals("Player Sid or password", 1, p.getId());

		// No such sid test
		Player p1 = playerRepo.findBySid("HDpVys213");

		assertEquals("There should be no user with this sid", null, p1);
	}

	@Test
	public void emailTest() {
		// Player 1 - Mr. TK
		Player p = playerRepo.findByEmail("mr.tk@pacific.ee");

		assertEquals("Player with this email", 1, p.getId());

		// No such email test
		Player p1 = playerRepo.findByEmail("mr.tk@pacific.ee123");

		assertEquals("There should be no user with this email", null, p1);
	}

}
