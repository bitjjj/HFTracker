package com.oppsis.app.hftracker.event;

import com.oppsis.app.hftracker.ui.widget.FilterPanelView.FundFilter;

public class ChangeFundFilterEvent {
	
	private FundFilter mFundFilter;
	
	public ChangeFundFilterEvent(FundFilter filter){
		this.mFundFilter = filter;
	}
	
	public FundFilter getFundFilter() {
		return mFundFilter;
	}

	
}
