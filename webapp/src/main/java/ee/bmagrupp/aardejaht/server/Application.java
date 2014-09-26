package ee.bmagrupp.aardejaht.server;

import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import com.mysql.jdbc.CommunicationsException;


import javax.sql.DataSource;

//import org.apache.commons.dbcp.BasicDataSource;
@Configuration
// @EnableBatchProcessing
@ComponentScan
// spring boot configuration
@EnableAutoConfiguration
// file that contains the properties
@PropertySource("classpath:application.properties")
public class Application {

    private static Logger LOG = LoggerFactory.getLogger(Application.class);
	/**
	 * Load the properties
	 */
	@Value("${database.driver}")
	private String databaseDriver;
	@Value("${database.url}")
	private String databaseUrl;
	@Value("${database.username}")
	private String databaseUsername;
	@Value("${database.password}")
	private String databasePassword;
	
	private final boolean USE_HSQLDB = false;

	public static void main(String[] args) {
		 SpringApplication.run(Application.class, args);

		 
//		 ClassPathXmlApplicationContext context = new 
//                 ClassPathXmlApplicationContext("applicationContext.xml");
	}

	@Bean
	public DataSource dataSource() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		 LOG.info("sadfhsdafu");
		// clever way to set the password from environment variables
		databasePassword = "root";
		if (USE_HSQLDB)
			setDatabaseConfig();
//			dataSource.setConnectionProperties(connectionProperties);
		

		dataSource.setDriverClassName(databaseDriver);
		dataSource.setUrl(databaseUrl);
		dataSource.setUsername(databaseUsername);
		dataSource.setPassword(databasePassword);

		return dataSource;
	}
	
	private void setDatabaseConfig() {
		databaseDriver ="org.hsqldb.jdbcDriver";
		databaseUrl= "jdbc:hsqldb:hsql://localhost/testdb";
		databaseUsername = "sa";
		databasePassword = "";
	}

}
