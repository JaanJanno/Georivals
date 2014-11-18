package ee.bmagrupp.georivals.mobile.core.communications.loaders.province;

import ee.bmagrupp.georivals.mobile.core.communications.Constants;
import ee.bmagrupp.georivals.mobile.core.communications.loaders.GenericObjectLoader;
import ee.bmagrupp.georivals.mobile.models.province.ProvinceDTO;

/**
 * Class for making a HTTP get request to the server and retrieving ProvinceDTO
 * parsed from JSON to objects. Use this by overriding the
 * handleResponseObject() method and calling retrieveObject() method.
 * 
 * @author Jaan Janno
 */

public abstract class ProvinceViewLoader extends
		GenericObjectLoader<ProvinceDTO> {

	/**
	 * 
	 * @param longitude
	 * @param latitude
	 */

	public ProvinceViewLoader(Double longitude, Double latitude) {
		super(ProvinceDTO.class, Constants.PROVINCE);
		addParameters(longitude, latitude);
	}

	/**
	 * 
	 * @param sid
	 * @param longitude
	 * @param latitude
	 */

	public ProvinceViewLoader(String sid, Double longitude, Double latitude) {
		super(ProvinceDTO.class, Constants.PROVINCE, "sid=" + sid);
		addParameters(longitude, latitude);
	}

	/*
	 * Adds longitude and latitude parameters to the request that will be
	 * created by the superclass methods.
	 */

	private void addParameters(Double longitude, Double latitude) {
		addParamter("longitude", Double.toString(longitude));
		addParamter("latitude", Double.toString(latitude));
	}

	/**
	 * Override this method to define the behavior after an ProvinceViewDTO
	 * object has been retrieved. Remember this method doesn't run on the UI
	 * thread!
	 */

	@Override
	abstract public void handleResponseObject(ProvinceDTO responseObject);

}
