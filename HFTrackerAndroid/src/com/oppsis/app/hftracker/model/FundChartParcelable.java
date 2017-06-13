package com.oppsis.app.hftracker.model;

public interface FundChartParcelable {

	public String getComparatorField();
	public void setComparatorField(String field);
	
	public String getIdentifier();
	public void setIdentifier(String identifier);
	
	public void initValue();
	
}
