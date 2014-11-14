package ee.bmagrupp.georivals.server.core.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Province implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(nullable = false)
	private double latitude;

	@Column(nullable = false)
	private double longitude;

	protected Province() {
	}

	/**
	 * Basic constructor for a Province.
	 * 
	 * @param latitude
	 *            For Tartu something like 58.37
	 * @param longitude
	 *            For Tartu something like 26.72
	 */
	public Province(double latitude, double longitude) {
		super();
		this.longitude = longitude;
		this.latitude = latitude;
	}

	public int getId() {
		return id;
	}

	public double getLatitude() {
		return latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	@Override
	public String toString() {
		return "Province [id=" + id + ", latitude=" + latitude + ", longitude="
				+ longitude + "]";
	}

}
