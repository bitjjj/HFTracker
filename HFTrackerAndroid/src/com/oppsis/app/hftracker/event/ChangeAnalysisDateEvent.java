package com.oppsis.app.hftracker.event;


public class ChangeAnalysisDateEvent {

	private String mDate;
	
	public ChangeAnalysisDateEvent(String date){
		this.mDate = date;
	}
	
	public String getDate() {
		return mDate;
	}
	
}
