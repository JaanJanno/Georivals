package ee.bmagrupp.aardejaht.server.core.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Unit implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(nullable = false)
	private int size;

	@Column(nullable = false)
	@Enumerated(EnumType.ORDINAL)
	private UnitState state;

	@Column(nullable = false)
	@Enumerated(EnumType.ORDINAL)
	private UnitType type;

	protected Unit() {
		super();
	}

	/**
	 * Default Unit constructor. To be used when this Unit can be used by the
	 * player.
	 * 
	 * @param size
	 *            how large this Unit is.
	 */
	public Unit(int size) {
		super();
		this.size = size;
		this.type = UnitType.NORMAL;
		this.state = UnitState.CLAIMED;
	}

	/**
	 * Constructor for randomly generating Units that the Player can retrieve.
	 * 
	 * @param size
	 * @param state
	 *            {@link UnitState} should be UNCLAIMED
	 */
	public Unit(int size, UnitState state) {
		super();
		this.size = size;
		this.type = UnitType.NORMAL;
		this.state = state;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public int getId() {
		return id;
	}

	public UnitState getState() {
		return state;
	}

	public void setState(UnitState state) {
		this.state = state;
	}

	public UnitType getType() {
		return type;
	}

	@Override
	public String toString() {
		return "Unit [id=" + id + ", size=" + size + ", state=" + state
				+ ", type=" + type + "]";
	}

}
