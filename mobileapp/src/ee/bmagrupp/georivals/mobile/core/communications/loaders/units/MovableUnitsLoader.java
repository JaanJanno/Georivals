package ee.bmagrupp.georivals.mobile.core.communications.loaders.units;

import java.util.List;
import ee.bmagrupp.georivals.mobile.core.communications.Constants;
import ee.bmagrupp.georivals.mobile.core.communications.loaders.GenericLoader;
import ee.bmagrupp.georivals.mobile.models.movement.MovementSelectionViewDTO;

/**
 * Class for sending a request to get a list of units that can be moved to the
 * province in given coordinates . <br>
 * 
 * Use this by overriding the handleResponse() method and calling
 * retrieveResponse() method.
 * 
 * @author Jaan Janno
 * 
 */

public abstract class MovableUnitsLoader extends
		GenericLoader<List<MovementSelectionViewDTO>> {

	/**
	 * 
	 * @param sid
	 *            Unique ID that identifies the player.
	 */

	public MovableUnitsLoader(String sid, double latitude, double longitude) {
		super(MovementSelectionViewDTO.listType, Constants.MYUNITS, "sid="
				+ sid);
		addParameter("latitude", Double.toString(latitude));
		addParameter("longitude", Double.toString(longitude));
	}

}