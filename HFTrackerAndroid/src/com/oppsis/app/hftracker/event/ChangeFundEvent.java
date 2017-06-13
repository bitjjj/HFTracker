package com.oppsis.app.hftracker.event;

import com.oppsis.app.hftracker.pojo.FundObject;

public class ChangeFundEvent {
	
	private FundObject mFundObject;
	
	public ChangeFundEvent(FundObject fund){
		this.mFundObject = fund;
	}
	
	public FundObject getFund() {
		return mFundObject;
	}

	
}
