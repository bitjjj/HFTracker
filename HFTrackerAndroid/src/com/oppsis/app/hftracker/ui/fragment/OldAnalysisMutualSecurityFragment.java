package com.oppsis.app.hftracker.ui.fragment;

import java.util.List;
import java.util.Map;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.oppsis.app.hftracker.R;
import com.oppsis.app.hftracker.adapter.AnalysisHoldingListAdapter;
import com.oppsis.app.hftracker.api.APIManager.ListCallback;
import com.oppsis.app.hftracker.api.util.DefaultListCallback;
import com.oppsis.app.hftracker.event.ChangeAnalysisDateEvent;
import com.oppsis.app.hftracker.event.ChangeAnalysisManagersEvent;
import com.oppsis.app.hftracker.event.OttoBus;
import com.oppsis.app.hftracker.model.FundAnalysis;
import com.oppsis.app.hftracker.pojo.StockQuoteObject;
import com.oppsis.app.hftracker.util.Constants;
import com.oppsis.app.hftracker.util.FundFuncUtils;
import com.oppsis.app.hftracker.util.YahooStockPriceUtils;
import com.squareup.otto.Subscribe;

@EFragment(R.layout.fragment_analysis_mutual_security)
public class OldAnalysisMutualSecurityFragment extends AnalysisBaseFragment implements OnScrollListener{

	@ViewById(R.id.analysis_holding_list) protected ListView mHoldingList;
	@ViewById(R.id.chart_placeholder) protected ImageView mPlaceholderImg;
	@ViewById(R.id.container) protected View mContainer;
	private TextView mLoadingMore;
	private ProgressBar mLoadingMoreBar;
	private View mMoreView;
	
	@Bean
    protected OttoBus mBus;
	
	private AnalysisHoldingListAdapter mHoldingListAdapter;
	private List<FundAnalysis> mHoldingResults;
	private int mPage = 0,mLastVisibleIndex;

	
	@Override
    public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            mBus.register(this);  
    }
	
	@AfterViews
	protected void initUI(){
		mMoreView = getActivity().getLayoutInflater().inflate(R.layout.list_item_footer_more, null);
		mLoadingMore = (TextView) mMoreView.findViewById(R.id.data_loading_text);
		mLoadingMoreBar = (ProgressBar) mMoreView.findViewById(R.id.data_loading_bar);
		
		mMoreView.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				if(mHoldingResults.size() != 0)beforeLoadingMoreData();
			}
		});
		
		mHoldingList.addFooterView(mMoreView);
		mHoldingList.setOnScrollListener(this);
		
		setTouchEvent(mHoldingList);
		setTouchEvent(mContainer);
	}
	
	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		checkIfLoadingData();
	}
	
	
	@Background(id="analysis_mutual")
	protected void loadData(List<String> managerNameList){//just for load first page
			mAPIFacade.getAnalysisMutualList(managerNameList, mDate, mPage++, new DefaultListCallback<FundAnalysis>(getActivity()) {
				
				@Override
				public void onSuccess(final List<FundAnalysis> result) {

					final Map<String, StockQuoteObject> stockData = YahooStockPriceUtils.getStockPrice(FundFuncUtils.getStockTickerList(result));
					
					mMainHandler.post(new Runnable() {
						
						@Override
						public void run() {
							if(result.size() > 0){
								mPlaceholderImg.setVisibility(View.GONE);
								mStatusChangeListener.hideManagerListBar();
								
								if(mHoldingListAdapter == null){
									mHoldingResults = result;
									mHoldingListAdapter = new AnalysisHoldingListAdapter(getActivity(), mHoldingResults,stockData);
									mHoldingList.setAdapter(mHoldingListAdapter);
								}
								else{
									mHoldingResults.clear();
									mHoldingResults.addAll(result);
									mHoldingListAdapter.notifyDataSetChanged();
								}

							}
							
						}
					});
				}
			});

	}
	
	@Background(id="analysis_mutual")
	protected void loadMoreData(List<String> managerNameList){
		mAPIFacade.getAnalysisMutualList(managerNameList, mDate, mPage++, new DefaultListCallback<FundAnalysis>(getActivity()) {
			
			@Override
			public void onSuccess(final List<FundAnalysis> result) {

				mMainHandler.post(new Runnable() {
					
					@Override
					public void run() {
						if(result.size() > 0){
							afterLoadingMoreData();
							
							mHoldingResults.addAll(result);
							mHoldingListAdapter.notifyDataSetChanged();

						}
						else{
							removeLoadingMoreData();
						}
						
					}
				});
			}
		});
	}
	
	protected void clearData(){
		mPage = 0;
		mHoldingResults.clear();
		mHoldingListAdapter.notifyDataSetChanged();
		mStatusChangeListener.hideManagerListBar();
	}
	
	@Subscribe
	public void onChangeAnalysisDate(ChangeAnalysisDateEvent event) {
		changeAnalysisDate(event.getDate());
		
		beforeLoadingData();
	}
	
	@Subscribe
	public void onChangeAnalysisManagers(ChangeAnalysisManagersEvent event) {
		changeAnalysisManagers(event.getSelectedManagers());
		
		beforeLoadingData();
	}

	private void beforeLoadingData(){
		if(mHoldingResults != null){
			mHoldingResults.clear();
			mHoldingListAdapter.notifyDataSetChanged();
			mPlaceholderImg.setVisibility(View.VISIBLE);
		}
		
		checkIfLoadingData();
	}
	
	private void beforeLoadingMoreData(){

		mLoadingMore.setText(getString(R.string.loading_data_label));
		mLoadingMoreBar.setVisibility(View.VISIBLE);

		mReload = true;
		
		loadMoreData(getManagerNameList());
	}
	
	private void afterLoadingMoreData(){
		mLoadingMore.setText(getString(R.string.loading_more_data_label));
		mLoadingMoreBar.setVisibility(View.GONE);
	}
	
	private void removeLoadingMoreData(){
		mHoldingList.removeFooterView(mMoreView);
	}
	
	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if (scrollState == OnScrollListener.SCROLL_STATE_IDLE && mHoldingListAdapter != null
                && mLastVisibleIndex >= mHoldingListAdapter.getCount() - Constants.PAGE_LOADING_BEFORE_COUNT) {
			beforeLoadingMoreData();
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		mLastVisibleIndex = firstVisibleItem + visibleItemCount - 1;
	}
	
	@Override
    public void onDestroy() {
            super.onDestroy();
            mBus.unregister(this);
    }


}
