package ee.bmagrupp.georivals.server.core.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class BattleHistory implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@ManyToOne(fetch = FetchType.LAZY)
	private Province location;

	@Column(nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date date;

	@ManyToOne(fetch = FetchType.LAZY)
	private Player attacker;

	@ManyToOne(fetch = FetchType.LAZY)
	private Player defender;

	@Column(nullable = false)
	private boolean attackerWon;

	@Column(nullable = false)
	private int attackerStrength;

	@Column(nullable = false)
	private int defenderStrength;

	@Column(nullable = false)
	private int attackerLosses;

	@Column(nullable = false)
	private int defenderLosses;

	protected BattleHistory() {
		super();
	}

	public BattleHistory(Province location, Date date, Player attacker,
			Player defender, boolean attackerWon, int attackerStrength,
			int defenderStrength, int attackerLosses, int defenderLosses) {
		super();
		this.location = location;
		this.date = date;
		this.attacker = attacker;
		this.defender = defender;
		this.attackerWon = attackerWon;
		this.attackerStrength = attackerStrength;
		this.defenderStrength = defenderStrength;
		this.attackerLosses = attackerLosses;
		this.defenderLosses = defenderLosses;
	}

	public int getId() {
		return id;
	}

	public Province getLocation() {
		return location;
	}

	public Date getDate() {
		return date;
	}

	public Player getAttacker() {
		return attacker;
	}

	public Player getDefender() {
		return defender;
	}

	public boolean isAttackerWon() {
		return attackerWon;
	}

	public int getAttackerStrength() {
		return attackerStrength;
	}

	public int getDefenderStrength() {
		return defenderStrength;
	}

	public int getAttackerLosses() {
		return attackerLosses;
	}

	public int getDefenderLosses() {
		return defenderLosses;
	}

	@Override
	public String toString() {
		return "BattleHistory [id=" + id + ", location=" + location + ", date="
				+ date + ", attacker=" + attacker + ", defender=" + defender
				+ ", attackerWon=" + attackerWon + ", attackerStrength="
				+ attackerStrength + ", defenderStrength=" + defenderStrength
				+ ", attackerLosses=" + attackerLosses + ", defenderLosses="
				+ defenderLosses + "]";
	}

}
