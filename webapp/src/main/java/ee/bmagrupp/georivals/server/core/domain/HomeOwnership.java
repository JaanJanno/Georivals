package ee.bmagrupp.georivals.server.core.domain;

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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import ee.bmagrupp.georivals.server.game.util.Constants;
import ee.bmagrupp.georivals.server.game.util.GeneratorUtil;

@Entity
public class HomeOwnership implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private Province province;

	@Column(nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastVisit;

	@Column(nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date startDate;

	@OneToMany(fetch = FetchType.LAZY)
	private Set<Unit> units = new HashSet<>();

	private String provinceName;

	protected HomeOwnership() {
		super();
	}

	public HomeOwnership(Province province, Unit unit) {
		super();
		this.province = province;
		this.startDate = new Date();
		this.lastVisit = new Date();

		// unit cannot be null
		if (unit == null) {
			this.units = null;
		} else {
			this.units.add(unit);
		}
		this.setProvinceName(GeneratorUtil.generateString(
				Constants.PROVINCE_NAME_LENGTH, province.getLatitude(),
				province.getLongitude()));
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

	public Province getProvince() {
		return province;
	}

	public Date getStartDate() {
		return startDate;
	}

	public String getProvinceName() {
		return provinceName;
	}

	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}

	public boolean addUnit(Unit unit) {
		if (this.units == null) {
			this.units = new HashSet<>();
		}
		return units.add(unit);
	}

	/**
	 * Returns the sum of the unit sizes.
	 * 
	 * @author TKasekamp
	 * @return int
	 */
	public int countUnits() {
		int overall = 0;
		for (Unit unit : units) {
			if (unit.getState() == UnitState.CLAIMED) {
				overall += unit.getSize();
			}
		}
		return overall;
	}

	@Override
	public String toString() {
		return "HomeOwnership [id=" + id + ", province=" + province
				+ ", lastVisit=" + lastVisit + ", startDate=" + startDate
				+ ", units=" + units + ", provinceName=" + provinceName + "]";
	}

	public void setProvince(Province newHome) {
		this.province = newHome;
	}

}
