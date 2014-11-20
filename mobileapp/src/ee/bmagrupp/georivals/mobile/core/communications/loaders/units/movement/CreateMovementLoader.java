package ee.bmagrupp.georivals.mobile.core.communications.loaders.units.movement;

import java.util.List;
import ee.bmagrupp.georivals.mobile.core.communications.Constants;
import ee.bmagrupp.georivals.mobile.core.communications.loaders.GenericObjectPostLoader;
import ee.bmagrupp.georivals.mobile.models.movement.BeginMovementDTO;
import ee.bmagrupp.georivals.mobile.models.movement.BeginMovementResponse;

/**
 * Class to make a POST to make new movements. Use by overriding the
 * handleResponseObject() method.
 * 
 * @author Jaan Janno
 * 
 */

public abstract class CreateMovementLoader extends
		GenericObjectPostLoader<BeginMovementResponse> {

	/**
	 * 
	 * @param post
	 *            List of movements the user wishes to do.
	 * @param sid
	 *            Unique secret ID of the player.
	 * @param longitude
	 *            Longitude of the province moved to.
	 * @param latitude
	 *            Latitude of the province moved to.
	 * 
	 */

	public CreateMovementLoader(List<BeginMovementDTO> post, String sid,
			double longitude, double latitude) {
		super(BeginMovementResponse.class, post.toArray(), Constants.MOVE_TO,
				"sid=" + sid);
		addParamter("longitude", Double.toString(longitude));
		addParamter("latitude", Double.toString(latitude));
	}

	/**
	 * Override this method to handle the response retrieved from the server.
	 */

	@Override
	public abstract void handleResponseObject(
			BeginMovementResponse responseObject);

}