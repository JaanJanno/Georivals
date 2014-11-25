package ee.bmagrupp.georivals.mobile.core.communications.loaders.province.modify;

import ee.bmagrupp.georivals.mobile.core.communications.Constants;
import ee.bmagrupp.georivals.mobile.core.communications.loaders.GenericLoader;
import ee.bmagrupp.georivals.mobile.models.ServerResponse;

/**
 * Class for sending a request to change the players home province. Use this by
 * overriding the handleResponse() method and calling retrieveResponse()
 * method.
 * 
 * @author Jaan Janno
 * 
 */

public abstract class ChangeHomeLoader extends
		GenericLoader<ServerResponse> {

	/**
	 * 
	 * @param latitude
	 *            Latitude of the new home province.
	 * @param longitude
	 *            Longitude of the new home province.
	 * @param sid
	 *            Unique ID that identifies the player.
	 */

	public ChangeHomeLoader(double latitude, double longitude, String sid) {
		super(ServerResponse.class, Constants.SET_HOME_PROVINCE, "sid=" + sid);
		addParameter("latitude", Double.toString(latitude));
		addParameter("longitude", Double.toString(longitude));
		setRequestMethod("POST");
	}

}
