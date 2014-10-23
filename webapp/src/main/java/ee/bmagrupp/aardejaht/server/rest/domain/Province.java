package ee.bmagrupp.aardejaht.server.rest.domain;

/**
 * Container object for sending province data. Missing stuff: is this province
 * under attack. And probably more stuff. 
 * 
 * @author TKasekamp
 *
 */
public class Province {
	private final int id;
	private final double latitude;
	private final double longitude;
	private final int unitCount;
	private final int playerId;
	private final String name;

	public Province(int id, double latitude, double longitude, int unitCount,
			int playerId, String name) {
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

}