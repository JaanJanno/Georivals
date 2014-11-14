package ee.bmagrupp.georivals.server.core.repository;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import ee.bmagrupp.georivals.server.Application;
import ee.bmagrupp.georivals.server.core.domain.Unit;
import ee.bmagrupp.georivals.server.core.repository.UnitRepository;

/**
 * Tests for {@link UnitRepository}
 * 
 * @author TKasekamp
 *
 */
@SpringApplicationConfiguration(classes = Application.class)
@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
public class UnitRepositoryTest {

	@Autowired
	UnitRepository unitRepo;

	@Test
	public void singleUnit() {
		Unit unit = unitRepo.findOne(2);

		assertEquals("Unit id", 2, unit.getId());
	}

}
