package ee.bmagrupp.aardejaht.server.core.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import ee.bmagrupp.aardejaht.server.util.NameGenerator;

@Entity
public class Province implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(nullable = false)
	private double longitude;

	@Column(nullable = false)
	private double latitude;

	private String name;

	protected Province() {
	}

	/**
	 * Basic constructor for a Province. Name is set to a random alphanumeric
	 * String.
	 * 
	 * @param longitude
	 * @param latitude
	 */
	public Province(double longitude, double latitude) {
		super();
		this.longitude = longitude;
		this.latitude = latitude;
		this.setName(NameGenerator.generate(6));
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
