package ee.bmagrupp.georivals.mobile.core.communications.loaders.province;

import java.util.List;
import ee.bmagrupp.georivals.mobile.core.communications.Constants;
import ee.bmagrupp.georivals.mobile.core.communications.loaders.GenericLoader;
import ee.bmagrupp.georivals.mobile.models.province.ProvinceDTO;

/**
 * Class used for loading a list of provinces owned by the player. Use by
 * overriding the handleResponse() method.
 * 
 * @author Jaan Janno
 * 
 */

public abstract class MyProvincesViewLoader extends
		GenericLoader<List<ProvinceDTO>> {

	/**
	 * 
	 * @param sid
	 *            Unique secret ID of the player.
	 */

	public MyProvincesViewLoader(String sid) {
		super(ProvinceDTO.listType, Constants.MYPROVINCES, "sid=" + sid);
	}

}
