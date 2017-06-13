
package com.oppsis.app.hftracker.ui;

import java.util.List;
import java.util.Map;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.ViewById;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;

import com.oppsis.app.hftracker.R;
import com.oppsis.app.hftracker.adapter.SearchListAdapter;
import com.oppsis.app.hftracker.api.util.DefaultListCallback;
import com.oppsis.app.hftracker.model.FundHolding;
import com.oppsis.app.hftracker.ui.base.BaseSwipeAndActionBarActivity;
import com.oppsis.app.hftracker.ui.widget.KeywordsFlow;
import com.oppsis.app.hftracker.util.Constants;
import com.oppsis.app.hftracker.util.YahooStockSuggestions;

@EActivity(R.layout.activity_search)
public class SearchActivity extends BaseSwipeAndActionBarActivity implements OnScrollListener{

    @ViewById(R.id.tag_cloud) protected KeywordsFlow keywordsFlow;
    @ViewById(R.id.search_list_container) protected View mSearchListContainer;
    @ViewById(R.id.search_list) protected ListView mSearchList;
    @ViewById(R.id.loading_view) protected View mLoadingView;
    @ViewById(R.id.search_header_ticker) protected TextView mHeaderTicker;
    private TextView mLoadingMore;
	private ProgressBar mLoadingMoreBar;
	private View mMoreView;
	
	private SearchListAdapter mSearchListAdapter;
	private List<FundHolding> mSearchResults;
    private SearchView mSearchView = null;
    private String mQuery = "";
	private int mPage = 0,mLastVisibleIndex;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String query = getIntent().getStringExtra(SearchManager.QUERY);
        query = query == null ? "" : query;
        mQuery = query;


        if (mSearchView != null) {
            mSearchView.setQuery(query, false);
        }

        overridePendingTransition(0, 0);
    }
    
    @AfterViews
    public void initUI(){
    	super.initUI();
    	keywordsFlow.setDuration(800l);  
        keywordsFlow.setOnItemClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mQuery = ((TextView) v).getText().toString();
				beforeLoadingData();
				loadData();
				
			}
		});
        
        mMoreView = getLayoutInflater().inflate(R.layout.list_item_footer_more, null);
		mLoadingMore = (TextView) mMoreView.findViewById(R.id.data_loading_text);
		mLoadingMoreBar = (ProgressBar) mMoreView.findViewById(R.id.data_loading_bar);
		
		mMoreView.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				beforeLoadingMoreData();
			}
		});
		
		mSearchList.addFooterView(mMoreView);
		mSearchList.setEmptyView(mLoadingView);
		mSearchList.setOnScrollListener(this);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        onNewIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {

        setIntent(intent);
        String query = intent.getStringExtra(SearchManager.QUERY);
        loadSuggestions(query);
    }

    @Background(id = "search")
    protected void loadSuggestions(final String query){
    	if(query != null && !"".equals(query)){
    		final List<Map<String,String>> suggestionsList = YahooStockSuggestions.getSuggestions(query);
    		Log.d(this.toString(), suggestionsList.toString());
    		mMainHandler.post(new Runnable() {
				@Override
				public void run() {
					keywordsFlow.rubKeywords();   
					for(int i=0; i<suggestionsList.size(); i++){
						 keywordsFlow.feedKeyword(suggestionsList.get(i).get("symbol")); 
					}
		            if(query.length() % 2 == 0){
		            	keywordsFlow.go2Show(KeywordsFlow.ANIMATION_IN); 
		            }
		            else{
		            	keywordsFlow.go2Show(KeywordsFlow.ANIMATION_OUT); 
		            }
					
				}
			});
    	}
    }
    
    @Background(id = "search")
    protected void loadData(){
    	if(mQuery != null && !"".equals(mQuery)){
    		
    		mAPIFacade.getHoldingSearchList(mQuery, mPage++, new DefaultListCallback<FundHolding>(this) {			
				@Override
				public void onSuccess(final List<FundHolding> result) {
					mMainHandler.post(new Runnable() {
						
						@Override
						public void run() {
							mHeaderTicker.setText(mQuery.toUpperCase());
							if(result.size() > 0){
								
								if(mSearchListAdapter == null){
									mSearchResults = result;
									mSearchListAdapter = new SearchListAdapter(SearchActivity.this, mSearchResults);
									mSearchList.setAdapter(mSearchListAdapter);
								}
								else{
									mSearchResults.addAll(result);
									mSearchListAdapter.notifyDataSetChanged();
								}
								
								afterLoadingMoreData();
							}
							else{
								removeLoadingMoreData();
							}
						}
					});
				}
			});
    	}
    }
    
    @ItemClick(R.id.search_list)
	protected void holdingListItemClick(FundHolding holding){
		MutualSecurityActivity_.intent(this).holding(holding).date(holding.getDateStr()).start();
		overridePendingTransition(R.anim.push_left_in,R.anim.no_anim);
	}
	
	private void beforeLoadingMoreData(){
		mLoadingMore.setText(getString(R.string.loading_data_label));
		mLoadingMoreBar.setVisibility(View.VISIBLE);
		
		loadData();
	}
	
	private void afterLoadingMoreData(){
		mLoadingMore.setText(getString(R.string.loading_more_data_label));
		mLoadingMoreBar.setVisibility(View.GONE);
	}
	
	private void removeLoadingMoreData(){
		mSearchList.removeFooterView(mMoreView);
	}
    
    private void beforeLoadingData(){
    	mPage = 0;
    	if(mSearchListAdapter != null){
    		mSearchResults.clear();
    		mSearchListAdapter.notifyDataSetChanged();
    	}
    	keywordsFlow.setVisibility(View.INVISIBLE);
    	mSearchListContainer.setVisibility(View.VISIBLE);
    }
    
    @Override
    public void onPause() {
        super.onPause();
        if (isFinishing()) {
            overridePendingTransition(0, 0);
        }
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_search, menu);
        final MenuItem searchItem = menu.findItem(R.id.menu_item_search);
        if (searchItem != null) {
            SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
            final SearchView view = (SearchView) searchItem.getActionView();
            mSearchView = view;
            if (view == null) {
            } else {
                view.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
                view.setIconified(false);
                view.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String s) {
                    	mQuery = s;
                        view.clearFocus();
                        beforeLoadingData();
                        loadData();
                        return true;
                    }

                    @Override
                    public boolean onQueryTextChange(String s) {
                    	loadSuggestions(s);
                        return true;
                    }
                });
                view.setOnCloseListener(new SearchView.OnCloseListener() {
                    @Override
                    public boolean onClose() {
                        finish();//TODO:[Next Release]Should clear list and show tag cloud again.
                        return false;
                    }
                });
            }

            if (!TextUtils.isEmpty(mQuery)) {
                view.setQuery(mQuery, false);
            }
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_item_search) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    @Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		mLastVisibleIndex = firstVisibleItem + visibleItemCount - 1;
	}


	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if (scrollState == OnScrollListener.SCROLL_STATE_IDLE && mSearchListAdapter != null
                && mLastVisibleIndex >= mSearchListAdapter.getCount() - Constants.PAGE_LOADING_BEFORE_COUNT) {
			beforeLoadingMoreData();
		}
		
	}
}
