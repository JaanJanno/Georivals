package ee.bmagrupp.aardejaht.server.core.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
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
public class Battle implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@ManyToOne(fetch = FetchType.LAZY)
	private Province location;

	@Column(nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date startDate;

	@ManyToOne(fetch = FetchType.LAZY)
	private Player attacker;

	@ManyToOne(fetch = FetchType.LAZY)
	private Player defender;

	@OneToMany
	private List<Stage> stages;

	@OneToMany
	private Set<Unit> defenderUnits;

	@OneToMany
	private Set<BattleUnit> attackerBattleUnits;

	protected Battle() {
		super();
	}

	public Battle(Province location, Date startDate, Player attacker,
			Player defender, Set<Unit> defenderUnits,
			Set<BattleUnit> attackerBattleUnits) {
		super();
		this.location = location;
		this.startDate = startDate;
		this.attacker = attacker;
		this.defender = defender;
		this.defenderUnits = defenderUnits;
		this.attackerBattleUnits = attackerBattleUnits;
	}

	public List<Stage> getStages() {
		return stages;
	}

	public void setStages(List<Stage> stages) {
		this.stages = stages;
	}

	public int getId() {
		return id;
	}

	public Province getLocation() {
		return location;
	}

	public Date getStartDate() {
		return startDate;
	}

	public Player getAttacker() {
		return attacker;
	}

	public Player getDefender() {
		return defender;
	}

	public Set<Unit> getDefenderUnits() {
		return defenderUnits;
	}

	public Set<BattleUnit> getAttackerBattleUnits() {
		return attackerBattleUnits;
	}

}
