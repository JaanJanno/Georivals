package ee.bmagrupp.georivals.server.rest.domain;

/**
 * Data transfer object containing mobile screen edge coordinates.
 * 
 * @author TKasekamp
 *
 */
public class CameraFOV {
	private double swlatitude;
	private double swlongitude;
	private double nelatitude;
	private double nelongitude;

	public CameraFOV() {
	}

	public CameraFOV(double swlatitude, double swlongitude, double nelatitude,
			double nelongitude) {
		super();
		this.swlatitude = swlatitude;
		this.swlongitude = swlongitude;
		this.nelatitude = nelatitude;
		this.nelongitude = nelongitude;
	}

	public double getSwlatitude() {
		return swlatitude;
	}

	public void setSwlatitude(double swlatitude) {
		this.swlatitude = swlatitude;
	}

	public double getSwlongitude() {
		return swlongitude;
	}

	public void setSwlongitude(double swlongitude) {
		this.swlongitude = swlongitude;
	}

	public double getNelatitude() {
		return nelatitude;
	}

	public void setNelatitude(double nelatitude) {
		this.nelatitude = nelatitude;
	}

	public double getNelongitude() {
		return nelongitude;
	}

	public void setNelongitude(double nelongitude) {
		this.nelongitude = nelongitude;
	}

	@Override
	public String toString() {
		return "CameraFOV [swlatitude=" + swlatitude + ", swlongitude="
				+ swlongitude + ", nelatitude=" + nelatitude + ", nelongitude="
				+ nelongitude + "]";
	}

	/**
	 * JSON representation of this object. Handwritten with love.
	 * 
	 * @return JSON string
	 */
	public String toJson() {
		return "{ \"nelongitude\":" + nelongitude + ",\"swlongitude\":"
				+ swlongitude + ",\"swlatitude\":" + swlatitude
				+ ",\"nelatitude\":" + nelatitude + " }";
	}

}