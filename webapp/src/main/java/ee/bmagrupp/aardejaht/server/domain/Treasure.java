package ee.bmagrupp.aardejaht.server.domain;

import java.io.Serializable;

import javax.persistence.*;

@Entity
public class Treasure implements Serializable {
	@Id
	@GeneratedValue
	private int id;
	@Column(nullable = false)
	private float latitude;
	@Column(nullable = false)
	private float longitude;
	@Column(nullable = false)
	private String pictureLink;

	public Treasure(float latitude, float longitude, String pictureLink) {
		super();
		this.latitude = latitude;
		this.longitude = longitude;
		this.pictureLink = pictureLink;
	}

	protected Treasure() {

	}

	public int getId() {
		return id;
	}

	public float getLatitude() {
		return latitude;
	}

	public float getLongitude() {
		return longitude;
	}

	public String getPictureLink() {
		return pictureLink;
	}

	@Override
	public String toString() {
		return "Treasure [id=" + id + ", latitude=" + latitude + ", longitude="
				+ longitude + ", pictureLink=" + pictureLink + "]";
	}

}
