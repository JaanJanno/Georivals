package ee.bmagrupp.georivals.mobile.models;

import java.util.Date;

public class ProfileEntry {

	private int id;
	private String username;
	private String email;
	private int totalUnits;
	private int ownedProvinces;

	public ProfileEntry(int id, String username, String email, int totalUnits,
			int ownedProvinces, Date registerDate) {
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

	public void setId(int id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getTotalUnits() {
		return totalUnits;
	}

	public void setTotalUnits(int totalUnits) {
		this.totalUnits = totalUnits;
	}

	public int getOwnedProvinces() {
		return ownedProvinces;
	}

	public void setOwnedProvinces(int ownedProvinces) {
		this.ownedProvinces = ownedProvinces;
	}

	@Override
	public String toString() {
		String s = new String("{");
		s += "id:" + id + ",";
		s += "username:" + username + ",";
		s += "email:" + email + ",";
		s += "totalUnits:" + totalUnits;
		s += "ownedProvinces:" + ownedProvinces + ",";
		return s + "}";
	}
}
