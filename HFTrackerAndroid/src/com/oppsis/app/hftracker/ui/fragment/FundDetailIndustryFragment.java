package com.oppsis.app.hftracker.ui.fragment;

import java.util.List;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.github.mikephil.charting.charts.PieChart;
import com.oppsis.app.hftracker.R;
import com.oppsis.app.hftracker.api.util.DefaultListCallback;
import com.oppsis.app.hftracker.event.ChangeFundFilterEvent;
import com.oppsis.app.hftracker.event.OttoBus;
import com.oppsis.app.hftracker.model.FundIndustry;
import com.oppsis.app.hftracker.pojo.FundObject;
import com.oppsis.app.hftracker.pojo.FundObjectFetcher;
import com.oppsis.app.hftracker.ui.base.BaseFragment;
import com.oppsis.app.hftracker.ui.util.ChartGenerator;
import com.oppsis.app.hftracker.ui.widget.FilterPanelView.FundFilter;
import com.squareup.otto.Subscribe;
import com.umeng.analytics.MobclickAgent;

@EFragment(R.layout.fragment_fund_detail_industry)
public class FundDetailIndustryFragment extends BaseFragment{
	
	@ViewById(R.id.fund_industry_chart) protected PieChart mChart;
	@ViewById(R.id.loading_view) protected View mLoadingView;
	@Bean protected OttoBus mBus;
	
	private FundObject mFund;
	private boolean mIsFirstLoaded = false,mIsVisibleToUser = false;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        mBus.register(this);
    }

	
	@AfterViews
	protected void initUI(){
		if(getResources().getBoolean(R.bool.init_industry_chart)){
			beforeLoadingDate(null);
		}
	}
	
	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		
		mIsVisibleToUser = isVisibleToUser;
		if (!isVisibleToUser || mFund == null) {
			return;
		}
		
		if(!mIsFirstLoaded){
			beforeLoadingDate(null);
			mIsFirstLoaded = true;
		}

	}
	
	@Subscribe
    public void onChangeFundFilter(ChangeFundFilterEvent event) {

		if(event.getFundFilter().getSort() == null && //just receiving filter from industry panel
        		event.getFundFilter().getDirection() == null){
			beforeLoadingDate(event.getFundFilter());
		}
    }
	
	private void beforeLoadingDate(FundFilter filter){
		mChart.setVisibility(View.INVISIBLE);
		mLoadingView.setVisibility(View.VISIBLE);
		loadData(filter);
	}
	
	@Background
	protected void loadData(FundFilter filter){
		
		mAPIFacade.getIndustryList(mFund, filter,new DefaultListCallback<FundIndustry>(getActivity()){
			@Override
			public void onSuccess(final List<FundIndustry> result) {
				mMainHandler.post(new Runnable() {
					
					@Override
					public void run() {
						mChart.setVisibility(View.VISIBLE);
						mLoadingView.setVisibility(View.GONE);
						ChartGenerator.createIndustryPieChart(getActivity(), mChart, result);
					}
				});
			}
		});
	}
	
	
	@Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
       
        if(activity instanceof FundObjectFetcher){
        	mFund = ((FundObjectFetcher) activity).fetchFundObject();
        }
       
    }
	
	public void onResume() {
	    super.onResume();
	    MobclickAgent.onPageStart("Fund_Detail_Industry_Screen"); 
	}
	public void onPause() {
	    super.onPause();
	    MobclickAgent.onPageEnd("Fund_Detail_Industry_Screen"); 
	}
	
    @Override
    public void onDestroy() {
            super.onDestroy();
            mBus.unregister(this);
    }
}
