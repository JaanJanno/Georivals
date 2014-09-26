package ee.bmagrupp.aardejaht.server.domain;

import java.util.Date;

public class Trek {
	private int ID;
	private int treasure_id;
	private int user_id;
	private Date startTime;
	private Date endTime;
	private int difference;
	private int score;

	public Trek(int treasure_id, int user_id, Date startTime) {
		super();
		this.treasure_id = treasure_id;
		this.user_id = user_id;
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public int getDifference() {
		return difference;
	}

	public void setDifference(int difference) {
		this.difference = difference;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public int getID() {
		return ID;
	}

	public int getTreasure_id() {
		return treasure_id;
	}

	public int getUser_id() {
		return user_id;
	}

	public Date getStartTime() {
		return startTime;
	}

	@Override
	public String toString() {
		return "Trek [ID=" + ID + ", treasure_id=" + treasure_id + ", user_id="
				+ user_id + ", startTime=" + startTime + ", endTime=" + endTime
				+ ", difference=" + difference + ", score=" + score + "]";
	}
}
