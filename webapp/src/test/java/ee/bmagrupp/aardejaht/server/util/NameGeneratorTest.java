package ee.bmagrupp.aardejaht.server.util;

import static org.junit.Assert.*;

import java.util.Date;

import org.junit.Test;

import ee.bmagrupp.aardejaht.server.util.GeneratorUtil;

/**
 * Tests for {@link GeneratorUtil}.
 * 
 * @author TKasekamp
 *
 */
public class NameGeneratorTest {

	@Test
	public void testGenerate() {
		String len6 = GeneratorUtil.generateString(6);
		assertEquals("The length should be 6", 6, len6.length());

		String len0 = GeneratorUtil.generateString(0);
		assertEquals("The length should be 0", 0, len0.length());

		String lenNegative = GeneratorUtil.generateString(-10);
		assertEquals("The length should be 0", 0, lenNegative.length());
	}

	@Test
	public void generateWithSeedTest() {
		Date date = new Date();

		int a1 = GeneratorUtil.generateWithSeed(date);
		int a2 = GeneratorUtil.generateWithSeed(date);
		int a3 = GeneratorUtil.generateWithSeed(date,
				Constants.UNIT_GENERATION_MIN, Constants.UNIT_GENERATION_MAX);

		assertEquals("The returned ints should be the same", a1, a2);
		assertEquals("The returned ints should be the same", a1, a3);

	}

}
