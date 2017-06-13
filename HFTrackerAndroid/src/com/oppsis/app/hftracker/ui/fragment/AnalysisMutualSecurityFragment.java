package com.oppsis.app.hftracker.ui.fragment;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.oppsis.app.hftracker.R;
import com.oppsis.app.hftracker.adapter.AnalysisHoldingExpandableListAdapter;
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
import com.umeng.analytics.MobclickAgent;
//FIXME:not work in en env,索罗斯，巴菲特 2014.9.30
@EFragment(R.layout.fragment_analysis_mutual_security)
public class AnalysisMutualSecurityFragment extends AnalysisBaseFragment implements OnScrollListener{

	@ViewById(R.id.analysis_holding_list) protected ExpandableListView mHoldingList;
	@ViewById(R.id.chart_placeholder) protected ImageView mPlaceholderImg;
	@ViewById(R.id.container) protected View mContainer;
	private TextView mLoadingMore;
	private ProgressBar mLoadingMoreBar;
	private View mMoreView;
	
	@Bean
    protected OttoBus mBus;
	
	private AnalysisHoldingExpandableListAdapter mHoldingListAdapter;
	private HashMap<String, List<Map.Entry<String, Float>>> mHoldingResultsChild;
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
		
		mHoldingList.setOnGroupClickListener(new OnGroupClickListener() {
			@Override
			public boolean onGroupClick(ExpandableListView parent, View v, final int groupPosition, long id) {
				return true;
			}
		});
		
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
			mPage = Constants.PAGE_ZERO;
			mAPIFacade.getAnalysisMutualList(managerNameList, mDate, mPage++, new DefaultListCallback<FundAnalysis>(getActivity()) {
				@Override
				public void onSuccess(final List<FundAnalysis> result) {

					final Map<String, StockQuoteObject> stockQuote = YahooStockPriceUtils.getStockPrice(FundFuncUtils.getStockTickerList(result));
					
					if(mHoldingResultsChild == null)mHoldingResultsChild = new HashMap<String, List<Entry<String,Float>>>();
					FundFuncUtils.addHoldingResultsChildren(result,mHoldingResultsChild);
					
					mMainHandler.post(new Runnable() {
						
						@Override
						public void run() {
							if(result.size() > 0){
								mPlaceholderImg.setVisibility(View.GONE);
								mStatusChangeListener.hideManagerListBar();
								
								if(mHoldingListAdapter == null){
									mHoldingResults = result;
									mHoldingListAdapter = new AnalysisHoldingExpandableListAdapter(getActivity(), mHoldingResults,mHoldingResultsChild,stockQuote);
									mHoldingList.setAdapter(mHoldingListAdapter);
								}
								else{
									mHoldingResults.clear();
									mHoldingResults.addAll(result);
									mHoldingListAdapter.setStockQuote(stockQuote);
									mHoldingListAdapter.notifyDataSetChanged();
								}

							}
							else if(result.size() <= 0){
								Toast.makeText(getActivity(), R.string.no_data_tip, Toast.LENGTH_SHORT).show();
							}
							
							if(result.size() < Constants.PAGE_COUNT){
								removeLoadingMoreView();
							}
							else if(result.size() >= Constants.PAGE_COUNT){
								addLoadingMoreView();
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
				final Map<String, StockQuoteObject> stockQuote = YahooStockPriceUtils.getStockPrice(FundFuncUtils.getStockTickerList(result));
				FundFuncUtils.addHoldingResultsChildren(result,mHoldingResultsChild);
				
				mMainHandler.post(new Runnable() {
					
					@Override
					public void run() {
						if(result.size() > 0){
							afterLoadingMoreData();
							
							mHoldingResults.addAll(result);
							mHoldingListAdapter.addStockQuote(stockQuote);
							mHoldingListAdapter.notifyDataSetChanged();
						}
						else{
							removeLoadingMoreView();
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
	
	private void addLoadingMoreView(){
		//if(mHoldingList.getFooterViewsCount() == 0){
			//mHoldingList.addFooterView(mMoreView);
			mMoreView.setVisibility(View.VISIBLE);
		//}
	}
	
	private void removeLoadingMoreView(){
		//if(mHoldingList.getFooterViewsCount() > 0){
			//mHoldingList.removeFooterView(mMoreView);
			mMoreView.setVisibility(View.INVISIBLE);
		//}
	}
	
	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if (scrollState == OnScrollListener.SCROLL_STATE_IDLE && mHoldingListAdapter != null && mMoreView.getVisibility() == View.VISIBLE
                && mLastVisibleIndex >= mHoldingListAdapter.getGroupCount() - Constants.PAGE_LOADING_BEFORE_COUNT) {
			beforeLoadingMoreData();
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		mLastVisibleIndex = firstVisibleItem + visibleItemCount - 1;
	}
	
	public void onResume() {
	    super.onResume();
	    MobclickAgent.onPageStart("Analysis_Mutual_Security_Screen");
	}
	public void onPause() {
	    super.onPause();
	    MobclickAgent.onPageEnd("Analysis_Mutual_Security_Screen"); 
	}
	
	@Override
    public void onDestroy() {
            super.onDestroy();
            mBus.unregister(this);
    }


}
