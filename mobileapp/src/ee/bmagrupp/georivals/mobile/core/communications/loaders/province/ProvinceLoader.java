package ee.bmagrupp.georivals.mobile.core.communications.loaders.province;


import java.util.List;
import ee.bmagrupp.georivals.mobile.core.communications.Constants;
import ee.bmagrupp.georivals.mobile.core.communications.loaders.GenericPostLoader;
import ee.bmagrupp.georivals.mobile.models.map.CameraFOV;
import ee.bmagrupp.georivals.mobile.models.province.ProvinceDTO;

/**
 * Class for loading provinces from server given an CameraFOV object to
 * represent the extent of the query.
 * 
 * @author Jaan Janno
 * 
 */

public abstract class ProvinceLoader extends GenericPostLoader<List<ProvinceDTO>> {

	/**
	 * 
	 * @param sid
	 *            Unique secret ID of the player.
	 * @param fov
	 *            Area visible on map.
	 */

	public ProvinceLoader(String sid, CameraFOV fov) {
		super(ProvinceDTO.listType, fov, Constants.PROVINCE, "sid=" + sid);
	}

}
