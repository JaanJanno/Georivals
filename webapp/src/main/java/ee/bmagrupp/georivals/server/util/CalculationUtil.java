package ee.bmagrupp.georivals.server.util;

/**
 * Various static methods.
 * 
 * @author TKasekamp
 *
 */
public class CalculationUtil {

	/**
	 * Corrects the double rounding error.
	 * 
	 * @param longitude
	 * @author Sander
	 * @return correct double value
	 */
	public static double normalizeLongitude(double longitude) {
		double rtrn = Math.floor(longitude * 1000.0) / 1000.0;
		if ((rtrn * 1000.0) % 2 != 0) {
			rtrn = ((rtrn * 1000) - 1) / 1000.0;
		}
		rtrn += (Constants.PROVINCE_WIDTH / 2);
		return rtrn;
	}

	/**
	 * Corrects the double rounding error.
	 * 
	 * @param latitude
	 * @author Sander
	 * @return correct double value
	 */
	public static double normalizeLatitute(double latitutude) {
		double rtrn = Math.floor(latitutude * 1000.0) / 1000.0;
		rtrn += (Constants.PROVINCE_HEIGHT / 2);
		return rtrn;
	}

	/**
	 * Corrects the double rounding error.
	 * 
	 * @param longitude
	 * @author TKasekamp
	 * @return correct double value
	 */
	public static double normalizeLongitude(String longitude) {
		return normalizeLongitude(Double.parseDouble(longitude));
	}

	/**
	 * Corrects the double rounding error.
	 * 
	 * @param latitude
	 * @author TKasekamp
	 * @return correct double value
	 */
	public static double normalizeLatitute(String latitutude) {
		return normalizeLongitude(Double.parseDouble(latitutude));
	}
}
