package com.oppsis.app.hftracker.ui;

import java.util.List;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import com.github.mikephil.charting.charts.LineChart;
import com.oppsis.app.hftracker.R;
import com.oppsis.app.hftracker.api.util.DefaultListCallback;
import com.oppsis.app.hftracker.pojo.SPXData;
import com.oppsis.app.hftracker.ui.util.ChartGenerator;

@EActivity(R.layout.activity_spx_eps)
public class SPXEPSActivity extends SPXBaseActivity{
	
	@ViewById(R.id.spx_eps_chart)protected LineChart mChart;
	
	protected int getActionBarTitleRes(){
		return R.string.hf_title_spx_eps;
	}
	
	protected int getSPXInfoRes(){
		return R.string.spx_eps_info;
	}
	
	@Background
	protected void loadData(){
		mAPIFacade.getSPXEPS(new DefaultListCallback<SPXData>(this) {
			@Override
			public void onSuccess(final List<SPXData> result) {
				mMainHandler.post(new Runnable() {
					
					@Override
					public void run() {
						ChartGenerator.createSPXLineChart(SPXEPSActivity.this, mChart, result);
						
					}
				});
				
			}
		});
	}
	
	protected int getSelfNavDrawerItem() {
		return R.id.navdrawer_item_spx_eps;
	}
	
	protected int getShareTitle(){
		return R.string.share_app_spx_eps_label;
	}
}
