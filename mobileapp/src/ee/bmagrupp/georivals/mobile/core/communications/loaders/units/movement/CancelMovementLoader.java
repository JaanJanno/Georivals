package ee.bmagrupp.georivals.mobile.core.communications.loaders.units.movement;

import ee.bmagrupp.georivals.mobile.core.communications.Constants;
import ee.bmagrupp.georivals.mobile.core.communications.loaders.GenericObjectLoader;
import ee.bmagrupp.georivals.mobile.models.movement.MovementViewDTO;

/**
 * Class to send a DELETE request to the server to cancel an
 * unit movement. User is identified by sid added in cookie.
 * 
 * Response can be handled by overriding the 
 * handleResponseObject() method.
 * 
 * @author Jaan Janno
 *
 */

public abstract class CancelMovementLoader extends GenericObjectLoader<MovementViewDTO> {
	
	/**
	 * 
	 * @param sid Sid of user to be added as request cookie.
	 * @param movementId Id of the movement to be cancelled.
	 */
	
	public CancelMovementLoader(String sid, int movementId) {
		super(MovementViewDTO.class, Constants.CANCEL_MOVE, "sid="+sid);
		setRequestMethod("DELETE");
		addParamter("id", Integer.toString(movementId));
	}
	
	/**
	 * Override this method to handle the response list of
	 * MovementViewDTO objects gotten from server.
	 * (Units moving back to origin)
	 */

	@Override
	public abstract void handleResponseObject(MovementViewDTO responseObject);

}
