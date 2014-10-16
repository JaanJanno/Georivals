package ee.bmagrupp.aardejaht.models;

import java.util.Date;

public class ProfileEntry {
	
	private int id;
	private String username;
	private String email;
	private int totalUnits;
	private int ownedProvinces;
	private Date registerDate;
	
	@Override
	public String toString() {
		String s = new String("{");
		s += "id:" + id + ",";
		s += "username:" + username + ",";
		s += "email:" + email + ",";
		s += "totalUnits:" + totalUnits;
		s += "ownedProvinces:" + ownedProvinces + ",";
		s += "registerDate:" + registerDate;
		return s + "}";
	}
}
