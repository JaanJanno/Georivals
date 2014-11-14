package ee.bmagrupp.georivals.server.util;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Tests for {@link CalculationUtil}
 * 
 * @author TKasekamp
 *
 */
public class CalculationUtilTest {

	@Test
	public void latitudeTest() {
		double latitude = -40.4195;
		double output = CalculationUtil.normalizeLatitute(-40.4195);
		assertEquals("Rounding stuff", latitude, output, 0.0001);

		double output2 = CalculationUtil.normalizeLatitute(-40.41954535345636);
		assertEquals("Rounding stuff", latitude, output2, 0.0001);

		double output3 = CalculationUtil
				.normalizeLatitute(-40.4195000000000000000001);
		assertEquals("Rounding stuff", latitude, output3, 0.0001);

		double output4 = CalculationUtil.normalizeLatitute(0);
		assertEquals("Rounding stuff", 0.0005, output4, 0.0001);
	}

	@Test
	public void longitudeTest() {
		double longitude = 144.961;
		double output = CalculationUtil.normalizeLongitude(144.961);
		double output2 = CalculationUtil
				.normalizeLongitude(144.961000000000003);
		assertEquals("Rounding stuff", longitude, output, 0.0001);
		assertEquals("Rounding stuff", longitude, output2, 0.0001);
	}

}
