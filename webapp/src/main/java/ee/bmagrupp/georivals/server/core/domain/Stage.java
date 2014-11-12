package ee.bmagrupp.georivals.server.core.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Stage implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@ManyToOne(optional = false)
	private Unit attacker;

	@ManyToOne(optional = false)
	private Unit defender;

	@Column(nullable = false)
	private int attackerLosses;

	@Column(nullable = false)
	private int defenderLosses;

	protected Stage() {
		super();
	}

	public Stage(Unit attacker, Unit defender, int attackerLosses,
			int defenderLosses) {
		super();
		this.attacker = attacker;
		this.defender = defender;
		this.attackerLosses = attackerLosses;
		this.defenderLosses = defenderLosses;
	}

	public int getId() {
		return id;
	}

	public Unit getAttacker() {
		return attacker;
	}

	public Unit getDefender() {
		return defender;
	}

	public int getAttackerLosses() {
		return attackerLosses;
	}

	public int getDefenderLosses() {
		return defenderLosses;
	}

}
