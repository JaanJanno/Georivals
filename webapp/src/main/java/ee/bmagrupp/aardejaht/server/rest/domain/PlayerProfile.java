package ee.bmagrupp.aardejaht.server.rest.domain;

import java.util.Date;

public class PlayerProfile {

	private final int id;

	private final String username;

	private final String email;

	private final int totalUnits;

	private final int ownedProvinces;

	private final Date registerDate;

	public PlayerProfile(int id, String username, String email, int totalUnits,
			int ownedProvinces, Date registerDate) {
		super();
		this.id = id;
		this.username = username;
		this.email = email;
		this.totalUnits = totalUnits;
		this.ownedProvinces = ownedProvinces;
		this.registerDate = registerDate;
	}

	public int getId() {
		return id;
	}

	public String getUsername() {
		return username;
	}

	public String getEmail() {
		return email;
	}

	public int getTotalUnits() {
		return totalUnits;
	}

	public int getOwnedProvinces() {
		return ownedProvinces;
	}

	public Date getRegisterDate() {
		return registerDate;
	}

}
