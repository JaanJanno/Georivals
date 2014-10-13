package ee.bmagrupp.aardejaht.server.core.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Unit implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	private int id;

	@Column(nullable = false)
	private int size;

	@ManyToOne(optional = false)
	private Player owner;

	@Column(nullable = false)
	@Enumerated(EnumType.ORDINAL)
	private UnitType type;

	protected Unit() {
		super();
	}

	public Unit(int size, Player owner) {
		super();
		this.size = size;
		this.owner = owner;
		this.type = UnitType.NORMAL;
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

	public Player getOwner() {
		return owner;
	}

	public UnitType getType() {
		return type;
	}

}
