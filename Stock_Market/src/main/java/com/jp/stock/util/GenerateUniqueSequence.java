/**
 * 
 */
package com.jp.stock.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jp.stock.integration.gemfire.UniqueSequenceDao;
import com.jp.stock.model.UniqueSequence;

/**
 * Utility for getting the unique sequence for using as a key value
 * 
 * @author chandresh.mishra
 * 
 */
@Component
public class GenerateUniqueSequence {

	/**
	 * Autowired uniqueSequence database service
	 */
	@Autowired
	UniqueSequenceDao uniqueSequenceDao;

	/**
	 * @return int The unique sequence starting from 1
	 */
	public int getUniqueSequence() {

		UniqueSequence currentSequence = uniqueSequenceDao.findOne(1);
		if (currentSequence == null) {
			uniqueSequenceDao.save(new UniqueSequence(1, 1));
			return 1;
		}
		int value = currentSequence.getValue();
		currentSequence.setValue(value + 1);

		uniqueSequenceDao.save(currentSequence);
		return value + 1;
	}

}
