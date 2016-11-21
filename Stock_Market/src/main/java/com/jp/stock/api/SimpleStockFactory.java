/**
 * 
 */
package com.jp.stock.api;

import com.jp.stock.service.StockQuery;
import com.jp.stock.service.TradeQuery;
import com.jp.stock.util.LoadExchangeData;

/**
 * Factory of the services in the Simple Stock application.
 * 
 * All external systems or high level layers in the application will access to
 * the services through this factory. It is implemented as a singleton,
 * following the singleton pattern proposed by Bill Pugh.
 * 
 * @author chandresh.mishra
 * 
 */

public class SimpleStockFactory {

	/**
	 * Load the spring context for the Application
	 */
	SpringService springService = SpringService.getInstance();

	/**
	 * Private constructor for the factory which prevents new instance
	 */
	private SimpleStockFactory() {
		springService.getBean(LoadExchangeData.class);
	}

	/**
	 * Holder class for the singleton factory instance. It is loaded on the
	 * first execution of SimpleStockFactory#getInstance()}
	 */
	private static class SingletonHelper {
		private static final SimpleStockFactory INSTANCE = new SimpleStockFactory();
	}

	/**
	 * Gets the singleton instance of the factory of the services in the Simple
	 * Stock application.
	 * 
	 * @return An object of the SimpleStockFactory, which represents the factory
	 *         to access to all services in the Simple Stock application.
	 */
	public static SimpleStockFactory getInstance() {
		return SingletonHelper.INSTANCE;
	}

	/**
	 * Gets the instance StockQuery through SimpleStockFactory instance
	 * 
	 * @return An object of the StockQuery service
	 */
	public StockQuery getStockService() {
		return springService.getBean(StockQuery.class);
	}

	/**
	 * Gets the instance TradeQuery through SimpleStockFactory instance
	 * 
	 * @return An object of the TradeQuery service
	 */
	public TradeQuery getTradeService() {
		return springService.getBean(TradeQuery.class);
	}
}
