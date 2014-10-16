package ee.bmagrupp.aardejaht.server.core.old;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class TestUser {

	@Id
	@GeneratedValue
	private int id;
	@Column(nullable = false)
	private String username;
	@Column(nullable = false)
	private String email;

	@Column(nullable = false)
	private int totalUnits;

	@Column(nullable = false)
	private int ownedProvinces;

	@Column(nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date registerDate;

	protected TestUser() {
		super();
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

	public Date getRegisterDate() {
		return registerDate;
	}

}
