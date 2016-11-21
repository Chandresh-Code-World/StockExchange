/**
 * 
 */
package com.jp.stock.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.jp.stock.integration.gemfire.StockDao;
import com.jp.stock.model.Stock;
import com.jp.stock.model.StockType;
import com.jp.stock.service.StockQuery;

import javax.annotation.PostConstruct;

/**
 * This class loads the Stock data from the excel file received from exchange.
 * It runs on the context start up and continue to run every day at 7 o clock
 * periodically to load the EOD files into the system.
 * 
 * @author chandresh.mishra
 * 
 */
@Component
@PropertySource("com/jp/stock/exchangeData/StockExchange.properties")
public class LoadExchangeData {

	private Logger logger = Logger.getLogger(LoadExchangeData.class);

	@Autowired
	private StockQuery stockQuery;

	// Exchange file location from the property file.
	@Value("${stock.dataFile}")
	private String dataFile;

	// bean used to for getting the values from the property file
	@Bean
	public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
		return new PropertySourcesPlaceholderConfigurer();
	}

	// Runs once on loading of context
	@PostConstruct
	public void onStartup() {

		logger.info("Loading Stock data into System");
		stockQuery.saveStockCollection(readStockDataFromExcelFile(dataFile));
	}

	// Runs periodically at specific time of day decided by cron expression
	@Scheduled(cron = "${stock.cron}")
	public void onSchedule() {

		logger.info("Loading Stock data into System periodically");
		stockQuery.saveStockCollection(readStockDataFromExcelFile(dataFile));
	}

	/**
	 * Read the EOD excel file containing the Stock data It uses Apache POI to
	 * read the Excel file
	 * 
	 * @param excelFilePath
	 *            location of EOD file
	 * @return list of stock data
	 */

	public List<Stock> readStockDataFromExcelFile(String excelFilePath) {

		FileInputStream inputStream = null;
		List<Stock> listStock = null;
		try {
			logger.info("Loading Exchange File");
			listStock = new ArrayList<Stock>();

			inputStream = new FileInputStream(new File(excelFilePath));

			
			Workbook workbook = new XSSFWorkbook(inputStream);
			Sheet firstSheet = workbook.getSheetAt(0);
			Iterator<Row> iterator = firstSheet.iterator();
			// Iterating over each row
			while (iterator.hasNext()) {
				Row nextRow = iterator.next();
				Iterator<Cell> cellIterator = nextRow.cellIterator();

				// Leave the first row containing labels
				if (nextRow.getRowNum() == 0) {
					continue;
				}
				Stock stock = new Stock();
				// Iterating over cell and set the value in stock object
				while (cellIterator.hasNext()) {
					Cell nextCell = cellIterator.next();
					int columnIndex = nextCell.getColumnIndex();

					switch (columnIndex) {

					case 0:
						stock.setSymbol((String) getCellValue(nextCell));
						break;

					case 1:
						stock.setStockType(StockType.valueOf(getCellValue(
								nextCell).toString()));
						break;
					case 2:
						stock.setLastDividend(new BigDecimal(
								(Double) getCellValue(nextCell)));
						break;

					case 3:
						stock.setFixedDividend(new BigDecimal(
								(Double) getCellValue(nextCell)));
						break;

					case 4:
						stock.setParValue(new BigDecimal(
								(Double) getCellValue(nextCell)));
						break;

					}

				}
				listStock.add(stock);
				stock=null;
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				inputStream.close();
			} catch (IOException e) {
				logger.info("Exception while reading the exchange file");
				e.printStackTrace();
			}
		}
		return listStock;

	}

	/**
	 * 
	 * @param cell
	 *            cell of the each row in excel file
	 * @return object value depending on the type of cell value
	 */
	private Object getCellValue(Cell cell) {
		switch (cell.getCellType()) {
		case Cell.CELL_TYPE_STRING:
			return cell.getStringCellValue();

		case Cell.CELL_TYPE_BOOLEAN:
			return cell.getBooleanCellValue();

		case Cell.CELL_TYPE_NUMERIC:
			return cell.getNumericCellValue();
		}

		return null;
	}

}
