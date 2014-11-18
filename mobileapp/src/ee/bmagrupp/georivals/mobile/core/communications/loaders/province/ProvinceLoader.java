package ee.bmagrupp.georivals.mobile.core.communications.loaders.province;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;
import com.google.gson.Gson;
import ee.bmagrupp.georivals.mobile.core.communications.Constants;
import ee.bmagrupp.georivals.mobile.core.communications.loaders.GenericListPostLoader;
import ee.bmagrupp.georivals.mobile.models.map.CameraFOV;
import ee.bmagrupp.georivals.mobile.models.province.ProvinceDTO;

/**
 * Class for loading provinces from server given an CameraFOV object to
 * represent the extent of the query.
 * 
 * @author Jaan Janno
 * 
 */

public abstract class ProvinceLoader extends GenericListPostLoader<ProvinceDTO> {

	CameraFOV fov; // Represent the visible area on map view.

	/**
	 * 
	 * @param sid
	 *            Unique secret ID of the player.
	 * @param fov
	 *            Area visible on map.
	 */

	public ProvinceLoader(String sid, CameraFOV fov) {
		super(ProvinceDTO.listType, Constants.PROVINCE, "sid=" + sid);
		this.fov = fov;
	}

	/*
	 * Writes JSON of sent FOV object to the server.
	 */

	@Override
	public void writeRequestBody(DataOutputStream writer) throws IOException {
		String JSON = new Gson().toJson(fov);
		writer.writeBytes(JSON);
	}

	/**
	 * Override this method to handle the list of province objects retrieved
	 * from the server.
	 */

	@Override
	abstract public void handleResponseList(List<ProvinceDTO> responseList);

}
