package ee.bmagrupp.aardejaht.server.domain;

public class User {

	private int ID;
	private String username;
	private String email;

	public User(String email) {
		super();
		this.email = email;
		this.username = "lollakas";
		this.ID = 0;
	}

	public int getID() {
		return ID;
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

	@Override
	public String toString() {
		return "User [ID=" + ID + ", username=" + username + ", email=" + email
				+ "]";
	}
}
