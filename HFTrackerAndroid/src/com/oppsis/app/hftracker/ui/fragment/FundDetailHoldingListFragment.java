package com.oppsis.app.hftracker.ui.fragment;

import java.util.List;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.ViewById;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.oppsis.app.hftracker.R;
import com.oppsis.app.hftracker.adapter.FundHoldingListAdapter;
import com.oppsis.app.hftracker.api.util.DefaultListCallback;
import com.oppsis.app.hftracker.event.ChangeFundEvent;
import com.oppsis.app.hftracker.event.ChangeFundFilterEvent;
import com.oppsis.app.hftracker.event.OttoBus;
import com.oppsis.app.hftracker.model.FundHolding;
import com.oppsis.app.hftracker.pojo.FundObject;
import com.oppsis.app.hftracker.ui.MutualSecurityActivity_;
import com.oppsis.app.hftracker.ui.base.BaseFragment;
import com.oppsis.app.hftracker.ui.widget.FilterPanelView.FundFilter;
import com.oppsis.app.hftracker.util.Constants;
import com.squareup.otto.Subscribe;

@EFragment(R.layout.fragment_fund_detial_holding_list)
public class FundDetailHoldingListFragment extends BaseFragment implements OnScrollListener{

	@ViewById(R.id.fund_holding_list) protected ListView mHoldingList;
	@ViewById(R.id.loading_view) protected View mLoadingView;
	@ViewById(R.id.fund_detail_header_date)protected TextView mHeaderDateText;
	private TextView mLoadingMore;
	private ProgressBar mLoadingMoreBar;
	private View mMoreView;
	
	@FragmentArg("fund") protected FundObject fund;
	@Bean protected OttoBus mBus;
	
	private FundHoldingListAdapter mHoldingListAdapter;
	private List<FundHolding> mHoldingResults;
	private FundFilter mFilter = null;
	private int mPage = 0,mLastVisibleIndex;

	@Override
    public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            mBus.register(this);
    }
	
	@Subscribe
    public void onChangeFundFilter(ChangeFundFilterEvent event) {
        if(event.getFundFilter().getSort() != null && 
        		event.getFundFilter().getDirection() != null){//avoid filter from industry tab
			this.mFilter = event.getFundFilter();
	        mPage = 0;
	        beforeLoadingData();
        }
    }
	
	@Subscribe
    public void onChangeFund(ChangeFundEvent event) {
       
		this.fund = event.getFund();
	    mPage = 0;
	    beforeLoadingData();
        
    }
	
	
	@AfterViews
	protected void initUI(){
		mMoreView = getActivity().getLayoutInflater().inflate(R.layout.list_item_footer_more, null);
		mLoadingMore = (TextView) mMoreView.findViewById(R.id.data_loading_text);
		mLoadingMoreBar = (ProgressBar) mMoreView.findViewById(R.id.data_loading_bar);
		
		mMoreView.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				beforeLoadingMoreData();
			}
		});
		
		mHoldingList.addFooterView(mMoreView);
		mHoldingList.setEmptyView(mLoadingView);
		mHoldingList.setOnScrollListener(this);
		
		beforeLoadingData();
	}
	
	@ItemClick(R.id.fund_holding_list)
	protected void holdingListItemClick(FundHolding holding){
		//getParentFragment() != null ? getParentFragment() : getActivity()
		MutualSecurityActivity_.intent(getActivity()).holding(holding).date(mHeaderDateText.getText().toString()).start();
		getActivity().overridePendingTransition(R.anim.push_left_in,R.anim.no_anim);
	}
	
	private void beforeLoadingData(){
		if(mHoldingResults != null){
			mHoldingResults.clear();
			mHoldingListAdapter.notifyDataSetChanged();
		}
		
		loadData();
	}
	
	private void beforeLoadingMoreData(){
		if(isAdded()){
			mLoadingMore.setText(getString(R.string.loading_data_label));
			mLoadingMoreBar.setVisibility(View.VISIBLE);
		}
		
		loadData();
	}
	
	private void afterLoadingMoreData(){
		if(isAdded()){
			mLoadingMore.setText(getString(R.string.loading_more_data_label));
			mLoadingMoreBar.setVisibility(View.GONE);
		}
	}
	
	private void removeLoadingMoreData(){
		mHoldingList.removeFooterView(mMoreView);
	}
	
	@Background
	protected void loadData(){
		if(fund == null)return;
		
		mAPIFacade.getHoldingList(fund, mFilter,mPage++,new DefaultListCallback<FundHolding>(getActivity()){
			@Override
			public void onSuccess(final List<FundHolding> result) {
				mMainHandler.post(new Runnable() {
					
					@Override
					public void run() {
						if(result.size() > 0){
							mHeaderDateText.setText(result.get(0).getDateStr());
							if(mHoldingListAdapter == null){
								mHoldingResults = result;
								mHoldingListAdapter = new FundHoldingListAdapter(getActivity(), mHoldingResults);
								mHoldingList.setAdapter(mHoldingListAdapter);
							}
							else{
								mHoldingResults.addAll(result);
								mHoldingListAdapter.notifyDataSetChanged();
							}
							
							afterLoadingMoreData();
						}
						else{
							removeLoadingMoreData();
						}
						
						if(result.size() == 0){
							Toast.makeText(getActivity(), getString(R.string.no_data_tip), Toast.LENGTH_SHORT).show();
						}
					}
				});
				
			}
		});
	}
	
	
	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		mLastVisibleIndex = firstVisibleItem + visibleItemCount - 1;
	}


	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if (scrollState == OnScrollListener.SCROLL_STATE_IDLE && mHoldingListAdapter != null
                && mLastVisibleIndex >= mHoldingListAdapter.getCount() - Constants.PAGE_LOADING_BEFORE_COUNT) {
			beforeLoadingMoreData();
		}
		
	}
	
    @Override
    public void onDestroy() {
            super.onDestroy();
            mBus.unregister(this);
    }

}
