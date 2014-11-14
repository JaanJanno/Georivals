package ee.bmagrupp.georivals.server.core.repository;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import ee.bmagrupp.georivals.server.Application;
import ee.bmagrupp.georivals.server.core.domain.Ownership;
import ee.bmagrupp.georivals.server.core.repository.OwnershipRepository;

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

		assertEquals("There should be 6 ownerships", 5, owners.size());
	}

	@Test
	public void singleOwnership() {
		Ownership owner = ownerRepo.findOne(1);

		assertEquals("Ownership id", 1, owner.getId());
	}

	@Test
	public void findBetweenTest() {
		List<Ownership> owners = (List<Ownership>) ownerRepo.findBetween(
				-40.423, 144.960, -40.419, 144.966);

		assertEquals("Number of provinces in this area", 5, owners.size());

		List<Ownership> owners2 = (List<Ownership>) ownerRepo.findBetween(
				-40.423, 144.960, -40.419, 144.966);

		assertEquals("Number of provinces in this area", 5, owners2.size());

		List<Ownership> owners3 = (List<Ownership>) ownerRepo.findBetween(
				59.37, 27.71, 59.40, 27.75);

		assertEquals("Number of provinces in this area", 0, owners3.size());
	}

	@Test
	public void findByProvinceIdTest() {
		Ownership ow = ownerRepo.findByProvinceId(6);
		assertEquals("Ownership id", 1, ow.getId());
	}

}
