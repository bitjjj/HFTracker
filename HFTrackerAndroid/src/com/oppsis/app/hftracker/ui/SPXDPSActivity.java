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

@EActivity(R.layout.activity_spx_dps)
public class SPXDPSActivity extends SPXBaseActivity {
	
	@ViewById(R.id.spx_dps_chart)protected LineChart mChart;
	
	protected int getActionBarTitleRes(){
		return R.string.hf_title_spx_dps;
	}
	
	protected int getSPXInfoRes(){
		return R.string.spx_dps_info;
	}
	
	@Background
	protected void loadData(){
		mAPIFacade.getSPXDPS(new DefaultListCallback<SPXData>(this) {
			@Override
			public void onSuccess(final List<SPXData> result) {
				mMainHandler.post(new Runnable() {
					
					@Override
					public void run() {
						ChartGenerator.createSPXLineChart(SPXDPSActivity.this, mChart, result);
						
					}
				});
				
			}
		});
	}
	
	
	protected int getSelfNavDrawerItem() {
		return R.id.navdrawer_item_spx_dps;
	}
	
	protected int getShareTitle(){
		return R.string.share_app_spx_dps_label;
	}
}
