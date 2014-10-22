package ee.bmagrupp.aardejaht.server.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.FlushMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.orm.hibernate3.SessionHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations = { "classpath:/applicationContext.xml" })
public class TestBase {
	private FlushMode flushMode = FlushMode.MANUAL;
	@Autowired
	SessionFactory sessionFactory;
	private static final Log logger = LogFactory.getLog(TestBase.class);

	@Before
	public void setUp() throws Exception {
		// setup hibernate session holder
		Session session = getSession(this.sessionFactory);
		TransactionSynchronizationManager.bindResource(sessionFactory,
				new SessionHolder(session));
		logger.debug("Hibernate session is bound");
	}

	@After
	public void tearDown() throws Exception {
		// single session mode
		SessionHolder sessionHolder = (SessionHolder) TransactionSynchronizationManager
				.unbindResource(sessionFactory);
		closeSession(sessionHolder.getSession(), sessionFactory);
		logger.debug("Hibernate session is closed");
	}

	/**
	 * This simply verifies that test base loads and unloads stuff
	 * 
	 * @throws Exception
	 */
	@Test
	public void verifyTestBase() throws Exception {
		assertNotNull(getSession(this.sessionFactory));
	}

	/**
	 * Get a Session for the SessionFactory that this filter uses. Note that
	 * this just applies in single session mode!
	 *
	 * 
	 * The default implementation delegates to the
	 * SessionFactoryUtils.getSession method and sets the Session's flush mode
	 * to "NEVER".
	 *
	 * 
	 * Can be overridden in subclasses for creating a Session with a custom
	 * entity interceptor or JDBC exception translator.
	 *
	 * @param sessionFactory
	 *            the SessionFactory that this filter uses
	 * @return the Session to use
	 * @throws DataAccessResourceFailureException
	 *             if the Session could not be created
	 * @see org.springframework.orm.hibernate3.SessionFactoryUtils#getSession(SessionFactory,
	 *      boolean)
	 * @see org.hibernate.FlushMode#NEVER
	 */
	protected Session getSession(SessionFactory sessionFactory)
			throws DataAccessResourceFailureException {
		Session session = SessionFactoryUtils.getSession(sessionFactory, true);
		FlushMode flushMode = this.flushMode;
		if (flushMode != null) {
			session.setFlushMode(flushMode);
		}
		return session;
	}

	/**
	 * Close the given Session. Note that this just applies in single session
	 * mode!
	 *
	 * 
	 * Can be overridden in subclasses, e.g. for flushing the Session before
	 * closing it. See class-level javadoc for a discussion of flush handling.
	 * Note that you should also override getSession accordingly, to set the
	 * flush mode to something else than NEVER.
	 *
	 * @param session
	 *            the Session used for filtering
	 * @param sessionFactory
	 *            the SessionFactory that this filter uses
	 */
	protected void closeSession(Session session, SessionFactory sessionFactory) {
		SessionFactoryUtils.closeSession(session);
	}

}
