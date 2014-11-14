package ee.bmagrupp.georivals.server.util;

public class Constants {
	// BOT
	public static final int BOT_ID = 0;
	public static final String BOT_NAME = "BOT";
	public static final double BOT_STRENGTH_CONSTANT = 0.2;

	// Player
	public static final int PLAYER_DEFAULT_STRENGTH = 100;
	public static final int PLAYER_DEFAULT_ID = -1;
	
	// Province generation
	public static final double PROVINCE_WIDTH = 0.002;
	public static final double PROVINCE_HEIGHT = 0.001;

	// Province
	public static final int PROVINCE_UNIT_MAX = 100;
	public static final int PROVINCE_UNIT_MIN = 1;

	// Unit
	public static final int UNIT_GENERATION_MAX = 10;
	public static final int UNIT_GENERATION_MIN = 0;

	/**
	 * 24h in milliseconds.
	 */
	public static final int UNIT_GENERATION_TIME = 86400000;

	// Other
	public static final String DOWNLOAD_PAGE = "http://math.ut.ee/~jaan911/mobileapp/Georivals.apk";
	public static final int PROVINCE_NAME_LENGTH = 10;
	public static final int PLAYER_SID_LENGTH = 16;
	public static final int PLAYER_START_UNITS = 10;
}
