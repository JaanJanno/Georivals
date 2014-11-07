package ee.bmagrupp.aardejaht.server.service;

import java.util.List;

import ee.bmagrupp.aardejaht.server.core.domain.Province;
import ee.bmagrupp.aardejaht.server.rest.domain.CameraFOV;
import ee.bmagrupp.aardejaht.server.rest.domain.ProvinceDTO;
import ee.bmagrupp.aardejaht.server.rest.domain.ProvinceViewDTO;

/**
 * Service that generates provinces.
 * 
 * @author TKasekamp
 *
 */
public interface ProvinceService {

	/**
	 * Request {@link ProvinceDTO}'s for drawing.
	 * 
	 * @param fov
	 *            Area where to draw Provinces
	 * @param cookie
	 *            SID of this user
	 * @return List of {@link ProvinceDTO}
	 */
	public List<ProvinceDTO> getProvinces(CameraFOV fov, String cookie);

	/**
	 * Returns the {@link Province} defined by the latitude and longitude.
	 * 
	 * @param latitude
	 * @param longitude
	 * @param cookie
	 *            SID of this user
	 * @return {@link ProvinceViewDTO}
	 */
	public ProvinceViewDTO getProvince(String latitude, String longitude,
			String cookie);

}
