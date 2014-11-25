package ee.bmagrupp.georivals.mobile.core.communications.loaders.units.movement;

import java.util.List;
import ee.bmagrupp.georivals.mobile.core.communications.Constants;
import ee.bmagrupp.georivals.mobile.core.communications.loaders.GenericLoader;
import ee.bmagrupp.georivals.mobile.models.movement.MovementViewDTO;

/**
 * Class for sending a GET request to receive a list of an users movements in
 * progress. User is identified by sid added in cookie.
 * 
 * Response list can be handled by overriding the handleResponse() method.
 * 
 * @author Jaan Janno
 */

public abstract class MovementViewLoader extends
		GenericLoader<List<MovementViewDTO>> {

	/**
	 * 
	 * @param sid
	 *            Sid of user to be added as request cookie.
	 */

	public MovementViewLoader(String sid) {
		super(MovementViewDTO.listType, Constants.MOVEMENT, "sid=" + sid);
	}

}
