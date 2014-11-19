package ee.bmagrupp.georivals.mobile.core.communications.loaders.units;

import java.util.List;
import ee.bmagrupp.georivals.mobile.core.communications.Constants;
import ee.bmagrupp.georivals.mobile.core.communications.loaders.GenericListLoader;
import ee.bmagrupp.georivals.mobile.models.movement.MovementSelectionViewDTO;

/**
 * Class for sending a request to get a list of units that can be moved to the
 * province in given coordinates . <br>
 * 
 * Use this by overriding the handleResponseList() method and calling
 * retrieveList() method.
 * 
 * @author Jaan Janno
 * 
 */

public abstract class MovableUnitsLoader extends
		GenericListLoader<MovementSelectionViewDTO> {

	/**
	 * 
	 * @param sid
	 *            Unique ID that identifies the player.
	 */

	public MovableUnitsLoader(String sid) {
		super(MovementSelectionViewDTO.listType, Constants.MYUNITS, "sid="
				+ sid);
	}

	/**
	 * Override this method to define the behavior after an object has been
	 * retrieved. Remember this method doesn't run on the UI thread!
	 */

	@Override
	public abstract void handleResponseList(
			List<MovementSelectionViewDTO> responseList);

}