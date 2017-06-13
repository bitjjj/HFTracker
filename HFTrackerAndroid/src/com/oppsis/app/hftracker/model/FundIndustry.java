package com.oppsis.app.hftracker.model;

public class FundIndustry implements FundChartParcelable{

	private int id;
	private float num;
	private String dateStr,name,fund_name;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public float getNum() {
		return num;
	}
	public void setNum(float num) {
		this.num = num;
	}
	
	public String getDateStr() {
		return dateStr;
	}
	public void setDateStr(String dateStr) {
		this.dateStr = dateStr;
	}
	
	public String getFund_name() {
		return fund_name;
	}
	public void setFund_name(String fund_name) {
		this.fund_name = fund_name;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public void initValue() {
	}
	
	@Override
	public String getComparatorField() {
		return name;
	}
	@Override
	public void setComparatorField(String field) {
		name = field;
	}
	@Override
	public String getIdentifier() {
		return fund_name;
	}
	@Override
	public void setIdentifier(String identifier) {
		this.fund_name = identifier;
		
	}
	
}
