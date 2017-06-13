package com.oppsis.app.hftracker.ui;

import java.util.List;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.oppsis.app.hftracker.R;
import com.oppsis.app.hftracker.adapter.MutualSecurityListAdapter;
import com.oppsis.app.hftracker.api.APIFacade;
import com.oppsis.app.hftracker.api.util.DefaultListCallback;
import com.oppsis.app.hftracker.model.FundHolding;
import com.oppsis.app.hftracker.pojo.FundLocalInfoManager;
import com.oppsis.app.hftracker.pojo.FundObject;
import com.oppsis.app.hftracker.ui.base.BaseSwipeAndActionBarActivity;
import com.oppsis.app.hftracker.util.Constants;
import com.tjerkw.slideexpandable.library.AbstractSlideExpandableListAdapter.OnItemExpandCollapseListener;
import com.tjerkw.slideexpandable.library.SlideExpandableListAdapter;

@EActivity(R.layout.activity_mutual_security_old)
public class OldMutualSecurityActivity extends BaseSwipeAndActionBarActivity{

	@ViewById(R.id.mutual_security_list)protected ListView mSecurityList;
	@ViewById(R.id.loading_view) protected View mLoadingView;
	@ViewById(R.id.mutual_security_header_date)protected TextView mHeaderDateText;
	
	@Extra
	protected FundHolding holding;
	@Extra
	protected String date;
	
	//private MutualSecurityListAdapter mSecurityListAdapter;
	private SlideExpandableListAdapter mSecuritySlideAdapter;
	private List<FundHolding> mSecurityResults;
	private APIFacade mAPIFacade;
	private Handler mMainHandler = new Handler();
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            
            mAPIFacade = new APIFacade(getApplicationContext());
    }
	
	
	@AfterViews
	public void initUI(){
		super.initUI(holding.getSecurity(), Constants.FUND_MANAGER_ICONS.get(FundLocalInfoManager.getFundProperty(this, holding.getFund_name(),FundObject.PROPERTY_FUND_ID)));
		
		mSecurityList.setEmptyView(mLoadingView);
		
		mHeaderDateText.setText(date);
		
		loadData();
	}
	
	/*@ItemClick(R.id.mutual_security_list)
	protected void securityListItemClick(FundHolding holding){
		
	}*/
	
	
	@Background
	protected void loadData(){
		mAPIFacade.getHoldingMutualList(holding, date, new DefaultListCallback<FundHolding>(this) {
			
			@Override
			public void onSuccess(List<FundHolding> result) {
				mSecurityResults = result;
				
				mMainHandler.post(new Runnable() {
					
					@Override
					public void run() {
						MutualSecurityListAdapter securityListAdapter = new MutualSecurityListAdapter(OldMutualSecurityActivity.this, mSecurityResults);
						mSecuritySlideAdapter = new SlideExpandableListAdapter(
								securityListAdapter,
				                R.id.mutual_security_expandable_button,
				                R.id.mutual_security_expandable
				            );
						mSecuritySlideAdapter.setItemExpandCollapseListener(mExpandListener);
						mSecurityList.setAdapter(mSecuritySlideAdapter);
					}
				});
			}
		});
	}
	
	private OnItemExpandCollapseListener mExpandListener = new OnItemExpandCollapseListener(){
		@Override
		public void onExpand(View itemView, int position) {
			Log.d(this.toString(), "onExpand"
					+ ""
					+ "");
			/*Object tag = itemView.getTag();
			if(tag == null){
				final LineChart chart = (LineChart)itemView.findViewById(R.id.mutual_security_history_chart);
				mAPIFacede.getHoldingHistoryList(mSecurityResults.get(position), new ListCallback<FundHolding>() {
					@Override
					public void success(final List<FundHolding> result) {
						mMainHandler.post(new Runnable() {
							
							@Override
							public void run() {
								ChartGenerator.createHoldingHistoryLineChart(MutualSecurityActivity.this, chart, result);
								
							}
						});
					}
				});
				
				itemView.setTag(Constants.TRUE_STR);
			}*/
		}
		
		@Override
		public void onCollapse(View itemView, int position) {
			Log.d(this.toString(), "onCollapse");
		}
	};
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);

		getMenuInflater().inflate(R.menu.menu_share, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();

		return super.onOptionsItemSelected(item);
	}
	
}
