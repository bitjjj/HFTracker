package com.oppsis.app.hftracker.ui;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.oppsis.app.hftracker.R;
import com.oppsis.app.hftracker.pojo.FundLocalInfoManager;
import com.oppsis.app.hftracker.pojo.FundObject;
import com.oppsis.app.hftracker.ui.base.BaseSwipeAndActionBarActivity;
import com.oppsis.app.hftracker.ui.fragment.FundDetailHoldingChartFragment;
import com.oppsis.app.hftracker.ui.fragment.FundDetailHoldingChartFragment_;
import com.oppsis.app.hftracker.util.Constants;

@EActivity(R.layout.activity_port_value_chart)
public class PortValueChartActivity extends BaseSwipeAndActionBarActivity{
	@Extra protected FundObject fund;
	
	@AfterViews
	public void initUI(){
		super.initUI(getString(R.string.chart_holdings_value_title),Constants.FUND_MANAGER_ICONS.get(FundLocalInfoManager.getFundProperty(this, fund.getName(),FundObject.PROPERTY_FUND_ID)));
		
		FundDetailHoldingChartFragment fragment = FundDetailHoldingChartFragment_.builder().fund(fund).build();
		
		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		
		if(fragmentManager.findFragmentByTag(Constants.FUND_HOLDINGS_CHART_FRAGMENT_TAG) != null){
			fragmentTransaction.replace(R.id.fund_holding_chart_fragment, fragment);
		}
		else{
			fragmentTransaction.add(R.id.fund_holding_chart_fragment, fragment, Constants.FUND_HOLDINGS_CHART_FRAGMENT_TAG);
		}
		
		fragmentTransaction.commit();
	}
}
