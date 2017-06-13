package com.oppsis.app.hftracker.ui.fragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import android.os.Bundle;
import android.view.View;

import com.github.mikephil.charting.charts.BarChart;
import com.oppsis.app.hftracker.R;
import com.oppsis.app.hftracker.api.util.DefaultListCallback;
import com.oppsis.app.hftracker.event.ChangeAnalysisDateEvent;
import com.oppsis.app.hftracker.event.ChangeAnalysisManagersEvent;
import com.oppsis.app.hftracker.event.OttoBus;
import com.oppsis.app.hftracker.model.FundIndustry;
import com.oppsis.app.hftracker.ui.util.ChartGenerator;
import com.oppsis.app.hftracker.util.Constants;
import com.oppsis.app.hftracker.util.FundChartComparator;
import com.oppsis.app.hftracker.util.FundFuncUtils;
import com.squareup.otto.Subscribe;
import com.umeng.analytics.MobclickAgent;

@EFragment(R.layout.fragment_analysis_industry)
public class AnalysisIndustryFragment extends AnalysisBaseFragment{

	@ViewById(R.id.analysis_industry_chart)protected BarChart mIndustryChart;
	@ViewById(R.id.container) protected View mContainer;
	
	@Bean
    protected OttoBus mBus;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            mBus.register(this);  
    }
	
	@AfterViews
	protected void initUI(){
		setTouchEvent(mIndustryChart);
		setTouchEvent(mContainer);
	}
	
	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);

		checkIfLoadingData();
	}
	
	
	@Subscribe
	public void onChangeAnalysisDate(ChangeAnalysisDateEvent event) {

		changeAnalysisDate(event.getDate());
		
		checkIfLoadingData();
	}
	
	@Subscribe
	public void onChangeAnalysisManagers(ChangeAnalysisManagersEvent event) {
		changeAnalysisManagers(event.getSelectedManagers());
		
		checkIfLoadingData();
	}
	
	@Background
	protected void loadData(List<String> managerNameList){

			mAPIFacade.getIndustryLists(managerNameList, mDate, new DefaultListCallback<FundIndustry>(getActivity()) {
				
				@SuppressWarnings({ "rawtypes", "unchecked" })
				@Override
				public void onSuccess(List<FundIndustry> result) {
					Map<String, ArrayList> entry = FundFuncUtils.alignFundChartObjects(result, new FundChartComparator(),FundIndustry.class);
					final ArrayList<String> identifierList = entry.get(Constants.IDENTIFIER_LIST);
					final ArrayList<String> sorterList = entry.get(Constants.SORTER_LIST);
					final ArrayList<List<FundIndustry>> industryLists = entry.get(Constants.RESULT_LIST);
					
					mMainHandler.post(new Runnable() {
						
						@Override
						public void run() {
							
							ChartGenerator.createAnlysisIndustryGroupBarChart(getActivity(), mIndustryChart, identifierList,sorterList, industryLists);
							mStatusChangeListener.hideManagerListBar();
						}
					});
				}
			});
		
	}
	
	protected void clearData(){
		ChartGenerator.clearChart(mIndustryChart);
		mStatusChangeListener.hideManagerListBar();
	}

	
	public void onResume() {
	    super.onResume();
	    MobclickAgent.onPageStart("Analysis_Industry_Screen");
	}
	public void onPause() {
	    super.onPause();
	    MobclickAgent.onPageEnd("Analysis_Industry_Screen"); 
	}
	
	@Override
    public void onDestroy() {
            super.onDestroy();
            mBus.unregister(this);
    }
}
