package com.oppsis.app.hftracker.ui;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ItemSelect;
import org.androidannotations.annotations.ViewById;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.oppsis.app.hftracker.R;
import com.oppsis.app.hftracker.adapter.RankExpandableListAdapter;
import com.oppsis.app.hftracker.api.util.DefaultListCallback;
import com.oppsis.app.hftracker.model.FundAnalysis;
import com.oppsis.app.hftracker.model.FundDateList;
import com.oppsis.app.hftracker.pojo.StockQuoteObject;
import com.oppsis.app.hftracker.ui.base.BaseActivity;
import com.oppsis.app.hftracker.util.Constants;
import com.oppsis.app.hftracker.util.FundFuncUtils;
import com.oppsis.app.hftracker.util.IntentUtil;
import com.oppsis.app.hftracker.util.YahooStockPriceUtils;

@EActivity(R.layout.activity_rank)
public class RankActivity extends BaseActivity implements OnScrollListener{

	@ViewById(R.id.rank_holding_list) protected ExpandableListView mHoldingList;
	@ViewById(R.id.loading_view) protected View mLoadingView;
	@ViewById(R.id.rank_date_selector) Spinner mDateSpinner;
	@ViewById(R.id.rank_security_type_selector) Spinner mTypeSpinner;
	private TextView mLoadingMore;
	private ProgressBar mLoadingMoreBar;
	private View mMoreView;
	
	private ArrayAdapter<CharSequence> mTypeAdapter,mDateAdapter;
	private RankExpandableListAdapter mHoldingListAdapter;
	private HashMap<String, List<Map.Entry<String, Float>>> mHoldingResultsChild;
	private List<FundAnalysis> mHoldingResults;

