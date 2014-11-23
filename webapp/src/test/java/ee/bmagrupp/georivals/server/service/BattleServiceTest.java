package ee.bmagrupp.georivals.server.service;

import java.util.List;

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

import ee.bmagrupp.georivals.server.Application;
import ee.bmagrupp.georivals.server.rest.domain.BattleHistoryDTO;
import ee.bmagrupp.georivals.server.rest.domain.BattleType;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@ActiveProfiles("test")
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
		TransactionalTestExecutionListener.class })
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
@Transactional
public class BattleServiceTest {
	
	@Autowired
	BattleService battleServ;
	
	@Test 
	public void getMyBattlesTest(){
		List<BattleHistoryDTO> lst = battleServ.getBattles("BPUYYOU62flwiWJe");
		assertEquals("Expected 1 battle history", 1, lst.size());
		assertEquals("TK won", BattleType.ATTACK_PLAYER_WON, lst.get(0).getType());
	}	
	
}
