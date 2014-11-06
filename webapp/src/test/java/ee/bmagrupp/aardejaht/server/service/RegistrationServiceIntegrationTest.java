package ee.bmagrupp.aardejaht.server.service;

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
import ee.bmagrupp.aardejaht.server.core.domain.Player;

/**
 * Integration tests for {@link RegistrationService}.
 * 
 * @author Sander Tiganik
 *
 */

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@ActiveProfiles("test")
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
		TransactionalTestExecutionListener.class })
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
@Transactional
public class RegistrationServiceIntegrationTest {
	
	@Autowired
	RegistrationService regServ;
	
	@Test
	public void createPlayerTest(){
		String username = "LollipopGuildMaster";
		String email = "Willy@Wonka.gm";
		double Lat = 35.3605653;
		double Long = 138.7277694;
		
		Player player = regServ.createPlayer(username, email, Lat, Long);
		Lat = player.getHome().getProvince().getLatitude();
		Long = player.getHome().getProvince().getLongitude();
		
		assert(Lat == 35.3605);
		assert(Long == 138.727);
	}
}
