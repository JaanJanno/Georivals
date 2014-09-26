package ee.bmagrupp.aardejaht.server.domain;

public class Treasure {
	private int ID;
	private float latitude;
	private float longitude;
	private String pictureLink;

	public Treasure(float latitude, float longitude, String pictureLink) {
		super();
		this.latitude = latitude;
		this.longitude = longitude;
		this.pictureLink = pictureLink;
	}

	public int getID() {
		return ID;
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
		return "Treasure [ID=" + ID + ", latitude=" + latitude + ", longitude="
				+ longitude + ", pictureLink=" + pictureLink + "]";
	}

}
