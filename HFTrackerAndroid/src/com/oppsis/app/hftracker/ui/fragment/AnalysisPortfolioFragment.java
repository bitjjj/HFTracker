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
import android.util.Log;
import android.view.View;

import com.github.mikephil.charting.charts.LineChart;
import com.oppsis.app.hftracker.R;
import com.oppsis.app.hftracker.api.util.DefaultListCallback;
import com.oppsis.app.hftracker.event.ChangeAnalysisManagersEvent;
import com.oppsis.app.hftracker.event.OttoBus;
import com.oppsis.app.hftracker.model.FundDateList;
import com.oppsis.app.hftracker.ui.util.ChartGenerator;
import com.oppsis.app.hftracker.util.Constants;
import com.oppsis.app.hftracker.util.FundChartComparator;
import com.oppsis.app.hftracker.util.FundFuncUtils;
import com.squareup.otto.Subscribe;
import com.umeng.analytics.MobclickAgent;

@EFragment(R.layout.fragment_analysis_portfolio)
public class AnalysisPortfolioFragment extends AnalysisBaseFragment{

	@ViewById(R.id.analysis_portfolio_chart)protected LineChart mPortChart;
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
		
		setTouchEvent(mPortChart);
		setTouchEvent(mContainer);
	}
	
	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);

		checkIfLoadingData();
	}
	
	@Subscribe
	public void onChangeAnalysisManagers(ChangeAnalysisManagersEvent event) {

		changeAnalysisManagers(event.getSelectedManagers());
		
		checkIfLoadingData();
		
	}
	
	
	@Background
	protected void loadData(List<String> managerNameList){

			mAPIFacade.getDateLists(managerNameList, new DefaultListCallback<FundDateList>(getActivity()) {
				
				@SuppressWarnings({ "rawtypes", "unchecked" })
				@Override
				public void onSuccess(List<FundDateList> result) {
					Log.d(this.toString(), result.toString());
					
					Map<String, ArrayList> entry = FundFuncUtils.alignFundChartObjects(result, new FundChartComparator(),FundDateList.class);
					final ArrayList<String> dateList = entry.get(Constants.SORTER_LIST);
					final ArrayList<List<FundDateList>> holdingLists = entry.get(Constants.RESULT_LIST);
					
					mMainHandler.post(new Runnable() {
						
						@Override
						public void run() {
							ChartGenerator.createAnlysisPortLineChart(getActivity(), mPortChart, dateList, holdingLists);
							mStatusChangeListener.hideManagerListBar();
						}
					});
				}
			});
		
	}
	
	protected void clearData(){
		ChartGenerator.clearChart(mPortChart);
		mStatusChangeListener.hideManagerListBar();
	}
	
	protected boolean isNeedDateEvent(){
		return false;
	}

	public void onResume() {
	    super.onResume();
	    MobclickAgent.onPageStart("Analysis_Portfolio_Screen");
	}
	public void onPause() {
	    super.onPause();
	    MobclickAgent.onPageEnd("Analysis_Portfolio_Screen"); 
	}
	
	@Override
    public void onDestroy() {
            super.onDestroy();
            mBus.unregister(this);
    }
}
