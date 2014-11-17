package ee.bmagrupp.georivals.server.game;

import java.util.Date;
import java.util.Random;

import ee.bmagrupp.georivals.server.core.domain.BattleHistory;
import ee.bmagrupp.georivals.server.core.domain.Player;
import ee.bmagrupp.georivals.server.core.domain.Province;
import static ee.bmagrupp.georivals.server.util.Constants.ATTACKER_ODDS;

public class BattleLogic {
	
	/**
	 * @author Sander
	 * @param player1Units - attacker units
	 * @param player2Units - defender units
	 * @return returns a integer array with 2 values, player 1 left over men and player 2 left over men
	 */
	
	public static int[] resolveBattle(int player1, int player2){
		Random r = new Random(new Date().getTime());
		Random r2 = new Random(r.nextLong());	// a little black boxing so math nerds won't cheat
		
		while(player1 > 0 || player2 > 0){
			int n = r2.nextInt(100) + 1; // range 1 - 100
			if(n >= 1 && n <= ATTACKER_ODDS){
				player2 -= 1;
			}
			else{
				player1 -= 1;
			}
			r2.setSeed(r.nextLong());
		}
		
		int[] result = new int[2];
		result[0] = player1;
		result[1] = player2;
		return result;
	}
	
	public static BattleHistory battle(Province location,Player attacker,Player defender, int attackerStrength, int defenderStrength){
		Date curDate = new Date();
		int[] result = resolveBattle(attackerStrength, defenderStrength);
		boolean attackerWon = false;
		if(result[1] == 0){
			attackerWon = true;
		}
		int attackerLosses = attackerStrength - result[0];
		int defenderLosses = defenderStrength - result[1];
		return new BattleHistory(location, curDate, attacker, defender, attackerWon, attackerStrength, defenderStrength, attackerLosses, defenderLosses);
	}
}

