package ee.bmagrupp.aardejaht.server.core.old;

import java.io.Serializable;

import javax.persistence.*;


@Entity
@Table(name = "user_data")
@SuppressWarnings("serial")
public class User implements Serializable {

	@Id
	@GeneratedValue
	private int id;
	@Column(nullable = false)
	private String username;
	@Column(nullable = false)
	private String email;

	protected User() {
	}

	public User(String email, String username) {
		super();
		this.email = email;
		this.username = username;
	}

	public int getId() {
		return id;
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
		return "User [id=" + id + ", username=" + username + ", email=" + email
				+ "]";
	}
}
