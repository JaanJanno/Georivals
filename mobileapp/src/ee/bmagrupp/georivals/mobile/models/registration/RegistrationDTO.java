package ee.bmagrupp.georivals.mobile.models.registration;

/**
 * Data transfer object for registration.
 * 
 * @author TKasekamp
 * 
 */
public class RegistrationDTO {
	private String userName;
	private String email;
	private double homeLat;
	private double homeLong;

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

	public double getHomeLat() {
		return homeLat;
	}

	public void setHomeLat(double homeLat) {
		this.homeLat = homeLat;
	}

	public double getHomeLong() {
		return homeLong;
	}

	public void setHomeLong(double homelong) {
		this.homeLong = homelong;
	}

	@Override
	public String toString() {
		return "RegistrationDTO [userName=" + userName + ", email=" + email
				+ ", homeLat=" + homeLat + ", homeLong=" + homeLong + "]";
	}

	public String toJson() {
		return "{\"userName\":\"" + userName + "\",\"email\":\"" + email
				+ "\",\"homeLat\":" + homeLat + ",\"homeLong\":" + homeLong
				+ "}";
	}

	public RegistrationDTO(String userName, String email) {
		super();
		this.userName = userName;
		this.email = email;
	}

	public RegistrationDTO(String userName, String email, double homeLat,
			double homeLong) {
		super();
		this.userName = userName;
		this.email = email;
		this.homeLat = homeLat;
		this.homeLong = homeLong;
	}
}
