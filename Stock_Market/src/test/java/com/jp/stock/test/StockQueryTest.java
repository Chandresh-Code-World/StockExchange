/**
 * 
 */
package com.jp.stock.test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import org.junit.Assert;
import org.junit.Test;

import com.jp.stock.api.SimpleStockFactory;
import com.jp.stock.api.SpringService;
import com.jp.stock.integration.gemfire.TradeDao;
import com.jp.stock.model.BuyOrSell;
import com.jp.stock.service.StockQuery;



/**
 * @author chandresh.mishra
 *
 */
public class StockQueryTest {
	
	//Loads the Context and Inital Gemfire Data
	SimpleStockFactory simpleStockFactory=SimpleStockFactory.getInstance();
	StockQuery stockQuery=simpleStockFactory.getStockService();
	
	
	@Test
	public void getDividendYield()
	{
		
		BigDecimal result = stockQuery.getDividendYield("POP", new BigDecimal(130));
		Assert.assertEquals(new BigDecimal("0.062"), result);
		
	}
	
	@Test
	public void getPERatio()
	{
		
		BigDecimal result = stockQuery.getPERatio("POP", new BigDecimal(130));
		Assert.assertEquals(new BigDecimal("2096.774"), result);
		
	}
	
	@Test
	public void recordTrade()
	{
		TradeDao tradeDao=SpringService.getInstance().getBean(TradeDao.class);
		//Initial it is empty
		Assert.assertEquals(0,tradeDao.findAllTradeForStock("TEA").size());
		stockQuery.recordTrade("TEA", new Date(), new BigInteger("100"), BuyOrSell.BUY, new BigDecimal(100));
		Assert.assertEquals(1,tradeDao.findAllTradeForStock("TEA").size());
		
		
	}
	

	@Test
	public void getVolumeWeightedStockPrice()
	{
		stockQuery.recordTrade("JOE", new Date(), new BigInteger("100"), BuyOrSell.BUY, new BigDecimal(100));
		BigDecimal result =stockQuery.getVolumeWeightedStockPrice("JOE");
		Assert.assertEquals(new BigDecimal("100"), result);
	}

}
