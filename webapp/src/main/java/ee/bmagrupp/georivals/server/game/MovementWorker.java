package ee.bmagrupp.georivals.server.game;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ee.bmagrupp.georivals.server.core.domain.Movement;

/**
 * Thread that will run all the time and end a {@link Movement} when the correct
 * time comes.
 * 
 * @author TKasekamp
 *
 */
@Component
public class MovementWorker implements Runnable {

	private static Logger LOG = LoggerFactory.getLogger(MovementWorker.class);

	@Autowired
	EndMovementService endMovServ;

	private Date endDate;

	public MovementWorker() {

	}

	@Override
	public void run() {
		LOG.info("Worker running with endDate " + endDate);
		endMovServ.handleMovement(endDate);
		endDate = endMovServ.getNextMovement();
		LOG.info("Ended movement. Next date is " +endDate);

	}

	public void setEndDate(Date date) {
		if (endDate == null) {
			LOG.info("Replaced null with " + date);
			endDate = date;
		} else if (endDate.after(date)) {
			LOG.info("Replaced " + endDate + " with " + date);
			endDate = date;
		}
	}
}
