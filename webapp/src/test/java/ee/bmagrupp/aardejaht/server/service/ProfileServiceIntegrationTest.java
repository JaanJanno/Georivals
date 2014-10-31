package ee.bmagrupp.aardejaht.server.service;

import static org.junit.Assert.*;

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
import ee.bmagrupp.aardejaht.server.rest.domain.PlayerProfile;

/**
 * Integration tests for {@link ProfileService}.
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
public class ProfileServiceIntegrationTest {

	@Autowired
	ProfileService profServ;

	@Test
	public void findBySidSuccess() {
		String sid = "BPUYYOU62flwiWJe";
		PlayerProfile player = profServ.getPlayerProfile(sid);

		assertEquals("Player name", "Mr. TK", player.getUsername());
		assertEquals("Player id", 1, player.getId());
		assertEquals("Player email", "mr.tk@pacific.ee", player.getEmail());
		assertEquals("Player name", 2, player.getOwnedProvinces());
		assertEquals("Player name", 23, player.getTotalUnits());
	}

	@Test
	public void findBySidFail() {
		String sid = "BPUYYOU62flwiWJe123";
		PlayerProfile player = profServ.getPlayerProfile(sid);

		assertNull(player);
	}

}
