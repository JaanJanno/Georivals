package ee.bmagrupp.aardejaht.server.core.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class Movement implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	private int id;

	@ManyToOne(optional = false)
	private Unit unit;

	@ManyToOne(optional = false)
	private Province origin;

	@ManyToOne(optional = false)
	private Province destination;

	@Column(nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date start;

	@Column(nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date end;

	protected Movement() {
		super();
	}

	public Movement(Unit unit, Province origin, Province destination,
			Date start, Date end) {
		super();
		this.unit = unit;
		this.origin = origin;
		this.destination = destination;
		this.start = start;
		this.end = end;
	}

	public int getId() {
		return id;
	}

	public Unit getUnit() {
		return unit;
	}

	public Province getOrigin() {
		return origin;
	}

	public Province getDestination() {
		return destination;
	}

	public Date getStart() {
		return start;
	}

	public Date getEnd() {
		return end;
	}

}
