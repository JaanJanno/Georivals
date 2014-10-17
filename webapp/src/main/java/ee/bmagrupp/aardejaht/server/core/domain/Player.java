package ee.bmagrupp.aardejaht.server.core.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class Player implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(nullable = false)
	private String userName;

	private String email;

	@Column(nullable = false)
	private String sid;

	@Temporal(TemporalType.TIMESTAMP)
	private Date lastLogin;

	@Column(nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date registerDate;

	@OneToMany(fetch = FetchType.LAZY)
	private Set<Ownership> provinces;

	@OneToOne
	private HomeOwnership home;

	protected Player() {
		super();
	}

	public Player(String userName, String email, String sid) {
		super();
		this.userName = userName;
		this.email = email;
		this.sid = sid;
		this.registerDate = new Date();
	}

	public Player(String userName, String sid) {
		super();
		this.userName = userName;
		this.sid = sid;
		this.registerDate = new Date();
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

	public Date getRegisterDate() {
		return registerDate;
	}

	public Set<Ownership> getProvinces() {
		return provinces;
	}

	public HomeOwnership getHome() {
		return home;
	}

	public String getSid() {
		return sid;
	}

	public void setProvinces(Set<Ownership> provinces) {
		this.provinces = provinces;
	}

	public void setHome(HomeOwnership home) {
		this.home = home;
	}

	@Override
	public String toString() {
		return "Player [id=" + id + ", userName=" + userName + ", email="
				+ email + ", sid=" + sid + ", lastLogin=" + lastLogin + "]";
	}

}
