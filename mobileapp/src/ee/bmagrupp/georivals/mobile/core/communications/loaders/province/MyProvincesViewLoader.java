package ee.bmagrupp.georivals.mobile.core.communications.loaders.province;

import java.util.List;
import ee.bmagrupp.georivals.mobile.core.communications.Constants;
import ee.bmagrupp.georivals.mobile.core.communications.loaders.GenericListLoader;
import ee.bmagrupp.georivals.mobile.models.province.ProvinceDTO;

/**
 * Class used for loading a list of provinces owned by the player. Use by
 * overriding the handleResponseList() method.
 * 
 * @author Jaan Janno
 * 
 */

public abstract class MyProvincesViewLoader extends
		GenericListLoader<ProvinceDTO> {

	/**
	 * 
	 * @param sid
	 *            Unique secret ID of the player.
	 */

	public MyProvincesViewLoader(String sid) {
		super(ProvinceDTO.listType, Constants.MYPROVINCES, "sid="+sid);
	}

	/**
	 * Override this method to handle the list of province objects retrieved
	 * from the server.
	 */

	@Override
	public abstract void handleResponseList(List<ProvinceDTO> responseList);

}
