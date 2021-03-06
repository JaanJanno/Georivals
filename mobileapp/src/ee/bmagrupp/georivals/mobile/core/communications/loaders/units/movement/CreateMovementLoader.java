package ee.bmagrupp.georivals.mobile.core.communications.loaders.units.movement;

import java.util.List;

import ee.bmagrupp.georivals.mobile.core.communications.Constants;
import ee.bmagrupp.georivals.mobile.core.communications.loaders.GenericPostLoader;
import ee.bmagrupp.georivals.mobile.models.movement.BeginMovementDTO;
import ee.bmagrupp.georivals.mobile.models.movement.BeginMovementResponse;

/**
 * Class to make a POST to make new movements. Use by overriding the
 * handleResponse() method.
 * 
 * @author Jaan Janno
 * 
 */

public abstract class CreateMovementLoader extends
		GenericPostLoader<BeginMovementResponse> {

	/**
	 * 
	 * @param post
	 *            List of movements the user wishes to do.
	 * @param sid
	 *            Unique secret ID of the player.
	 * @param latitude
	 *            Latitude of the province moved to.
	 * @param longitude
	 *            Longitude of the province moved to.
	 * 
	 */

	public CreateMovementLoader(List<BeginMovementDTO> post, String sid,
			double latitude, double longitude) {
		super(BeginMovementResponse.class, post.toArray(), Constants.MOVE_TO,
				"sid=" + sid);
		addParameter("latitude", Double.toString(latitude));
		addParameter("longitude", Double.toString(longitude));
	}

}