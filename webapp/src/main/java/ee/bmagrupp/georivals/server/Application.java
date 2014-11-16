package ee.bmagrupp.georivals.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

@Configuration
// @EnableBatchProcessing
@ComponentScan
// spring boot configuration
@EnableAutoConfiguration
public class Application {

	@SuppressWarnings("unused")
	private static Logger LOG = LoggerFactory.getLogger(Application.class);

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);

	}

	@Bean
	public ThreadPoolTaskScheduler threadPoolTaskScheduler() {
		ThreadPoolTaskScheduler t = new ThreadPoolTaskScheduler();
		// TODO these settings must be optimal
		t.setPoolSize(10);
		return t;
	}

}
