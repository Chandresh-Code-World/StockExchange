package com.jp.stock.service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import com.jp.stock.model.BuyOrSell;
import com.jp.stock.model.Stock;
import com.jp.stock.model.Trade;

/**
 * StockQuery interface exposes the method to operate on a given Stock It
 * calculate the dividend yield, P/E Ratio, Stock Price and record trades for a
 * given stock.
 * 
 * @author chandresh.mishra
 * 
 */

public interface StockQuery {

	/**
	 * Calculates the dividend yield for a given stock.
	 * 
	 * @param symbol
	 *            Stock symbol.
	 * @param price
	 *            price of Stock
	 * 
	 * @return A BigDecimal value which represents the dividend yield for a
	 *         given stock.
	 */
	public BigDecimal getDividendYield(String symbol, BigDecimal price);

	/**
	 * Calculates the P/E Ratio for a given stock.
	 * 
	 * @param symbol
	 *            Stock symbol.
	 * @param price
	 *            price of Stock
	 * 
	 * @return A BigDecimal value which represents the P/E Ratio for a given
	 *         stock.
	 */

	public BigDecimal getPERatio(String symbol, BigDecimal price);

	/**
	 * Record a trade in the Simple Stocks application.
	 * 
	 * @param symbol
	 *            Stock symbol.
	 * @param timestamp
	 *            Time stamp for a trade
	 * @param quantity
	 *            quantity of a stock traded
	 * @param indicator
	 *            Buy or Sell of stock indicator
	 * @param price
	 *            price of Stock
	 * @return Trade , the saved entity
	 */
	public Trade recordTrade(String symbol, Date timestamp,
			BigInteger quantity, BuyOrSell indicator, BigDecimal price);

	/**
	 * Gets the volume weighted stock price based on the trades in last 15
	 * minutes.
	 * 
	 * @param stock
	 *            Stock symbol.
	 * @return BigDecimal the VolumeWeightedStockPrice in the last in 15
	 *         minutes.
	 */
	public BigDecimal getVolumeWeightedStockPrice(String symbol);

	/**
	 * Saves the list of Stock in Gemfire Region
	 * 
	 * @param stockList
	 *            List of Stock
	 */
	public void saveStockCollection(List<Stock> stockList);

}
