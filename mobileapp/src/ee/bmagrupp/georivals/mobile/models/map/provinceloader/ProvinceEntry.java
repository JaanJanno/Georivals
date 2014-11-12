package ee.bmagrupp.georivals.mobile.models.map.provinceloader;

public class ProvinceEntry {
	private int id;
	private double latitude;
	private double longitude;
	private int unitCount;
	private int playerId;
	private String name;

	public ProvinceEntry(int id, double latitude, double longitude,
			int unitCount, int playerId, String name) {
		super();
		this.id = id;
		this.latitude = latitude;
		this.longitude = longitude;
		this.unitCount = unitCount;
		this.playerId = playerId;
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

	public int getUnitCount() {
		return unitCount;
	}

	public int getPlayerId() {
		return playerId;
	}

	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return "ProvinceEntry [id=" + id + ", latitude=" + latitude
				+ ", longitude=" + longitude + ", unitCount=" + unitCount
				+ ", playerId=" + playerId + ", name=" + name + "]";
	}
}
