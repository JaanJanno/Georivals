package ee.bmagrupp.georivals.server.game;

import static ee.bmagrupp.georivals.server.game.util.Constants.BOT_MAX_UNITS;
import static ee.bmagrupp.georivals.server.game.util.Constants.BOT_MIN_UNITS;
import static ee.bmagrupp.georivals.server.game.util.Constants.PROVINCE_UNIT_MAX;
import static ee.bmagrupp.georivals.server.game.util.Constants.PROVINCE_UNIT_MIN;
import static ee.bmagrupp.georivals.server.game.util.Constants.UNIT_GENERATION_TIME;

import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import ee.bmagrupp.georivals.server.core.domain.Ownership;
import ee.bmagrupp.georivals.server.core.domain.Province;
import ee.bmagrupp.georivals.server.game.util.Constants;
import ee.bmagrupp.georivals.server.game.util.GeneratorUtil;

/**
 * Stuff that makes the game work. Most of it should be here. If it isn't, well,
 * good luck finding it.
 * 
 * @author TKasekamp
 *
 */
public class GameLogic {
	// These are for testing purposes only
	// static int time = 10;

	/**
	 * Checks if the player defined by player1Strength can attack the provinces
	 * owned by player2Strength. Returns true if player1 is not over 2 times
	 * bigger then player2.
	 * 
	 * @param player1Strength
	 *            Current player
	 * @param player2Strength
	 *            Player to check against
	 * @return {@code boolean}
	 */
	public static boolean canAttack(int player1Strength, int player2Strength) {
		if (player2Strength * 2 >= player1Strength) {
			return true;
		}
		return false;
	}

	/**
	 * Returns the number of new Units to be added to this Province. New units
	 * are added if: <br>
	 * 1) The difference between currentDate and lastVisitDate is more than
	 * {@link Constants#UNIT_GENERATION_TIME}<br>
	 * 2) The provinceStrenght is less than {@link Constants#PROVINCE_UNIT_MAX}<br>
	 * If both 1 and 2 are true then an int between
	 * {@link Constants#UNIT_GENERATION_MIN} and
	 * {@link Constants#UNIT_GENERATION_MAX} is returned. If this int +
	 * provinceStrenght now exceeds {@link Constants#PROVINCE_UNIT_MAX}, the
	 * return int will be decreased so that there are a total of
	 * {@link Constants#PROVINCE_UNIT_MAX} Units in this province. <br>
	 * Otherwise 0 will be returned.
	 * 
	 * @param lastVisitDate
	 *            The last visit {@link Date} from {@link Ownership}
	 * @param currentDate
	 *            The current {@link Date}
	 * @param provinceStrength
	 *            The number of units in this province
	 * @author TKasekamp
	 * @return Integer
	 */
	public static int generateNewUnits(Date lastVisitDate, Date currentDate,
			int provinceStrength) {
		long a = currentDate.getTime() - lastVisitDate.getTime();
		if ((a > UNIT_GENERATION_TIME)
				&& (provinceStrength < PROVINCE_UNIT_MAX)) {
			int b = GeneratorUtil.generateWithSeed(lastVisitDate);
			if (b + provinceStrength > PROVINCE_UNIT_MAX) {
				b = PROVINCE_UNIT_MAX - provinceStrength;
			}
			return b;
		}
		return 0;
	}

	/**
	 * Generates a Province unit size when the province is owned by a bot.
	 * Latitude and longitude are used as seeds. This method will always return
	 * the same int for the same inputs.
	 * 
	 * @param latitude
	 * @param longitude
	 * @author TKasekamp
	 * @return int
	 */
	public static int botUnits(double latitude, double longitude) {
		int min = BOT_MIN_UNITS;
		int max = BOT_MAX_UNITS;
		long seed = (long) (((latitude * 1000.0) + ((longitude) / 1000.0)) * 1000000);
		Random rand = new Random(seed);
		int botStrength = rand.nextInt((max - min) + 1) + min;
		if (botStrength > PROVINCE_UNIT_MAX) {
			botStrength = PROVINCE_UNIT_MAX;
		} else if (botStrength < PROVINCE_UNIT_MIN) {
			botStrength = PROVINCE_UNIT_MIN;
		}

		return botStrength;
	}

	/**
	 * Calculates the end time for this movement.
	 * 
	 * @param start
	 *            {@link Province}
	 * @param destination
	 *            {@link Province}
	 * @param curDate
	 *            {@link Date}
	 * @return {@link Date}
	 */
	public static Date calculateTime(Province start, Province destination,
			Date curDate) {
		double latitude = Math.abs(destination.getLatitude()
				- start.getLatitude());
		double longitude = Math.abs(destination.getLongitude()
				- start.getLongitude());
		double distance = Math
				.sqrt(latitude * latitude + longitude * longitude);

		int time = (int) ((distance / Constants.PROVINCE_HEIGHT) * Constants.SPEED_CONSTANT);

		Calendar cal = Calendar.getInstance(); // creates calendar
		cal.setTime(curDate); // sets calendar time/date
		cal.add(Calendar.SECOND, time);

		return cal.getTime();
	}

}
