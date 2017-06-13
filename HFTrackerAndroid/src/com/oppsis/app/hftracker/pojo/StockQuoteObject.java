
package com.oppsis.app.hftracker.pojo;


public class StockQuoteObject {

	String symbol;
	double price;
	double change;
	String changePercent;
	double volume;
	long lastUpdate;
	
	public String getChangePercent() {
		return changePercent;
	}

	public void setChangePercent(String changePercent) {
		this.changePercent = changePercent;
	}


	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public double getChange() {
		return change;
	}

	public void setChange(double change) {
		this.change = change;
	}

	public double getVolume() {
		return volume;
	}


	public void setVolume(double volume) {
		this.volume = volume;
	}

	public long getLastUpdated() {
		return lastUpdate;
	}

	public void setLastUpdated(long lastUpdate) {
		this.lastUpdate = lastUpdate;
	}
}