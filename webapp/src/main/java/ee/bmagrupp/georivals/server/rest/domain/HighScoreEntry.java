package ee.bmagrupp.georivals.server.rest.domain;

import java.io.Serializable;

/**
 * Object for sending HighScore data to mobile app.
 * 
 * @author TKasekamp
 *
 */
public class HighScoreEntry implements Serializable {

	private static final long serialVersionUID = 1L;

	private final int id;

	private final String username;

	private final double averageUnits;

	private final int provincesOwned;

	public HighScoreEntry(int id, String username, double averageUnits,
			int provincesOwned) {
		super();
		this.id = id;
		this.username = username;
		this.averageUnits = averageUnits;
		this.provincesOwned = provincesOwned;
	}

	public int getId() {
		return id;
	}

	public String getUsername() {
		return username;
	}

	public double getAverageUnits() {
		return averageUnits;
	}

	public int getProvincesOwned() {
		return provincesOwned;
	}

	@Override
	public String toString() {
		String s = new String("{");
		s += "{id:" + id + ";";
		s += "{username:" + username + ";";
		s += "{averageUnits:" + averageUnits + ";";
		s += "{provincesOwned:" + provincesOwned + ";";
		return s + "}";
	}
}
