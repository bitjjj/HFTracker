package com.oppsis.app.hftracker.model;

public class FundDateList implements FundChartParcelable{
	
	public final static FundDateList EMPTY = new FundDateList(); 
	
	private int id;
	private String dateStr,portfolioValue,qtr,fund_name;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getDateStr() {
		return dateStr;
	}
	public void setDateStr(String dateStr) {
		this.dateStr = dateStr;
	}
	public String getPortfolioValue() {
		return portfolioValue;
	}
	public void setPortfolioValue(String portfolioValue) {
		this.portfolioValue = portfolioValue;
	}
	public String getQtr() {
		return qtr;
	}
	public void setQtr(String qtr) {
		this.qtr = qtr;
	}
	public String getFund_name() {
		return fund_name;
	}
	public void setFund_name(String fund_name) {
		this.fund_name = fund_name;
	}
	
	
	@Override
	public String getComparatorField() {
		return dateStr;
	}
	@Override
	public void setComparatorField(String field) {
		dateStr = field;
	}
	@Override
	public String getIdentifier() {
		return fund_name;
	}
	@Override
	public void setIdentifier(String identifier) {
		this.fund_name = identifier;
		
	}

	@Override
	public void initValue() {
		setPortfolioValue("0.0");
	}
}
