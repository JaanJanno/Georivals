package ee.bmagrupp.aardejaht.server.rest.domain;

/**
 * DTO for ProfileView.
 * 
 * @author TKasekamp
 *
 */
public class PlayerProfile {

	private final int id;

	private final String username;

	private final String email;

	private final int totalUnits;

	private final int ownedProvinces;

	public PlayerProfile(int id, String username, String email, int totalUnits,
			int ownedProvinces) {
		super();
		this.id = id;
		this.username = username;
		this.email = email;
		this.totalUnits = totalUnits;
		this.ownedProvinces = ownedProvinces;
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

}
