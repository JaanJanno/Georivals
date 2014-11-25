package ee.bmagrupp.georivals.mobile.core.communications.loaders.province;

import ee.bmagrupp.georivals.mobile.core.communications.Constants;
import ee.bmagrupp.georivals.mobile.core.communications.loaders.GenericLoader;
import ee.bmagrupp.georivals.mobile.models.province.ProvinceDTO;

/**
 * Class for making a HTTP get request to the server and retrieving ProvinceDTO
 * parsed from JSON to objects. Use this by overriding the
 * handleResponse() method and calling retrieveResponse() method.
 * 
 * @author Jaan Janno
 */

public abstract class ProvinceViewLoader extends
		GenericLoader<ProvinceDTO> {

	/**
	 * 
	 * @param latitude
	 * @param longitude
	 */

	public ProvinceViewLoader(Double latitude, Double longitude) {
		super(ProvinceDTO.class, Constants.PROVINCE);
		addParameters(latitude, longitude);
	}

	/**
	 * 
	 * @param sid
	 * @param latitude
	 * @param longitude
	 */

	public ProvinceViewLoader(String sid, Double latitude, Double longitude) {
		super(ProvinceDTO.class, Constants.PROVINCE, "sid=" + sid);
		addParameters(latitude, longitude);
	}

	/*
	 * Adds longitude and latitude parameters to the request that will be
	 * created by the superclass methods.
	 */

	private void addParameters(Double latitude, Double longitude) {
		addParameter("latitude", Double.toString(latitude));
		addParameter("longitude", Double.toString(longitude));
	}

}
