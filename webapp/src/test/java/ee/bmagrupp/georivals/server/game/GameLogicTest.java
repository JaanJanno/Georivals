package ee.bmagrupp.georivals.server.game;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Tests for {@link GameLogic}
 * 
 * @author TKasekamp
 *
 */
public class GameLogicTest {

	@Test
	public void botStengthTest() {
		int a1 = GameLogic.botUnits(36.3605, 138.727, 13);
		int a2 = GameLogic.botUnits(36.3605, 138.727, 13);
		assertEquals("The returned ints should be the same", a1, a2);

	}
	//TODO more tests
}
