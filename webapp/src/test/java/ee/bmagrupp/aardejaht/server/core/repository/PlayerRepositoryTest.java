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

}
