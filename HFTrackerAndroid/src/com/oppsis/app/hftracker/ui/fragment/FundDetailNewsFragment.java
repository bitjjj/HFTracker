package com.oppsis.app.hftracker.ui.fragment;

import java.util.List;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.ViewById;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.oppsis.app.hftracker.R;
import com.oppsis.app.hftracker.adapter.FundNewsListAdapter;
import com.oppsis.app.hftracker.api.util.DefaultListCallback;
import com.oppsis.app.hftracker.event.ChangeFundEvent;
import com.oppsis.app.hftracker.event.ChangeFundNewsEvent;
import com.oppsis.app.hftracker.event.OttoBus;
import com.oppsis.app.hftracker.model.FundNews;
import com.oppsis.app.hftracker.pojo.FundObject;
import com.oppsis.app.hftracker.pojo.FundObjectFetcher;
import com.oppsis.app.hftracker.ui.NewsDetailActivity;
import com.oppsis.app.hftracker.ui.base.BaseFragment;
import com.oppsis.app.hftracker.util.Constants;
import com.squareup.otto.Subscribe;
import com.umeng.analytics.MobclickAgent;

@EFragment(R.layout.fragment_fund_detail_news)
public class FundDetailNewsFragment extends BaseFragment implements OnScrollListener,SwipeRefreshLayout.OnRefreshListener{

	@ViewById(R.id.fund_news_list)protected ListView mNewsList;
	@ViewById(R.id.loading_view) protected View mLoadingView;
	@ViewById(R.id.fund_news_refresh_container) protected SwipeRefreshLayout refreshContainer;
	@Bean protected OttoBus mBus;
	private TextView mLoadingMore;
	private ProgressBar mLoadingMoreBar;
	private View mMoreView;
	
	private FundNewsListAdapter mNewsListAdapter;
	private List<FundNews> mNewsResults;
	private FundObject mFund;
	private boolean mIsFirstLoaded = false,mIsVisibleToUser = false;
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
				beforeLoadingMoreData();
			}
		});
		
		mNewsList.addFooterView(mMoreView);
		mNewsList.setEmptyView(mLoadingView);
		mNewsList.setOnScrollListener(this);
		
		refreshContainer.setOnRefreshListener(this);
		refreshContainer.setColorSchemeColors(
				getResources().getColor(android.R.color.holo_blue_bright),
				getResources().getColor(android.R.color.holo_green_light),
				getResources().getColor(android.R.color.holo_orange_light),
				getResources().getColor(android.R.color.holo_red_light));
		
		if(getResources().getBoolean(R.bool.init_news_list)){//from 7 tablet
			loadData();
		}
	}
	
	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		
		mIsVisibleToUser = isVisibleToUser;
		if (!isVisibleToUser || mFund == null) {
			return;
		}
		
		if(!mIsFirstLoaded){
			loadData();
			mIsFirstLoaded = true;
		}

	}
	
	@ItemClick(R.id.fund_news_list)
	protected void newsItemClick(FundNews news){
		if(getResources().getBoolean(R.bool.lanuch_news_detail_activity)){
			Intent i = new Intent(getActivity(),NewsDetailActivity.class);
			Bundle bundle = new Bundle();
			bundle.putParcelable("data", news);
			i.putExtras(bundle);
			
			getActivity().startActivity(i);
			getActivity().overridePendingTransition(R.anim.push_left_in,R.anim.no_anim);
		}
		else{
			mBus.post(new ChangeFundNewsEvent(news));
		}
	}
	
	private void beforeLoadingMoreData(){
		mLoadingMore.setText(getString(R.string.loading_data_label));
		mLoadingMoreBar.setVisibility(View.VISIBLE);
		
		loadData();
	}
	
	private void afterLoadingMoreData(){
		if(isAdded()){//avoid close window too fast before loading complete
			mLoadingMore.setText(getString(R.string.loading_more_data_label));
			mLoadingMoreBar.setVisibility(View.GONE);
		}
	}
	
	private void removeLoadingMoreData(){
		if(isAdded()){
			mNewsList.removeFooterView(mMoreView);
		}
	}
	
	@Background(id="news-list")
	protected void loadData(){
		mAPIFacade.getNewsList(mFund, mPage++,new NewsListCallback(getActivity()));
	}
	
	@Background(id="news-list")
	protected void refreshData(){
		mAPIFacade.refreshNewsList(mFund, new NewsListCallback(getActivity()));
	}
	
	@Subscribe
    public void onChangeFund(ChangeFundEvent event) {
		this.mFund = event.getFund();
		
		onRefresh();
        
    }
	
	@Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if(activity instanceof FundObjectFetcher){
        	mFund = ((FundObjectFetcher) activity).fetchFundObject();
        }
    }


	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		mLastVisibleIndex = firstVisibleItem + visibleItemCount - 1;
		
	}


	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if (scrollState == OnScrollListener.SCROLL_STATE_IDLE && mNewsListAdapter != null
                && mLastVisibleIndex >= mNewsListAdapter.getCount() - Constants.PAGE_LOADING_BEFORE_COUNT) {
			beforeLoadingMoreData();
		}
		
	}


	@Override
	public void onRefresh() {
		mPage = 0;
		if(mNewsResults != null){
			mNewsResults.clear();
			mNewsListAdapter.notifyDataSetInvalidated();
		}
		
		refreshData();
	}
	
	public void onResume() {
	    super.onResume();
	    MobclickAgent.onPageStart("Fund_Detail_News_Screen"); 
	}
	
	public void onPause() {
	    super.onPause();
	    MobclickAgent.onPageEnd("Fund_Detail_News_Screen"); 
	}
	
	@Override
    public void onDestroy() {
            super.onDestroy();
            mBus.unregister(this);
    }
	
	private class NewsListCallback extends DefaultListCallback<FundNews>{

		public NewsListCallback(Activity context){
			super(context);
		}
		
		@Override
		public void onSuccess(final List<FundNews> result) {
			mMainHandler.post(new Runnable() {
				
				@Override
				public void run() {
					if(result.size() > 0){
						
						if(mNewsListAdapter == null){
							mNewsResults = result;
							mNewsListAdapter = new FundNewsListAdapter(getActivity(), mNewsResults);
							mNewsList.setAdapter(mNewsListAdapter);
							
							//for tablet initialize news content at first position
							if(getResources().getBoolean(R.bool.load_first_news_detial)){
								mBus.post(new ChangeFundNewsEvent(mNewsResults.get(0)));
							}
						}
						else{
							mNewsResults.addAll(result);
							mNewsListAdapter.notifyDataSetChanged();
						}
						
						afterLoadingMoreData();
					}
					else{
						removeLoadingMoreData();
					}
					
					if(refreshContainer.isRefreshing()){
						refreshContainer.setRefreshing(false);
					}
				}
			});
			
		}
		
	}
}
