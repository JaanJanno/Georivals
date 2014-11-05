package ee.bmagrupp.aardejaht.models;

import com.google.android.gms.maps.model.LatLngBounds;

public class CameraFOV {
	private double swlatitude;
	private double swlongitude;
	private double nelatitude;
	private double nelongitude;
	
	
	
	public CameraFOV(double sWlatitude, double sWlongitude, double nElatitude,
			double nElongitude) {
		super();
		swlatitude = sWlatitude;
		swlongitude = sWlongitude;
		nelatitude = nElatitude;
		nelongitude = nElongitude;
	}

	public CameraFOV (LatLngBounds latLngBounds){
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