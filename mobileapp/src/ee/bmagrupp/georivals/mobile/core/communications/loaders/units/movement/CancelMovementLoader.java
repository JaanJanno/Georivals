package ee.bmagrupp.georivals.mobile.core.communications.loaders.units.movement;

import ee.bmagrupp.georivals.mobile.core.communications.Constants;
import ee.bmagrupp.georivals.mobile.core.communications.loaders.GenericLoader;
import ee.bmagrupp.georivals.mobile.models.movement.MovementViewDTO;

/**
 * Class to send a DELETE request to the server to cancel an unit movement. User
 * is identified by sid added in cookie.
 * 
 * Response can be handled by overriding the handleResponse() method.
 * 
 * @author Jaan Janno
 * 
 */

public abstract class CancelMovementLoader extends
		GenericLoader<MovementViewDTO> {

	/**
	 * 
	 * @param sid
	 *            Sid of user to be added as request cookie.
	 * @param movementId
	 *            Id of the movement to be cancelled.
	 */

	public CancelMovementLoader(String sid, int movementId) {
		super(MovementViewDTO.class, Constants.CANCEL_MOVE, "sid=" + sid);
		setRequestMethod("DELETE");
		addParamter("id", Integer.toString(movementId));
	}

}
