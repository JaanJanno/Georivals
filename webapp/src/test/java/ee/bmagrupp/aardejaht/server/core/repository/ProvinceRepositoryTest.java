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
import ee.bmagrupp.aardejaht.server.core.domain.Province;

/**
 * Tests for {@link ProvinceRepository}
 * 
 * @author TKasekamp
 *
 */
@SpringApplicationConfiguration(classes = Application.class)
@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
public class ProvinceRepositoryTest {

	@Autowired
	ProvinceRepository provRepo;

	@Test
	public void allProvinces() {
		List<Province> provs = (List<Province>) provRepo.findAll();

		assertEquals("There should be 6 Provinces", 6, provs.size());
	}

	@Test
	public void singleProvince() {
		Province prov = provRepo.findOne(1);

		assertEquals("Province id", 1, prov.getId());
	}

}
