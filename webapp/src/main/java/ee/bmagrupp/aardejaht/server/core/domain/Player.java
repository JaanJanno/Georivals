package ee.bmagrupp.aardejaht.server.core.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
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
	private Set<Ownership> ownedProvinces = new HashSet<>();

	@OneToOne(optional = false)
	private HomeOwnership home;

	protected Player() {
		super();
	}

	public Player(String userName, String email, String sid, Province province) {
		super();
		this.userName = userName;
		this.email = email;
		this.sid = sid;
		this.registerDate = new Date();
		this.createHome(province);
	}

	public Player(String userName, String sid, Province province) {
		super();
		this.userName = userName;
		this.sid = sid;
		this.registerDate = new Date();
		this.createHome(province);
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

	public Set<Ownership> getOwnedProvinces() {
		return ownedProvinces;
	}

	public HomeOwnership getHome() {
		return home;
	}

	public String getSid() {
		return sid;
	}

	public void setOwnedProvinces(Set<Ownership> provinces) {
		this.ownedProvinces = provinces;
	}

	public void setHome(HomeOwnership home) {
		this.home = home;
	}

	/**
	 * Creates a {@link HomeOwnership} with this {@link Province}. No
	 * {@link Unit}s will be added.
	 * 
	 * @param province
	 *            {@link Province} to set as Home Province.
	 */
	public void createHome(Province province) {
		this.home = new HomeOwnership(province, null);
	}

	public boolean addOwnership(Ownership e) {
		return ownedProvinces.add(e);
	}

	@Override
	public String toString() {
		return "Player [id=" + id + ", userName=" + userName + ", email="
				+ email + ", sid=" + sid + ", lastLogin=" + lastLogin
				+ ", registerDate=" + registerDate + ", provinces="
				+ ownedProvinces + ", home=" + home + "]";
	}

}
