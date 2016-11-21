/**
 * 
 */
package com.jp.stock.service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jp.stock.exception.StockMarketExchangeException;
import com.jp.stock.integration.gemfire.StockDao;
import com.jp.stock.integration.gemfire.TradeDao;
import com.jp.stock.model.BuyOrSell;
import com.jp.stock.model.Stock;
import com.jp.stock.model.StockType;
import com.jp.stock.model.Trade;
import com.jp.stock.util.GenerateUniqueSequence;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 * It provides the Implementation for the StockQuery Interface
 * @author chandresh.mishra
 *
 */
/**
 * Register it as a bean in Spring context
 */
@Component
public class SimpleStockQuery implements StockQuery {

	/**
	 * Autowired Stock database service
	 */
	@Autowired
	private StockDao stockDao;
	/**
	 * Autowired Trade database service
	 */
	@Autowired
	private TradeDao tradeDao;

	/**
	 * Autowired unique key generation service
	 */
	@Autowired
	private GenerateUniqueSequence generateUniqueSequence;

	private Logger logger = Logger.getLogger(SimpleStockQuery.class);

	/**
	 * The precision scale in the calculation.
	 */
	private static final int PRECISION_SCALE = 7;

	/**
	 * Calculates the dividend yield of the given stock based on the given
	 * price. The result precision scale is 3 and applies
	 * {@link BigDecimal#ROUND_HALF_EVEN}.
	 * 
	 * @param symbol
	 *            the symbol of the stock to be calculated
	 * @param price
	 *            the price used in calculation
	 */
    @Override
	public BigDecimal getDividendYield(String symbol, BigDecimal price) {

		logger.info("Calculating Dividend Yield for the stock symbol: "
				+ symbol);

		// Throws exception in case stock symbol is null or blank
		if (StringUtils.isBlank(symbol))
			throw new StockMarketExchangeException(
					"Stock Symbol can not be null");

		// Get the valid stock from the Gemfire Memory
		Stock stock = getValidStock(symbol);
		// Check price is valid or not
		checkPositive(price);
		BigDecimal result = BigDecimal.ZERO;
		// calculation for common stock
		if (stock.getStockType() == StockType.COMMON) {
			result = stock.getLastDividend().divide(price, PRECISION_SCALE,
					BigDecimal.ROUND_HALF_EVEN);
		}
		// calculation for Preferred stock
		else if (stock.getStockType() == StockType.PREFERRED) {
			result = stock.getFixedDividend().multiply(stock.getParValue())
					.divide(price, PRECISION_SCALE, BigDecimal.ROUND_HALF_EVEN);
		}
		return result.setScale(3, BigDecimal.ROUND_HALF_EVEN);
	}

	/**
	 * Calculates the P/E Ratio of the given stock based on the given price. The
	 * result precision scale is 3 and applies
	 * {@link BigDecimal#ROUND_HALF_EVEN}.
	 * 
	 * @param symbol
	 *            the stock symbol to be calculated
	 * @param price
	 *            the trade price
	 */
    @Override
	public BigDecimal getPERatio(String symbol, BigDecimal price) {

		logger.info("Calculating P/E Ratio for the stock symbol: " + symbol);
		// calculating the dividend for the Stock
		BigDecimal dividend = getDividendYield(symbol, price);

		if (BigDecimal.ZERO.compareTo(dividend) >= 0)
			throw new StockMarketExchangeException(
					"illegal value for the Dividend for Stock" + symbol);

		BigDecimal PERatio = BigDecimal.ZERO;

		PERatio = price.divide(dividend, PRECISION_SCALE,
				BigDecimal.ROUND_HALF_EVEN);
		return PERatio.setScale(3, BigDecimal.ROUND_HALF_EVEN);
	}

