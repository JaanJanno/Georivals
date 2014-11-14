package ee.bmagrupp.georivals.server.util;

import java.util.Date;
import java.util.Random;

/**
 * Utility class for generating stuff. Includes only static methods.
 * 
 * @author TKasekamp
 *
 */
public class GeneratorUtil {

	private static char[] chars = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h',
			'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u',
			'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H',
			'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U',
			'V', 'W', 'X', 'Y', 'Z', '1', '2', '3', '4', '5', '6', '7', '8',
			'9', '0' };

	/**
	 * @author Sander
	 * @param n
	 *            - the length of the random string to be generated
	 * @return The string that has been randomly generated
	 */

	public static String generateString(int n) {
		StringBuilder sb = new StringBuilder(n);
		Random x = new Random();
		for (int i = 0; i < n; i++) {
			sb.append(chars[x.nextInt(chars.length)]);
		}
		return sb.toString();
	}

	/**
	 * @param n
	 *            - length of String to be returned
	 * @param lat1
	 *            - latitude in normalized form (3 significants)
	 * @param long1
	 *            - longitude in normalized form (3 significants)
	 * @return n-length string
	 */

	public static String generateString(int n, double lat1, double long1) {
		StringBuilder sb = new StringBuilder(n);
		long seed = (long) (((lat1 * 1000.0) + ((long1) / 1000.0)) * 1000000);
		Random r = new Random(seed);
		for (int i = 0; i < n; i++) {
			sb.append(chars[r.nextInt(chars.length)]);
		}
		return sb.toString();
	}

	/**
	 * @param lat1
	 *            - latitude in normalized form (3 significants)
	 * @param long1
	 *            - longitude in normalized form (3 significants)
	 * @return {@link Constants#PROVINCE_NAME_LENGTH}-length string
	 */

	public static String generateString(double lat1, double long1) {
		return generateString(Constants.PROVINCE_NAME_LENGTH, lat1, long1);
	}

	/**
	 * Generates an int between {@link Constants#UNIT_GENERATION_MIN} and
	 * {@link Constants#UNIT_GENERATION_MAX} with the {@link Date#getTime()} as
	 * seed. This method will always return the same int for the same date.
	 * 
	 * @param date
	 *            {@link Date} to be used as seed.
	 * @return int
	 */
	public static int generateWithSeed(Date date) {
		return generateWithSeed(date, Constants.UNIT_GENERATION_MIN,
				Constants.UNIT_GENERATION_MAX);
	}

	/**
	 * Generates an int between min and max with the {@link Date#getTime()} as
	 * seed. This method will always return the same int for the same date.
	 * 
	 * @author TKasekamp
	 * @author Jaan Janno
	 * @param date
	 *            {@link Date} to be used as seed.
	 * @param min
	 *            The minimum to be generated.
	 * @param max
	 *            The maximum to be generated.
	 * @return int
	 */
	public static int generateWithSeed(Date date, int min, int max) {
		Random random = new Random(date.getTime());
		int a = min + ((int) (random.nextDouble() * (max - min)));
		return a;
	}

}
