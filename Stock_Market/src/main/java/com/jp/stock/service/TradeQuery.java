/**
 * 
 */
package com.jp.stock.service;

import java.math.BigDecimal;

/**
 * It exposes the method for calculation of GBCE All Share Index
 * 
 * @author chandresh.mishra
 * 
 */
public interface TradeQuery {

	/**
	 * Calculates the GBCE All Share Index using the geometric mean of prices
	 * for all stocks.
	 * 
	 * @return BigDecimal geometric mean of prices for all stocks
	 */
	public BigDecimal calculateGBCEAllShareIndex();

}
