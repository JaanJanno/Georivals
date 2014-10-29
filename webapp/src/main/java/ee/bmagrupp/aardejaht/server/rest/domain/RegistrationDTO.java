package ee.bmagrupp.aardejaht.server.rest.domain;

/**
 * Data transfer object for registration.
 * 
 * @author TKasekamp
 *
 */
public class RegistrationDTO {
	private String userName;
	private String email;
	private double home_lat;
	private double home_long;

	public RegistrationDTO() {

	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public double getHome_lat() {
		return home_lat;
	}

	public void setHome_lat(double home_lat) {
		this.home_lat = home_lat;
	}

	public double getHome_long() {
		return home_long;
	}

	public void setHome_long(double home_long) {
		this.home_long = home_long;
	}

	@Override
	public String toString() {
		return "RegistrationDTO [userName=" + userName + ", email=" + email
				+ ", home_lat=" + home_lat + ", home_long=" + home_long + "]";
	}

	public String toJson() {
		return "{\"userName\":\"" + userName + "\",\"email\":\"" + email
				+ "\",\"home_lat\":" + home_lat + ",\"home_long\":" + home_long
				+ "}";
	}

}
