package com.oppsis.app.hftracker.ui;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;

import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.oppsis.app.hftracker.R;
import com.oppsis.app.hftracker.pojo.FundLocalInfoManager;
import com.oppsis.app.hftracker.pojo.FundObject;
import com.oppsis.app.hftracker.pojo.FundObjectFetcher;
import com.oppsis.app.hftracker.ui.base.BaseSwipeAndActionBarActivity;
import com.oppsis.app.hftracker.ui.fragment.FilterDialogFragment_;
import com.oppsis.app.hftracker.ui.fragment.FundDetailIndustryFragment;
import com.oppsis.app.hftracker.ui.fragment.FundDetailIndustryFragment_;
import com.oppsis.app.hftracker.util.Constants;

@EActivity(R.layout.activity_industry_chart)
public class IndustryChartActivity extends BaseSwipeAndActionBarActivity implements FundObjectFetcher{

	@Extra protected FundObject fund;
	
	@AfterViews
	public void initUI(){
		super.initUI(getString(R.string.chart_industry_title),Constants.FUND_MANAGER_ICONS.get(FundLocalInfoManager.getFundProperty(this, fund.getName(),FundObject.PROPERTY_FUND_ID)));
		
		FundDetailIndustryFragment fragment = 
				FundDetailIndustryFragment_.builder().build();
		
		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		
		if(fragmentManager.findFragmentByTag(Constants.FUND_HOLDINGS_INDUSTRY_FRAGMENT_TAG) != null){
			fragmentTransaction.replace(R.id.fund_holding_industry_fragment, fragment);
		}
		else{
			fragmentTransaction.add(R.id.fund_holding_industry_fragment, fragment, Constants.FUND_HOLDINGS_INDUSTRY_FRAGMENT_TAG);
		}
		
		
		fragmentTransaction.commit();
	}
	
	@Click(R.id.filter_action_btn)
	protected void showFilterDialog(){
		if(fund != null){
			//DialogFragment newFragment = OldFilterDialogFragment.newInstance(mFund,Constants.ACTIONBAR_LIST_HOLDINGS);
			DialogFragment newFragment = FilterDialogFragment_.builder().tabIndex(Constants.ACTIONBAR_LIST_INDUSTRY).fund(fund).build();
		    newFragment.show(getSupportFragmentManager(), "filter_dialog");
		}
	}

	@Override
	public FundObject fetchFundObject() {
		return fund;
	}
}
