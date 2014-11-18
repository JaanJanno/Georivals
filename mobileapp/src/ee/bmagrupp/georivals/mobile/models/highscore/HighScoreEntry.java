package ee.bmagrupp.georivals.mobile.models.highscore;

public class HighScoreEntry {

	private int id;
	private String username;
	private double averageUnits;
	private int provincesOwned;

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

	public int getProvincesOwned() {
		return provincesOwned;
	}

	public void setProvincesOwned(int provincesOwned) {
		this.provincesOwned = provincesOwned;
	}

	@Override
	public String toString() {
		String s = new String("{");
		s += "id:" + id + ",";
		s += "username:" + username + ",";
		s += "averageUnits:" + averageUnits + ",";
		s += "provincesOwned:" + provincesOwned;
		return s + "}";
	}
}
