/**
 * 
 */
package com.jp.stock.exception;

/**
 * 
 * A generic exception for the stock exchange.
 * 
 * @author chandresh.mishra
 */
public class StockMarketExchangeException extends RuntimeException {

	private static final long serialVersionUID = 1381320130022416598L;

	public StockMarketExchangeException(String message) {
		super(message);
	}
}
