package ee.bmagrupp.aardejaht.server.util;

import static ee.bmagrupp.aardejaht.server.util.Constants.BOT_STRENGTH_CONSTANT;

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
		String gen = "";
		Random x = new Random();
		for (int i = 0; i < n; i++) {
			gen += chars[x.nextInt(chars.length)];
		}
		return gen;
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

	/**
	 * Generates a Province unit size when the province is owned by a bot.
	 * Latitude and longitude are used as seeds. This method will always return
	 * the same int for the same inputs.
	 * 
	 * @param latitude
	 * @param longitude
	 * @param playerStrength
	 * @author TKasekamp
	 * @return int
	 */
	public static int botUnits(double latitude, double longitude,
			int playerStrength) {
		// TODO improve this heap of ... code
		int min = playerStrength
				- (int) (playerStrength * BOT_STRENGTH_CONSTANT);
		int max = playerStrength
				+ (int) (playerStrength * BOT_STRENGTH_CONSTANT);
		Random rand = new Random((long) ((latitude + longitude) * 1000));
		int botStrength = rand.nextInt((max - min) + 1) + min;
		if (botStrength > Constants.PROVINCE_UNIT_MAX) {
			botStrength = Constants.PROVINCE_UNIT_MAX;
		} else if (botStrength < Constants.PROVINCE_UNIT_MIN) {
			botStrength = Constants.PROVINCE_UNIT_MIN;
		}

		return botStrength;
	}
}
