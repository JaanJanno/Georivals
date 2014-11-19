package ee.bmagrupp.georivals.mobile.core.communications.loaders.province.modify;

import ee.bmagrupp.georivals.mobile.core.communications.Constants;
import ee.bmagrupp.georivals.mobile.core.communications.loaders.GenericObjectLoader;
import ee.bmagrupp.georivals.mobile.models.ServerResponse;

/**
 * Class for sending a request to change the players
 * home province. Use this by overriding the
 * handleResponseObject() method and calling 
 * retrieveObject() method.
 * 
 * @author Jaan Janno
 * 
 */

public abstract class ChangeHomeLoader extends
		GenericObjectLoader<ServerResponse> {

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
		addParamter("latitude", Double.toString(latitude));
		addParamter("longitude", Double.toString(longitude));
		setRequestMethod("POST");
	}

	/**
	 * Override this method to define the behavior after an ServerResult object
	 * has been retrieved. Remember this method doesn't run on the UI thread!
	 */

	@Override
	public abstract void handleResponseObject(ServerResponse responseObject);

}
