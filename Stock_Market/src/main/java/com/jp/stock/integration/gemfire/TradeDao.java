/**
 * 
 */
package com.jp.stock.integration.gemfire;

import java.util.Collection;

import org.springframework.data.gemfire.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.jp.stock.model.Trade;

/**
 * It performs the CRUD operation on Trade region by extending Spring data
 * CrudRepository
 * 
 * @author chandresh.mishra
 * 
 */
public interface TradeDao extends CrudRepository<Trade, Integer> {

	/**
	 * Saves Trade record in gemfire region
	 */
	public Trade save(Trade trade);

	/**
	 * Get the all the trade record for a given stock symbol from gemfire Trade
	 * region
	 */

	@Query("SELECT * FROM /Trade T WHERE T.stock.symbol =$1 ")
	public Collection<Trade> findAllTradeForStock(String stock);

	/**
	 * Get all the trade from the Trade region
	 */
	public Collection<Trade> findAll();

}
