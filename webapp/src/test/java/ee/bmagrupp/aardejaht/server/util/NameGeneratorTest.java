package ee.bmagrupp.aardejaht.server.util;

import static org.junit.Assert.*;

import org.junit.Test;

import ee.bmagrupp.aardejaht.server.util.NameGenerator;

/**
 * Tests for {@link NameGenerator}.
 * 
 * @author TKasekamp
 *
 */
public class NameGeneratorTest {

	@Test
	public void testGenerate() {
		String len6 = NameGenerator.generate(6);
		assertEquals("The length should be 6", 6, len6.length());

		String len0 = NameGenerator.generate(0);
		assertEquals("The length should be 0", 0, len0.length());

		String lenNegative = NameGenerator.generate(-10);
		assertEquals("The length should be 0", 0, lenNegative.length());
	}

}
