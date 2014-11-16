package ee.bmagrupp.georivals.mobile.core.communications.loaders.units;

import ee.bmagrupp.georivals.mobile.core.communications.Constants;
import ee.bmagrupp.georivals.mobile.core.communications.loaders.GenericObjectPostLoader;
import ee.bmagrupp.georivals.mobile.models.ServerResponse;

/**
 * Class for sending a POST request to server for
 * claiming units from a given coordinate. Longitude
 * and latitude are sent as parameters to request.
 * User is identified by sid in cookie.
 * 
 * Respons is gotten as a ServerResponse object that 
 * can be handled overriding the handleResponseObject()
 * method.
 * 
 * @author Jaan Janno
 */

public abstract class UnitClaimLoader extends GenericObjectPostLoader<ServerResponse> {
	
	/**
	 * 
	 * @param sid Sid put into the cookie, identifies user.
	 * @param latitude Latitude request parameter.
	 * @param longitude Longitude request parameter.
	 */
	
	public UnitClaimLoader(String sid, double latitude, double longitude) {
		super(ServerResponse.class, Constants.CLAIM, "sid="+sid);
		addParamter("latitude", 	Double.toString(latitude));
		addParamter("longitude", 	Double.toString(longitude));
	}
	
	/**
	 * Override this to handle response gotten from the server.
	 */

	@Override
	public abstract void handleResponseObject(ServerResponse responseObject);

}