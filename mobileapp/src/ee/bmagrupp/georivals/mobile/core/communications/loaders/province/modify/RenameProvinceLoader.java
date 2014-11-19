package ee.bmagrupp.georivals.mobile.core.communications.loaders.province.modify;

import ee.bmagrupp.georivals.mobile.core.communications.Constants;
import ee.bmagrupp.georivals.mobile.core.communications.loaders.GenericObjectLoader;
import ee.bmagrupp.georivals.mobile.models.ServerResponse;

/**
 * Class to make a POST to rename a province. Use by overriding the
 * handleResponseObject() method.
 * 
 * @author Jaan Janno
 * 
 */

public abstract class RenameProvinceLoader extends
		GenericObjectLoader<ServerResponse> {

	/**
	 * 
	 * @param longitude
	 *            Longitude of the renamed province.
	 * @param latitude
	 *            Latitude of the renamed province.
	 * @param newname
	 *            New name for the province.
	 * @param sid
	 *            Unique secret ID of the player.
	 */

	public RenameProvinceLoader(double longitude, double latitude,
			String newname, String sid) {
		super(ServerResponse.class, Constants.RENAME_PROVINCE, "sid=" + sid);
		setRequestMethod("POST");
		addParamter("longitude", Double.toString(longitude));
		addParamter("latitude", Double.toString(latitude));
		addParamter("newname", newname);
	}

	/**
	 * Override this method to handle the response retrieved from the server.
	 */

	@Override
	public abstract void handleResponseObject(ServerResponse responseObject);

}