	private int mPage = 0,mLastVisibleIndex;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}
	
	@AfterViews
	protected void initUI(){
		getActionBar().setTitle(R.string.menu_hf_stock_rank);
		
		mMoreView = getLayoutInflater().inflate(R.layout.list_item_footer_more, null);
		mLoadingMore = (TextView) mMoreView.findViewById(R.id.data_loading_text);
		mLoadingMoreBar = (ProgressBar) mMoreView.findViewById(R.id.data_loading_bar);
		
		mMoreView.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				beforeLoadingMoreData();
			}
		});
		
		mHoldingList.setEmptyView(mLoadingView);
		mHoldingList.addFooterView(mMoreView);
		mHoldingList.setOnScrollListener(this);
		
		mTypeAdapter = ArrayAdapter.createFromResource(this,
		        R.array.security_types,R.layout.rank_spinner_text);
		mTypeAdapter.setDropDownViewResource(R.layout.rank_spinner_dropdown_text);
		mTypeSpinner.setAdapter(mTypeAdapter);
		
		loadDateList();
	}
	
	@Background(id="analysis_rank")
	protected void loadDateList(){
		mAPIFacade.getDateListAll(new DefaultListCallback<FundDateList>(this) {
			
			@Override
			public void onSuccess(List<FundDateList> result) {
				final CharSequence[] choice = new CharSequence[result.size()];
				for(int i=0; i<result.size(); i++){
					choice[i] = result.get(i).getDateStr();
				}
				
				mMainHandler.post(new Runnable() {				
					@Override
					public void run() {
						mDateAdapter = new ArrayAdapter<CharSequence>(RankActivity.this, R.layout.rank_spinner_text, choice);
						mDateAdapter.setDropDownViewResource(R.layout.rank_spinner_dropdown_text);
						mDateSpinner.setAdapter(mDateAdapter);
						mDateSpinner.setSelection(0);
					}
				});
			}
		});
	}
	
	@Background(id="analysis_rank")
	protected void loadData(){
		String date = mDateAdapter.getItem(mDateSpinner.getSelectedItemPosition()).toString(),
				securityType = Constants.SECURITY_TYPES_ARR[mTypeSpinner.getSelectedItemPosition()];
		mPage = Constants.PAGE_ZERO;
		mAPIFacade.getRankList(date, securityType, mPage++, new DefaultListCallback<FundAnalysis>(this) {
			@Override
			public void onSuccess(final List<FundAnalysis> result) {
				final Map<String, StockQuoteObject> stockQuote = YahooStockPriceUtils.getStockPrice(FundFuncUtils.getStockTickerList(result));
				
				if(mHoldingResultsChild == null)mHoldingResultsChild = new HashMap<String, List<Entry<String,Float>>>();
				FundFuncUtils.addHoldingResultsChildren(result,mHoldingResultsChild);
				
				mMainHandler.post(new Runnable() {
					
					@Override
					public void run() {
						if(result.size() > 0){
							
							if(mHoldingListAdapter == null){
								mHoldingResults = result;
								mHoldingListAdapter = new RankExpandableListAdapter(RankActivity.this, mHoldingResults,mHoldingResultsChild,stockQuote);
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
							Toast.makeText(RankActivity.this, R.string.no_data_tip, Toast.LENGTH_SHORT).show();
						}
						
						if(result.size() < Constants.PAGE_COUNT){
							removeLoadingMoreView();
						}
						else if(result.size() >= Constants.PAGE_COUNT && mHoldingList.getFooterViewsCount() == 0){
							addLoadingMoreView();
						}

					}
				});
			}
		});
	}
	
	@Background(id="analysis_rank")
	protected void loadMoreData(){
		String date = mDateAdapter.getItem(mDateSpinner.getSelectedItemPosition()).toString(),
				securityType = Constants.SECURITY_TYPES_ARR[mTypeSpinner.getSelectedItemPosition()];
		mAPIFacade.getRankList(date, securityType, mPage++, new DefaultListCallback<FundAnalysis>(this) {
			
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
	
	@ItemSelect(R.id.rank_security_type_selector)
    public void securityTypeSelected(boolean selected, int position) {
		Log.d(this.toString(), "securityTypeSelected" + selected + " " + position);
		if(mDateAdapter != null && mTypeAdapter != null){
			loadData();
		}
    }
	
	@ItemSelect(R.id.rank_date_selector)
    public void dateSelected(boolean selected, int position) {
		Log.d(this.toString(), "dateSelected" + selected + " " + position);
		if(mDateAdapter != null && mTypeAdapter != null){
			loadData();
		}
    }
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);

		if (getLPreviewUtils().shouldChangeActionBarForDrawer()
				&& isNavDrawerOpen()) {
			// nothing to show if nav drawer is open or animating
			return true;
		}

		getMenuInflater().inflate(R.menu.menu_sub, menu);
		
		setupShareProvider(menu, IntentUtil.getShareIntent(getString(R.string.share_app_rank_label, Constants.APP_LINK)));
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		//int id = item.getItemId();

		return super.onOptionsItemSelected(item);
	}
	
	private void beforeLoadingMoreData(){

		mLoadingMore.setText(getString(R.string.loading_data_label));
		mLoadingMoreBar.setVisibility(View.VISIBLE);
		
		loadMoreData();
	}
	
	private void afterLoadingMoreData(){
		mLoadingMore.setText(getString(R.string.loading_more_data_label));
		mLoadingMoreBar.setVisibility(View.GONE);
	}
	
	private void addLoadingMoreView(){
		mHoldingList.addFooterView(mMoreView);
	}
	
	private void removeLoadingMoreView(){
		mHoldingList.removeFooterView(mMoreView);
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if (scrollState == OnScrollListener.SCROLL_STATE_IDLE && mHoldingListAdapter != null
                && mLastVisibleIndex >= mHoldingListAdapter.getGroupCount() - Constants.PAGE_LOADING_BEFORE_COUNT) {
			beforeLoadingMoreData();
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		mLastVisibleIndex = firstVisibleItem + visibleItemCount - 1;
	}
	
	
	
	protected int getSelfNavDrawerItem() {
		return R.id.navdrawer_item_hf_stock_rank;
	}
}
