package ee.bmagrupp.georivals.server.core.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class Movement implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@ManyToOne(optional = false)
	private Unit unit;

	@ManyToOne(optional = false)
	private Province origin;

	@ManyToOne(optional = false)
	private Province destination;

	@ManyToOne(optional = false)
	private Player player;

	@Column(nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date start;

	@Column(nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date endDate;

	protected Movement() {
		super();
	}

	public Movement(Unit unit, Province origin, Province destination,
			Player player, Date start, Date endDate) {
		super();
		this.unit = unit;
		this.origin = origin;
		this.destination = destination;
		this.player = player;
		this.start = start;
		this.endDate = endDate;
	}

	public Player getPlayer() {
		return player;
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

	public Date getEndDate() {
		return endDate;
	}

	@Override
	public String toString() {
		return "Movement [id=" + id + ", unit=" + unit + ", origin=" + origin
				+ ", destination=" + destination + ", player=" + player
				+ ", start=" + start + ", endDate=" + endDate + "]";
	}

	public String logString() {
		return "Movement [id=" + id + ", unit=" + unit + ", origin=" + origin
				+ ", destination=" + destination + ", playerName="
				+ player.getUserName() + ", playerId=" + player.getId()
				+ ", start=" + start + ", endDate=" + endDate + "]";
	}

}
