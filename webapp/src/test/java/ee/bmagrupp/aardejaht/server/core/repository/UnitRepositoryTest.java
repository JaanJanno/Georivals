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
import ee.bmagrupp.aardejaht.server.core.domain.Unit;

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
	public void allUnits() {
		List<Unit> units = (List<Unit>) unitRepo.findAll();

		assertEquals("There should be 8 Units", 8, units.size());
	}

	@Test
	public void singleUnit() {
		Unit unit = unitRepo.findOne(1);

		assertEquals("Unit id", 1, unit.getId());
	}

}
