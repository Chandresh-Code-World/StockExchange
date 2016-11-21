/**
 * 
 */
package com.jp.stock.integration.gemfire;

import org.springframework.data.repository.CrudRepository;

import com.jp.stock.model.Stock;

/**
 * It performs the CRUD operation on Stock region by extending Spring data
 * CrudRepository
 * 
 * @author chandresh.mishra
 * 
 */
public interface StockDao extends CrudRepository<Stock, String> {

	/**
	 * Saves stock record in gemfire region
	 */
	public Stock save(Stock stock);

	/**
	 * Finds the record with the given stock symbol/Key
	 */
	public Stock findOne(String symbol);

}
