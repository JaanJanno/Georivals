package ee.bmagrupp.aardejaht.server.core.old;

import java.util.Date;
import java.io.Serializable;

import javax.persistence.*;

@SuppressWarnings("serial")
@Entity
public class Trek implements Serializable {
	@Id
	@GeneratedValue
	private int id;
	@Column(nullable = false)
	private Date startTime;
	private Date endTime;
	private int difference;
	private int score;
	@ManyToOne
	private User user;
	@ManyToOne
	private Treasure treasure;

	public Trek(User user, Treasure treasure) {
		super();
		this.startTime = new Date();
		this.user = user;
		this.treasure = treasure;
	}

	protected Trek() {

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

	public int getId() {
		return id;
	}

	public Date getStartTime() {
		return startTime;
	}

	public User getUser() {
		return user;
	}

	public Treasure getTreasure() {
		return treasure;
	}

	@Override
	public String toString() {
		return "Trek [id=" + id + ", startTime=" + startTime + ", endTime="
				+ endTime + ", difference=" + difference + ", score=" + score
				+ ", user=" + user + ", treasure=" + treasure + "]";
	}

}
