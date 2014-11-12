package ee.bmagrupp.georivals.mobile.core.communications;

public class Constants {
	
	static final public boolean isLocalMobile = false;

	/*
	 * URL's on the server used for retrieving information.
	 */
	
	static{
		if (isLocalMobile)
			WEBPAGE = "http://10.0.2.2:8080/";
		else
			WEBPAGE = "http://pacific-plains-8541.herokuapp.com/";
	}
	
	static final public String WEBPAGE;
	static final public String HIGHSCORE = WEBPAGE + "highscore/";
	static final public String PROFILE = WEBPAGE + "profile/id/";
	static final public String PROVINCE = WEBPAGE + "province/";
	static final public String REGISTRATION_PHASE1 = WEBPAGE + "registration/phase1/";
	static final public String REGISTRATION_PHASE2 = WEBPAGE + "registration/phase2/";
	
}