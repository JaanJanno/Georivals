package ee.bmagrupp.georivals.server.game;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import static ee.bmagrupp.georivals.server.util.Constants.BOT_MAX_UNITS;
import static ee.bmagrupp.georivals.server.util.Constants.BOT_MIN_UNITS;

/**
 * Tests for {@link GameLogic}
 * 
 * @author TKasekamp
 *
 */
public class GameLogicTest {

	@Test
	public void botStengthTest() {
		int a1 = GameLogic.botUnits(36.3605, 138.727);
		int a2 = GameLogic.botUnits(36.3605, 138.727);
		assertEquals("The returned ints should be the same", a1, a2);

	}

	@Test
	public void botStengthIntervalTest() {

		for (double x = 10.0005; x < 10.015; x += 0.001) {
			for (double y = 10.000; y < 10.01; y += 0.001) {
				int botStrength = GameLogic.botUnits(x, y);
				boolean isInRange = botStrength >= BOT_MIN_UNITS && botStrength <= BOT_MAX_UNITS;
				assertTrue("The returned int should be in range "
						+ BOT_MIN_UNITS + " to " + BOT_MAX_UNITS, isInRange);
			}
		}

	}

}
