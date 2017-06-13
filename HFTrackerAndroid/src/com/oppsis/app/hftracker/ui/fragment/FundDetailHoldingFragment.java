package com.oppsis.app.hftracker.ui.fragment;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.oppsis.app.hftracker.R;
import com.oppsis.app.hftracker.event.OttoBus;
import com.oppsis.app.hftracker.pojo.FundObject;
import com.oppsis.app.hftracker.pojo.FundObjectFetcher;
import com.oppsis.app.hftracker.util.Constants;
import com.umeng.analytics.MobclickAgent;

@EFragment(R.layout.fragment_fund_detail_holding)
public class FundDetailHoldingFragment extends Fragment{

	@Bean
    protected OttoBus mBus;
	
	private FundObject mFund;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            mBus.register(this);  
    }
	
	
	@AfterViews
	protected void initUI(){
		FundDetailHoldingChartFragment chartFragment = 
				FundDetailHoldingChartFragment_.builder().fund(mFund) .build();
		
		FundDetailHoldingListFragment listFragment = 
				FundDetailHoldingListFragment_.builder().fund(mFund) .build();
		
		FragmentManager fragmentManager = getChildFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		
		if(fragmentManager.findFragmentByTag(Constants.FUND_HOLDINGS_CHART_FRAGMENT_TAG) != null){
			fragmentTransaction.replace(R.id.fund_holding_chart_fragment, chartFragment);
		}
		else{
			fragmentTransaction.add(R.id.fund_holding_chart_fragment, chartFragment, Constants.FUND_HOLDINGS_CHART_FRAGMENT_TAG);
		}
		
		if(fragmentManager.findFragmentByTag(Constants.FUND_HOLDINGS_LIST_FRAGMENT_TAG) != null){
			fragmentTransaction.replace(R.id.fund_holding_list_fragment, listFragment);
		}
		else{
			fragmentTransaction.add(R.id.fund_holding_list_fragment, listFragment, Constants.FUND_HOLDINGS_LIST_FRAGMENT_TAG);
		}
		
		fragmentTransaction.commit();

	}
	

	@Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
       
        mFund = ((FundObjectFetcher) activity).fetchFundObject();
       
    }

	public void onResume() {
	    super.onResume();
	    MobclickAgent.onPageStart("Fund_Detail_Holding_Screen"); 
	}
	public void onPause() {
	    super.onPause();
	    MobclickAgent.onPageEnd("Fund_Detail_Holding_Screen"); 
	}
	
    @Override
    public void onDestroy() {
            super.onDestroy();
            mBus.unregister(this);
    }
	
}
