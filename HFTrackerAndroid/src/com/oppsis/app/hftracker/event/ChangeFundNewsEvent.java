package com.oppsis.app.hftracker.event;

import com.oppsis.app.hftracker.model.FundNews;

public class ChangeFundNewsEvent {
	private FundNews mFundNews;

	public ChangeFundNewsEvent(FundNews news) {
		this.mFundNews = news;
	}

	public FundNews getFundNews() {
		return mFundNews;
	}
}
