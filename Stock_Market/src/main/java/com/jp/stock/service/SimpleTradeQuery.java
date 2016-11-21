package com.jp.stock.service;

import java.math.BigDecimal;
import java.util.Collection;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jp.stock.exception.StockMarketExchangeException;
import com.jp.stock.integration.gemfire.TradeDao;
import com.jp.stock.model.Trade;

/**
 * It provides the Implementation for the TradeQuery Interface
 */

/**
 * Register it as a bean in Spring context
 */

@Component
public class SimpleTradeQuery implements TradeQuery {

	private static final int PRECISION_SCALE = 7;

	/**
	 * Autowired Trade database service
	 */
	@Autowired
	TradeDao tradeDao;

	private Logger logger = Logger.getLogger(SimpleTradeQuery.class);

	/**
	 * Calculates the GBCE all share index value based on the prices of all the
	 * stocks in the market service.
	 * 
	 * @return the GBCE all share index value.
	 */
	public BigDecimal calculateGBCEAllShareIndex() {

		logger.info("Calculating GBCE All Share Index:");

		// Fetching all the trade from trade region in Gemfire
		Collection<Trade> tradeCollection = tradeDao.findAll();

		if (tradeCollection.isEmpty())
			throw new StockMarketExchangeException("No Trade Record Found");

		BigDecimal accumulate = BigDecimal.ONE;

		for (Trade tradeRecord : tradeCollection) {

			BigDecimal price = tradeRecord.getPrice();
			checkPositive(price);
			accumulate = accumulate.multiply(price);
			price = null;
		}

		int n = tradeCollection.size();
		BigDecimal x = accumulate
				.divide(accumulate, BigDecimal.ROUND_HALF_EVEN);
		BigDecimal temp = BigDecimal.ZERO;
		BigDecimal e = new BigDecimal("0.1");

		do {
			temp = x;
			x = x.add(accumulate.subtract(x.pow(n)).divide(
					new BigDecimal(n).multiply(x.pow(n - 1)), PRECISION_SCALE,
					BigDecimal.ROUND_HALF_EVEN));
		} while (x.subtract(temp).abs().compareTo(e) > 0);

		return x.setScale(0, BigDecimal.ROUND_HALF_EVEN);
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
}
