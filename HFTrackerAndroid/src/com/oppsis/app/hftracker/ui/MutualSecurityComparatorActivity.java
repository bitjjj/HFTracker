package com.oppsis.app.hftracker.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import com.github.mikephil.charting.charts.LineChart;
import com.oppsis.app.hftracker.R;
import com.oppsis.app.hftracker.api.util.DefaultListCallback;
import com.oppsis.app.hftracker.model.FundHolding;
import com.oppsis.app.hftracker.ui.base.BaseSwipeAndActionBarActivity;
import com.oppsis.app.hftracker.ui.util.ChartGenerator;
import com.oppsis.app.hftracker.util.Constants;
import com.oppsis.app.hftracker.util.FundChartComparator;
import com.oppsis.app.hftracker.util.FundFuncUtils;

@EActivity(R.layout.activity_mutual_security_comparator)
public class MutualSecurityComparatorActivity extends BaseSwipeAndActionBarActivity{

	@ViewById(R.id.mutual_security_comparator_port_chart)protected LineChart mPortLineChart;
	@ViewById(R.id.mutual_security_comparator_shares_chart)protected LineChart mSharesLineChart;
	@ViewById(R.id.loading_view_container) protected FrameLayout mLoadingView;
	
	@Extra protected String[] names;
	@Extra protected FundHolding holding;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
    }
	
	@AfterViews
	public void initUI(){
		super.initUI(holding.getSecurity());

		loadData();
	}
	
	@Background
	protected void loadData(){
		mAPIFacade.getHoldingComparatorList(holding, names, new DefaultListCallback<FundHolding>(this) {
			
			@SuppressWarnings({ "rawtypes", "unchecked" })
			@Override
			public void onSuccess(List<FundHolding> result) {
				
				Map<String, ArrayList> entry = FundFuncUtils.alignFundChartObjects(result, new FundChartComparator(),FundHolding.class);
				final ArrayList<String> dateList = entry.get(Constants.SORTER_LIST);
				final ArrayList<List<FundHolding>> holdingLists = entry.get(Constants.RESULT_LIST);
				
				mMainHandler.post(new Runnable() {
					@Override
					public void run() {
						mLoadingView.setVisibility(View.GONE);
						mPortLineChart.setVisibility(View.VISIBLE);
						mSharesLineChart.setVisibility(View.VISIBLE);
						
						ChartGenerator.createHoldingComparatorPortLineChart(MutualSecurityComparatorActivity.this,mPortLineChart,dateList,holdingLists);
						ChartGenerator.createHoldingComparatorSharesLineChart(MutualSecurityComparatorActivity.this,mSharesLineChart,dateList,holdingLists);
					}
				});
			}
		});
	}
	
	
}
