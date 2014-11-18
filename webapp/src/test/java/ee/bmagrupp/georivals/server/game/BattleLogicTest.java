package ee.bmagrupp.georivals.server.game;

import static org.junit.Assert.*;

import javax.transaction.Transactional;

import ee.bmagrupp.georivals.server.Application;
import ee.bmagrupp.georivals.server.core.domain.BattleHistory;
import ee.bmagrupp.georivals.server.core.domain.Player;
import ee.bmagrupp.georivals.server.core.domain.Province;
import ee.bmagrupp.georivals.server.core.repository.PlayerRepository;
import ee.bmagrupp.georivals.server.game.BattleLogic;

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


@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@ActiveProfiles("test")
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
		TransactionalTestExecutionListener.class })
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
@Transactional
public class BattleLogicTest {
	
	@Autowired
	PlayerRepository playerRepo;
	
	@Autowired
	BattleLogic battleLog;
	
	@Test
	public void resolveBattleTest() {
		int player1Units = 100;
		int player2Units = 100;
		
		int[] result = battleLog.resolveBattle(player1Units, player2Units);
		if(result[0] != 0){
			assertEquals("Expected", 0, result[1]);
		}
		else{
			assertNotEquals("Expected", 0, result[1]);
		}
	}
	
	@Test
	public void battleTest() {
		Player player = playerRepo.findBySid("BPUYYOU62flwiWJe");
		BattleHistory result =  battleLog.battle(new Province(56.6545, 93.877), player, player, 100, 1);
		
		assertEquals("Expected", true, result.isAttackerWon());
	}
	
}
