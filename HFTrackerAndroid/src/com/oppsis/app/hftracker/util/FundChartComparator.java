package com.oppsis.app.hftracker.util;

import java.util.Comparator;

import com.oppsis.app.hftracker.model.FundChartParcelable;


public class FundChartComparator implements Comparator<FundChartParcelable>{

	@Override
	public int compare(FundChartParcelable lhs, FundChartParcelable rhs) {
		return lhs.getComparatorField().compareTo(rhs.getComparatorField());
	}

}
