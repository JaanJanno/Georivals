package ee.bmagrupp.aardejaht.server.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class HighScoreEntry {

	@Id
	@GeneratedValue
	private int id;
	@Column(nullable = false)
	private String username;
	@Column(nullable = false)
	private double averageUnits;
	@Column(nullable = false)
	private int territoriesOwned;

	public HighScoreEntry(int id, String username, double averageUnits,
			int territoriesOwned) {
		super();
		this.id = id;
		this.username = username;
		this.averageUnits = averageUnits;
		this.territoriesOwned = territoriesOwned;
	}

	protected HighScoreEntry() {

	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public double getAverageUnits() {
		return averageUnits;
	}

	public void setAverageUnits(double averageUnits) {
		this.averageUnits = averageUnits;
	}

	public int getTerritoriesOwned() {
		return territoriesOwned;
	}

	public void setTerritoriesOwned(int territoriesOwned) {
		this.territoriesOwned = territoriesOwned;
	}

	@Override
	public String toString() {
		String s = new String("{");
		s += "{id:" + id + ";";
		s += "{username:" + username + ";";
		s += "{averageUnits:" + averageUnits + ";";
		s += "{territoriesOwned:" + territoriesOwned + ";";
		return s + "}";
	}
}
