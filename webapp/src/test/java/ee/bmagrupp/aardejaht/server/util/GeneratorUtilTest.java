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
public class GeneratorUtilTest {

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
	
	@Test
	public void generateWithSeedTest2(){
		double lat1 = 145.687;
		double long1 = 01.029;
		
		String a1 = GeneratorUtil.generateString(8, lat1, long1);
		String a2 = GeneratorUtil.generateString(8, lat1, long1);
		
		assertEquals("The length of the String should be n", 8, a1.length());
		assertEquals("The Strings should be equal", a1, a2);
	}

	@Test
	public void botStengthTest() {
		int a1 = GeneratorUtil.botUnits(36.3605, 138.727, 13);
		int a2 = GeneratorUtil.botUnits(36.3605, 138.727, 13);
		assertEquals("The returned ints should be the same", a1, a2);

	}

}
