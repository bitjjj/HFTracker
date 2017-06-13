package com.oppsis.app.hftracker.ui.fragment;

import java.util.List;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.ViewById;

import com.github.mikephil.charting.charts.LineChart;
import com.oppsis.app.hftracker.R;
import com.oppsis.app.hftracker.api.util.DefaultListCallback;
import com.oppsis.app.hftracker.model.FundDateList;
import com.oppsis.app.hftracker.pojo.FundObject;
import com.oppsis.app.hftracker.ui.base.BaseFragment;
import com.oppsis.app.hftracker.ui.util.ChartGenerator;

@EFragment(R.layout.fragment_fund_detial_holding_chart)
public class FundDetailHoldingChartFragment extends BaseFragment{

	@ViewById(R.id.fund_holding_chart)
	protected LineChart mChart;
	@FragmentArg("fund")
	protected FundObject fund;
	
	@AfterViews
	protected void initUI(){
		loadData();
	}
	
	@Background
	protected void loadData(){
		mAPIFacade.getDateList(fund, new DefaultListCallback<FundDateList>(getActivity()){
			@Override
			public void onSuccess(final List<FundDateList> result) {
				mMainHandler.post(new Runnable() {
					@Override
					public void run() {
						ChartGenerator.createHoldingsValueLineChart(getActivity(), mChart, result);	
					}
				});
				
			}
		});
	}
	

}
