package ee.bmagrupp.aardejaht.server.core.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import ee.bmagrupp.aardejaht.server.util.Constants;
import ee.bmagrupp.aardejaht.server.util.NameGenerator;

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

	private String name;

	protected Province() {
	}

	/**
	 * Basic constructor for a Province. Name is set to a random alphanumeric
	 * String. The length of it is specified in {@link Constants}.
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
		this.setName(NameGenerator.generate(Constants.PROVINCE_NAME_LENGTH));
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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
				+ longitude + ", name=" + name + "]";
	}

}
