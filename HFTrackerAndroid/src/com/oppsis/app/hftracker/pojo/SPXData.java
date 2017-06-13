package com.oppsis.app.hftracker.pojo;

public class SPXData {

	private String dateStr;
	private long timeMark;
	private float value;
	
	public String getDateStr() {
		return dateStr;
	}
	public void setDateStr(String dateStr) {
		this.dateStr = dateStr;
	}
	public long getTimeMark() {
		return timeMark;
	}
	public void setTimeMark(long timeMark) {
		this.timeMark = timeMark;
	}
	public float getValue() {
		return value;
	}
	public void setValue(float value) {
		this.value = value;
	}
	
	public String getYear(){
		return this.dateStr.split(",")[1].trim();
	}
}
