package ee.bmagrupp.georivals.mobile.core.communications;

public class Constants {

	static final public boolean isLocalMobile = false;

	/*
	 * URL's on the server used for retrieving information.
	 */

	static {
		if (isLocalMobile)
			WEBPAGE = "http://10.0.2.2:8080/"; // Only Google API emulators.
		else
			WEBPAGE = "http://pacific-plains-8541.herokuapp.com/";
	}

	static final public String WEBPAGE;
	
	// Highscore
	
	static final public String HIGHSCORE = 	WEBPAGE + "highscore/";
	
	// Profile
	
	static final public String PROFILE = 	WEBPAGE + "profile/id/";
	
	// Province
	
	static final public String PROVINCE = 			WEBPAGE + "province/";
	static final public String RENAME_PROVINCE = 	PROVINCE + "rename/";
	static final public String SET_HOME_PROVINCE = 	PROVINCE + "changehome/";
	static final public String MYPROVINCES = 		PROVINCE + "myprovinces/";
	
	// Registration
	
	static final public String REGISTRATION_PHASE1 = WEBPAGE
			+ "registration/phase1/";
	static final public String REGISTRATION_PHASE2 = WEBPAGE
			+ "registration/phase2/";
	
	// Movement
	
	static final public String MOVEMENT = 		WEBPAGE + "movement/";
	static final public String CLAIM = 			MOVEMENT + "claim/";
	static final public String CANCEL_MOVE = 	MOVEMENT + "delete/";
	static final public String MYUNITS = 		MOVEMENT + "myunits/";
	
	// Battle
	
	static final public String BATTLE = 		WEBPAGE + "battle/";
	static final public String BATTLEHISTORY = 	BATTLE + "history/";
	
}
