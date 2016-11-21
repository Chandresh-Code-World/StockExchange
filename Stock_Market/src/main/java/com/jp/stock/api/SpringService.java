/**
 * 
 */
package com.jp.stock.api;

import org.apache.log4j.Logger;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.ClassPathResource;

/**
 * It loads the application context using a singleton pattern proposed by Bill
 * Pugh.
 * 
 * @author chandresh.mishra
 * 
 */

public class SpringService {

	/**
	 * Logger service for the class.
	 */
	private Logger logger = Logger.getLogger(SpringService.class);

	/**
	 * Spring context files pattern defined for the Simple Stocks application.
	 */

	private String contextFile = "com/jp/stock/gemfireData/StockExchangeContext.xml";

	private ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext();

	/**
	 * Private constructor for the factory which prevents new instance
	 * 
	 * The main purpose of the constructor is to load the Spring context for the
	 * Simple Stocks application.
	 */

	private SpringService() {

		logger.info("Loading Spring Context for Super Simple Stocks.");
		context.setConfigLocation(new ClassPathResource(contextFile).getPath());
		context.refresh();

	}

	/**
	 * Holder class for the singleton factory instance. It is loaded on the
	 * first execution of SpringService#getInstance()}
	 */
	private static class SingletonHelper {
		private static final SpringService INSTANCE = new SpringService();
	}

	/**
	 * Gets the singleton instance of the spring services.
	 * 
	 * @return An object of the SpringService, which represents the service to
	 *         access to all beans in the spring container.
	 */
	public static SpringService getInstance() {
		return SingletonHelper.INSTANCE;
	}

	/**
	 * Gets an object of the bean configured in the Spring context for the class
	 * <i>objectClass</i>.
	 * 
	 * @param objectClass
	 *            The class of the bean configured in the Spring context.
	 * 
	 * @return Return an object corresponding to the bean configured in the
	 *         Spring context represented for the class <i>objectClass</i>.
	 */
	public <T> T getBean(Class<T> objectClass) {
		return context.getBean(objectClass);
	}
}
