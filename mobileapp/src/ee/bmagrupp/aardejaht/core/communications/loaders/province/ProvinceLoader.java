package ee.bmagrupp.aardejaht.core.communications.loaders.province;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;
import com.google.gson.Gson;
import ee.bmagrupp.aardejaht.core.communications.Constants;
import ee.bmagrupp.aardejaht.core.communications.loaders.GenericListPostLoader;
import ee.bmagrupp.aardejaht.models.CameraFOV;
import ee.bmagrupp.aardejaht.models.ProvinceDTO;

public abstract class ProvinceLoader extends GenericListPostLoader<ProvinceDTO> {

	CameraFOV fov;
	
	public ProvinceLoader(String sid, CameraFOV fov) {
		super(ProvinceDTO.class, Constants.PROVINCE, "sid="+sid);
		this.fov = fov;
	}

	@Override
	public void writeRequestBody(DataOutputStream writer) throws IOException {
		String JSON = new Gson().toJson(fov);
		writer.writeBytes(JSON);
	}
	
	@Override
	abstract public void handleResponseList(List<ProvinceDTO> responseList);

}
