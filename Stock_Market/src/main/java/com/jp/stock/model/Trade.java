/**
 * 
 */
package com.jp.stock.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.gemfire.mapping.Region;

/**
 * 
 * A model class of trade record containing the trade information, like stock ,
 * time stamp, quantity of shares, buy or sell indicator and trading price.
 * 
 * It is also used to hold data in Gemfire trade region
 * @author chandresh.mishra
 */

/** Representing the Stock region in Gemfire */
@Region("Trade")
public class Trade implements Serializable {

	private static final long serialVersionUID = 3209342518270638001L;

	/** The trade identifier used as key in trade region */
	@Id
	private int tradeIdentifier;

	/** The stock */
	private Stock stock;

	/** The time stamp of this trade. */
	private Date timestamp;

	/** The quantify of this trade. */
	private BigInteger quantity;

	/** A buy/sell trade indicator. */
	private BuyOrSell indicator;

	/** The trade price. */
	private BigDecimal price;

	/**
	 * Constructor.
	 * 
	 * @param tradeIdentifier
	 * @param stock
	 * @param timestamp
	 * @param quantity
	 * @param indicator
	 * @param price
	 *            /** This constructor will be used for persisting data in
	 *            Gemfire
	 */

	@PersistenceConstructor
	public Trade(int tradeIdentifier, Stock stock, Date timestamp,
			BigInteger quantity, BuyOrSell indicator, BigDecimal price) {

		this.tradeIdentifier = tradeIdentifier;
		this.stock = stock;
		this.timestamp = timestamp;
		this.quantity = quantity;
		this.indicator = indicator;
		this.price = price;
	}

	/**
	 * @return the tradeIdentifier
	 */
	public int getTradeIdentifier() {
		return tradeIdentifier;
	}

	/**
	 * @param tradeIdentifier
	 *            the tradeIdentifier to set
	 */
	public void setTradeIdentifier(int tradeIdentifier) {
		this.tradeIdentifier = tradeIdentifier;
	}

	/**
	 * @return the stock
	 */
	public Stock getStock() {
		return stock;
	}

	/**
	 * @param stock
	 *            the stock to set
	 */
	public void setStock(Stock stock) {
		this.stock = stock;
	}

	/**
	 * @return the timestamp
	 */
	public Date getTimestamp() {
		return timestamp;
	}

	/**
	 * @param timestamp
	 *            the timestamp to set
	 */
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	/**
	 * @return the quantity
	 */
	public BigInteger getQuantity() {
		return quantity;
	}

	/**
	 * @param quantity
	 *            the quantity to set
	 */
	public void setQuantity(BigInteger quantity) {
		this.quantity = quantity;
	}

	/**
	 * @return the indicator
	 */
	public BuyOrSell getIndicator() {
		return indicator;
	}

	/**
	 * @param indicator
	 *            the indicator to set
	 */
	public void setIndicator(BuyOrSell indicator) {
		this.indicator = indicator;
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

}
