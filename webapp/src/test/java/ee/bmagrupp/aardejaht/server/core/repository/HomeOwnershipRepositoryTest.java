package ee.bmagrupp.aardejaht.server.core.repository;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import ee.bmagrupp.aardejaht.server.Application;
import ee.bmagrupp.aardejaht.server.core.domain.HomeOwnership;

/**
 * Tests for {@link HomeOwnershipRepository}
 * 
 * @author TKasekamp
 *
 */
@SpringApplicationConfiguration(classes = Application.class)
@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
public class HomeOwnershipRepositoryTest {

	@Autowired
	HomeOwnershipRepository homeRepo;

	@Test
	public void singleHome() {
		HomeOwnership owner = homeRepo.findOne(1);

		assertEquals("HomeOwnership id", 1, owner.getId());
	}

}
