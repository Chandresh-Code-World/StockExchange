/**
 * 
 */
package com.jp.stock.test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.jp.stock.api.SimpleStockFactory;
import com.jp.stock.model.BuyOrSell;
import com.jp.stock.service.StockQuery;
import com.jp.stock.service.TradeQuery;

/**
 * @author chandresh.mishra
 *
 */
public class TradeQueryTest {
	
	//Loads the Context and Inital Gemfire Data
		SimpleStockFactory simpleStockFactory=SimpleStockFactory.getInstance();
		TradeQuery tradeQuery=simpleStockFactory.getTradeService();
		StockQuery stockQuery=simpleStockFactory.getStockService();
		
		@Before
		public void saveTrade()
		{
			stockQuery.recordTrade("JOE", new Date(), new BigInteger("100"), BuyOrSell.BUY, new BigDecimal(100));
			stockQuery.recordTrade("JOE", new Date(), new BigInteger("100"), BuyOrSell.BUY, new BigDecimal(100));
			stockQuery.recordTrade("JOE", new Date(), new BigInteger("100"), BuyOrSell.SELL, new BigDecimal(100));
			stockQuery.recordTrade("JOE", new Date(), new BigInteger("100"), BuyOrSell.SELL, new BigDecimal(100));
		}
		
		@Test
		public void calculateGBCEAllShareIndex()
		{
			BigDecimal result= tradeQuery.calculateGBCEAllShareIndex();
			Assert.assertEquals(new BigDecimal("100"), result);
		}

}
