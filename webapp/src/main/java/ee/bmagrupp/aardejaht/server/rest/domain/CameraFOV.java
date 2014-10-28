package ee.bmagrupp.aardejaht.server.rest.domain;

/**
 * Data transfer object containing mobile screen edge coordinates.
 * 
 * @author TKasekamp
 *
 */
public class CameraFOV {
	private double SWlatitude;
	private double SWlongitude;
	private double NElatitude;
	private double NElongitude;

	public CameraFOV() {
	}

	public CameraFOV(double swlatitude, double swlongitude, double nelatitude,
			double nelongitude) {
		super();
		SWlatitude = swlatitude;
		SWlongitude = swlongitude;
		NElatitude = nelatitude;
		NElongitude = nelongitude;
	}

	public double getSWlatitude() {
		return SWlatitude;
	}

	public double getSWlongitude() {
		return SWlongitude;
	}

	public double getNElatitude() {
		return NElatitude;
	}

	public double getNElongitude() {
		return NElongitude;
	}

	public void setSWlatitude(double sWlatitude) {
		SWlatitude = sWlatitude;
	}

	public void setSWlongitude(double sWlongitude) {
		SWlongitude = sWlongitude;
	}

	public void setNElatitude(double nElatitude) {
		NElatitude = nElatitude;
	}

	public void setNElongitude(double nElongitude) {
		NElongitude = nElongitude;
	}

	@Override
	public String toString() {
		return "CameraFOV [SWlatitude=" + SWlatitude + ", SWlongitude="
				+ SWlongitude + ", NElatitude=" + NElatitude + ", NElongitude="
				+ NElongitude + "]";
	}

	/**
	 * JSON representation of this object. Handwritten with love.
	 * 
	 * @return JSON string
	 */
	public String toJson() {
		return "{ \"nelongitude\":" + NElongitude + ",\"swlongitude\":"
				+ SWlongitude + ",\"swlatitude\":" + SWlatitude
				+ ",\"nelatitude\":" + NElatitude + " }";
	}

}