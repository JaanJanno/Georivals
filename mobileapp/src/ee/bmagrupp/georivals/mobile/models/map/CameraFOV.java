package ee.bmagrupp.georivals.mobile.models.map;

import com.google.android.gms.maps.model.LatLngBounds;

public class CameraFOV {

	private double swlatitude;
	private double swlongitude;
	private double nelatitude;
	private double nelongitude;

	public CameraFOV(double swlatitude, double swlongitude, double nelatitude,
			double nelongitude) {
		this.swlatitude = swlatitude;
		this.swlongitude = swlongitude;
		this.nelatitude = nelatitude;
		this.nelongitude = nelongitude;
	}

	public CameraFOV(LatLngBounds latLngBounds) {
		this.swlatitude = latLngBounds.southwest.latitude;
		this.swlongitude = latLngBounds.southwest.longitude;
		this.nelatitude = latLngBounds.northeast.latitude;
		this.nelongitude = latLngBounds.northeast.longitude;
	}

	public double getSWlatitude() {
		return swlatitude;
	}

	public double getSWlongitude() {
		return swlongitude;
	}

	public double getNElatitude() {
		return nelatitude;
	}

	public double getNElongitude() {
		return nelongitude;
	}

}