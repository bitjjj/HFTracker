package com.oppsis.app.hftracker.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.oppsis.app.hftracker.pojo.StockQuoteObject;
import com.oppsis.app.util.FuncUtils;

public class YahooStockPriceUtils {

	public static Map<String, StockQuoteObject> getStockPrice(List<String> tickers) {

		BufferedReader reader = null;
		Map<String, StockQuoteObject> stocks = new HashMap<String, StockQuoteObject>();
		
		if(tickers.size() <= 0)return stocks;
		
		try {
			URL url = new URL(String.format(Constants.STOCK_YAHOO_API, FuncUtils.join(tickers, ",")));
			URLConnection urlConnection = url.openConnection();
			reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
			
			String inputLine;
			while ((inputLine = reader.readLine()) != null) {

				String[] yahooStockInfo = inputLine.split(",");
				StockQuoteObject stockInfo = new StockQuoteObject();
				stockInfo.setSymbol(yahooStockInfo[0].replaceAll("\"", ""));
				stockInfo.setPrice(Double.valueOf(yahooStockInfo[1]));
				stockInfo.setChange(Double.valueOf(yahooStockInfo[2]));
				stockInfo.setChangePercent(yahooStockInfo[3].replaceAll("\"", ""));
				stockInfo.setVolume(Double.valueOf(yahooStockInfo[4]));
				stockInfo.setLastUpdated(System.currentTimeMillis());
				stocks.put(stockInfo.getSymbol(), stockInfo);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (reader != null)
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
		
		return stocks;
	}
	
	public static void main(String[] args) {
		
	}

}
