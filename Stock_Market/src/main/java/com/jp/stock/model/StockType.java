package com.jp.stock.model;

/**
 * Constants for the stock type available in the Simple Stocks application.
 * 
 * 
 * @author chandresh.mishra
 * 
 */
public enum StockType {

	/**
	 * Indicates that a stock is common and the dividend yield is calculated
	 * with last dividend.
	 */
	COMMON,

	/**
	 * Indicates that a stock is preferred and the dividend yield is calculated
	 * with fixed dividend.
	 */
	PREFERRED;

	private String value;

	public String getValue() {
		return value;
	}

}
