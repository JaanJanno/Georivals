package ee.bmagrupp.georivals.server.util;

import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;

import ee.bmagrupp.georivals.server.core.repository.MovementRepository;
import ee.bmagrupp.georivals.server.game.MovementWorker;

/**
 * This class schedules movements at server startup. Will be used only once.
 * 
 * @author TKasekamp
 *
 */
@Component
public class MovementScheduleUtil {
	private static final Logger LOG = LoggerFactory
			.getLogger(MovementScheduleUtil.class);
	@Autowired
	private MovementRepository movRepo;

	@Autowired
	private ThreadPoolTaskScheduler threadPoolTaskScheduler;

	@Resource
	private MovementWorker worker;

	@PostConstruct
	private void scheduleMovements() {
		LOG.info("Starting to schedule movements");
		List<Date> endDates = movRepo.getAllEndDates();
		for (Date date : endDates) {
			worker.setEndDate(date);
			threadPoolTaskScheduler.schedule(worker, date);
		}
		LOG.info("Movements scheduled");
	}
}