	/**
	 * Records a trade, with time stamp, quantity of shares, buy or sell
	 * indicator and traded price.
	 */
    @Override
	public Trade recordTrade(String symbol, Date timestamp,
			BigInteger quantity, BuyOrSell indicator, BigDecimal price) {

		// Checking for blank and null values
		if (StringUtils.isBlank(symbol) || indicator == null
				|| timestamp == null) {
			throw new StockMarketExchangeException(
					"Stock Symbol and BuyOrSell indicator can not be null");
		}
		// checking the valid values
		checkPositive(new BigDecimal(quantity));
		checkPositive(price);
		// Fetching the Stock record with the symbol from the Gemfire region
		Stock stock = getValidStock(symbol);

		// Get a Unique sequence from gemfire region for each trade
		int tradeIdentifier = generateUniqueSequence.getUniqueSequence();
		// Trade object initialised with given values
		Trade trade = new Trade(tradeIdentifier, stock, timestamp, quantity,
				indicator, price);

		// saving the trade in Trade region
		return tradeDao.save(trade);
	}
    @Override
	public BigDecimal getVolumeWeightedStockPrice(String symbol) {

		logger.info("Calculating Dividend Yield for the stock symbol: "
				+ symbol);

		// Throws exception in case stock symbol is null or blank
		if (StringUtils.isBlank(symbol))
			throw new StockMarketExchangeException(
					"Stock Symbol can not be null");

		// Fetching all the trade with given Stock Symbol from trade region in
		// Gemfire
		Collection<Trade> tradeCollection = tradeDao
				.findAllTradeForStock(symbol);

		// Throws exception in case No Trade record found for the Stock
		if (tradeCollection.isEmpty())
			throw new StockMarketExchangeException(
					"No Trade record found for the Stock :" + symbol);

		// Get the trade record in last 15 min
		List<Trade> tradeList = getTradeRecordsByTime(tradeCollection, 15);

		BigDecimal volumeWeightedStockPrice = BigDecimal.ZERO;
		if (tradeList.isEmpty()) {
			return volumeWeightedStockPrice;
		}

		else {
			BigDecimal priceSum = BigDecimal.ZERO;
			BigInteger quantitySum = BigInteger.ZERO;

			for (Trade record : tradeList) {
				BigDecimal price = record.getPrice();
				checkPositive(price);
				BigDecimal quatity = new BigDecimal(record.getQuantity());
				checkPositive(quatity);

				priceSum = priceSum.add(price.multiply(quatity));
				quantitySum = quantitySum.add(record.getQuantity());
			}
			volumeWeightedStockPrice = priceSum.divide(new BigDecimal(
					quantitySum), PRECISION_SCALE, 3);
			return volumeWeightedStockPrice.setScale(0,
					BigDecimal.ROUND_HALF_EVEN);
		}

	}

	/**
	 * Validates the given value is a positive.
	 * 
	 * @param value
	 *            the value to be checked
	 */
	private void checkPositive(BigDecimal value) {
		if (value == null || BigDecimal.ZERO.compareTo(value) >= 0) {
			throw new StockMarketExchangeException("Found non-positive value: "
					+ value);
		}
	}

	/**
	 * Get the valid Stock record from the Gemfire region
	 * 
	 * @param value
	 *            the value to be checked
	 */
	private Stock getValidStock(String stockSymbol) {
		Stock stock = stockDao.findOne(stockSymbol);
		if (stock == null) {
			throw new StockMarketExchangeException(
					"No Stock found with this Stock Symbol :" + stockSymbol);

		}

		return stock;

	}

	/**
	 * Gets the trade records of the given stock in the last given minutes.
	 * 
	 * @param list
	 *            the list of trade record
	 * @param minutes
	 *            the range of the time to search
	 * @return a {@link TradeRecord}s matching the search criterion
	 */
	private List<Trade> getTradeRecordsByTime(Collection<Trade> list,
			int minutes) {
		List<Trade> result = new ArrayList<Trade>();

		Date currentTime = new Date();
		for (Trade record : list) {
			if (currentTime.getTime() - record.getTimestamp().getTime() <= minutes * 60 * 1000) {
				result.add(record);
			}
		}

		return result;

	}
	/**
	 * Saves the list of Stock in Gemfire Region
	 * 
	 * @param stockList
	 *            List of Stock
	 */
	@Override
	public void saveStockCollection(List<Stock> stockList) {
		stockDao.save(stockList);
		
	}

}
