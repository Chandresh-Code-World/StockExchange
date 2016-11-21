/**
 * 
 */
package com.jp.stock.integration.gemfire;

import org.springframework.data.repository.CrudRepository;

import com.jp.stock.model.UniqueSequence;

/**
 * It performs the CRUD operation on UniqueSequence region by extending Spring
 * data CrudRepository
 * 
 * The main purpose of this region is to generate the unique key value for each
 * Trade
 * 
 * @author chandresh.mishra
 * 
 */
public interface UniqueSequenceDao extends
		CrudRepository<UniqueSequence, Integer> {

	/**
	 * Saves/update Unique Sequence record in gemfire region
	 */
	public UniqueSequence save(UniqueSequence sequence);

	/**
	 * Get Unique Sequence record for a given key from gemfire region
	 */
	public UniqueSequence findOne(Integer id);

}
