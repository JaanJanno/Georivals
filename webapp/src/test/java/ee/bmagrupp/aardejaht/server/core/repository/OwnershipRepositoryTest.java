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
import ee.bmagrupp.aardejaht.server.core.domain.Ownership;

/**
 * Tests for {@link OwnershipRepository}
 * 
 * @author TKasekamp
 *
 */
@SpringApplicationConfiguration(classes = Application.class)
@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
public class OwnershipRepositoryTest {

	@Autowired
	OwnershipRepository ownerRepo;

	@Test
	public void allOwnerships() {
		List<Ownership> owners = (List<Ownership>) ownerRepo.findAll();

		assertEquals("There should be 6 ownerships", 6, owners.size());
	}

	@Test
	public void singleOwnership() {
		Ownership owner = ownerRepo.findOne(1);

		assertEquals("Ownership id", 1, owner.getId());
	}

	@Test
	public void findBetweenTest() {
		List<Ownership> owners = (List<Ownership>) ownerRepo.findBetween(58.37,
				26.71, 58.40, 26.75);

		assertEquals("Number of provinces in this area", 3, owners.size());

		List<Ownership> owners2 = (List<Ownership>) ownerRepo.findBetween(
				59.37, 27.71, 59.40, 27.75);

		assertEquals("Number of provinces in this area", 0, owners2.size());
	}

}
