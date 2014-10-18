package ee.bmagrupp.aardejaht.server.core.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 * The unit has to know, where it came from. The unit class itself should not
 * hold that info. A new object is needed to hold that info during a battle.
 * 
 * @author TKasekamp
 *
 */
@Entity
public class BattleUnit implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	/* Should probably be set to EAGER */
	@ManyToOne(fetch = FetchType.LAZY)
	private Unit unit;

	@ManyToOne(fetch = FetchType.LAZY)
	private Province origin;

	protected BattleUnit() {
		super();
	}

	public BattleUnit(Unit unit, Province origin) {
		super();
		this.unit = unit;
		this.origin = origin;
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

}
