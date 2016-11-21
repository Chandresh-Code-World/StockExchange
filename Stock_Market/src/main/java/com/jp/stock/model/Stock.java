/**
 * 
 */
package com.jp.stock.model;

import java.io.Serializable;
import java.math.BigDecimal;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.gemfire.mapping.Region;

/**
 * Stock class to hold the stock data.
 *
 * @author chandresh.mishra
 *
 */

/** Representing the Stock region in Gemfire */
@Region("Stock")
public class Stock implements Serializable {

	private static final long serialVersionUID = 3209342518270638000L;

	/** The symbol of the stock used as key in gemfire region */
	@Id
	private String symbol;

	/** The last dividend value */
	private BigDecimal lastDividend;

	/** The par value. */
	private BigDecimal parValue;

	/** The price of this stock. */
	private BigDecimal price;

	/** The Type of this stock. */
	private StockType stockType;

	/** The last dividend value */
	private BigDecimal fixedDividend;

	/**
	 * @param symbol
	 * @param lastDividend
	 * @param parValue
	 * @param price
	 * @param stockType
	 * @param fixedDividend
	 */

	/** This constructor will be used for persisting data in Gemfire */
	@PersistenceConstructor
	public Stock(String symbol, BigDecimal lastDividend, BigDecimal parValue,
			BigDecimal price, StockType stockType, BigDecimal fixedDividend) {
		this.symbol = symbol;
		this.lastDividend = lastDividend;
		this.parValue = parValue;
		this.price = price;
		this.stockType = stockType;
		this.fixedDividend = fixedDividend;

	}

	public Stock() {

	}

	/**
	 * @return the symbol
	 */
	public String getSymbol() {
		return symbol;
	}

	/**
	 * @param symbol
	 *            the symbol to set
	 */
	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	/**
	 * @return the lastDividend
	 */
	public BigDecimal getLastDividend() {
		return lastDividend;
	}

	/**
	 * @param lastDividend
	 *            the lastDividend to set
	 */
	public void setLastDividend(BigDecimal lastDividend) {
		this.lastDividend = lastDividend;
	}

	/**
	 * @return the parValue
	 */
	public BigDecimal getParValue() {
		return parValue;
	}

	/**
	 * @param parValue
	 *            the parValue to set
	 */
	public void setParValue(BigDecimal parValue) {
		this.parValue = parValue;
	}

	/**
	 * @return the price
	 */
	public BigDecimal getPrice() {
		return price;
	}

	/**
	 * @param price
	 *            the price to set
	 */
	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	/**
	 * @return the stockType
	 */
	public StockType getStockType() {
		return stockType;
	}

	/**
	 * @param stockType
	 *            the stockType to set
	 */
	public void setStockType(StockType stockType) {
		this.stockType = stockType;
	}

	/**
	 * @return the fixedDividend
	 */
	public BigDecimal getFixedDividend() {
		return fixedDividend;
	}

	/**
	 * @param fixedDividend
	 *            the fixedDividend to set
	 */
	public void setFixedDividend(BigDecimal fixedDividend) {
		this.fixedDividend = fixedDividend;
	}

}
