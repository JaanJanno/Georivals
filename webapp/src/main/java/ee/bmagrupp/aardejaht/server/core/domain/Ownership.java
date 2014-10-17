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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class Ownership implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@ManyToOne(optional = false)
	private Player owner;

	@ManyToOne(optional = false)
	private Province province;

	@Column(nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastVisit;

	@Column(nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date startDate;

	@OneToMany(fetch = FetchType.LAZY)
	private Set<Unit> units;

	protected Ownership() {
		super();
	}

	public Ownership(Player owner, Province province, Set<Unit> units) {
		super();
		this.owner = owner;
		this.province = province;
		this.units = units;
		this.startDate = new Date();
		this.lastVisit = new Date(); // TODO this might cause problems
	}

	public Date getLastVisit() {
		return lastVisit;
	}

	public void setLastVisit(Date lastVisit) {
		this.lastVisit = lastVisit;
	}

	public Set<Unit> getUnits() {
		return units;
	}

	public void setUnits(Set<Unit> units) {
		this.units = units;
	}

	public int getId() {
		return id;
	}

	public Player getOwner() {
		return owner;
	}

	public Province getProvince() {
		return province;
	}

	public Date getStartDate() {
		return startDate;
	}

}
