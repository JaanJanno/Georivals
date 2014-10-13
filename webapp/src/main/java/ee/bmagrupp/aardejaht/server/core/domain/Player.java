package ee.bmagrupp.aardejaht.server.core.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class Player implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	private int id;

	@Column(nullable = false)
	private String userName;

	private String email;

	@Column(nullable = false)
	private String sid;

	@Temporal(TemporalType.TIMESTAMP)
	private Date lastLogin;

	protected Player() {
		super();
	}

	public Player(String userName, String email, String sid) {
		super();
		this.userName = userName;
		this.email = email;
		this.sid = sid;
	}

	public Player(String userName, String sid) {
		super();
		this.userName = userName;
		this.sid = sid;
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

	public Date getLastLogin() {
		return lastLogin;
	}

	public void setLastLogin(Date lastLogin) {
		this.lastLogin = lastLogin;
	}

	public int getId() {
		return id;
	}

	public String getSid() {
		return sid;
	}

	@Override
	public String toString() {
		return "Player [id=" + id + ", userName=" + userName + ", email="
				+ email + ", sid=" + sid + ", lastLogin=" + lastLogin + "]";
	}

}
