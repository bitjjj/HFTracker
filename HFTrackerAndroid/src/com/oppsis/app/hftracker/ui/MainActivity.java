package com.oppsis.app.hftracker.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.enrique.apprater.AppRater;
import com.oppsis.app.hftracker.R;
import com.oppsis.app.hftracker.adapter.FundListAdapter;
import com.oppsis.app.hftracker.event.ChangeFundEvent;
import com.oppsis.app.hftracker.event.OttoBus;
import com.oppsis.app.hftracker.pojo.FundLocalInfoManager;
import com.oppsis.app.hftracker.pojo.FundObject;
import com.oppsis.app.hftracker.ui.base.BaseActivity;
import com.oppsis.app.hftracker.ui.fragment.FilterDialogFragment_;
import com.oppsis.app.hftracker.util.Constants;
import com.oppsis.app.hftracker.util.IntentUtil;
import com.oppsis.app.util.FuncUtils;
import com.squareup.otto.Subscribe;

@EActivity(R.layout.activity_main)
public class MainActivity extends BaseActivity{
	
	@ViewById(R.id.hf_listview)protected ListView mHFListView;
	@ViewById(R.id.loading_view)protected View mLoadingView;
	@Bean protected OttoBus mBus;
	
	private FundListAdapter mFundAdapter;
	private List<FundObject[]> mFundItems;
	private int column = 2;
	private FundObject mFund;//for tablet
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mBus.register(this);  
		column = getResources().getInteger(R.integer.fund_list_cover_column);
		
		new AppRater(this)
	        .setAppTitle(getString(R.string.app_name))
	        .setAppPackage(Constants.PACKAGE_NAME)
	        .init();
	}
	
	@AfterViews
	protected void initUI(){
		if(mHFListView != null){
			mFundItems = new ArrayList<FundObject[]>();
			loadFundList();
		}
	}
	
	@SuppressWarnings("unchecked")
	@Background
	protected void loadFundList(){
		
		List<FundObject> fundList = (List<FundObject>)FuncUtils.deepClone(FundLocalInfoManager.getFundList(this));
		Collections.sort(fundList,new Comparator<FundObject>() {
			@Override
			public int compare(FundObject lhs, FundObject rhs) {
				return Integer.valueOf(lhs.getFundId()) - Integer.valueOf(rhs.getFundId());
			}
		});
		
		Iterator<FundObject> iter = fundList.iterator();
		while(iter.hasNext()){
			FundObject fund = iter.next();
			if(fund.getHero() != null && fund.getHero().equals(FundObject.HERO)){
				FundObject[] funds = new FundObject[1];
				funds[0] = fund;
				mFundItems.add(funds);
				iter.remove();
			}
		}
		
		int count = fundList.size()/column,remaining = fundList.size()%column;
		for(int i=0; i<count; i++){
			FundObject[] funds = new FundObject[column];
			for(int j=0; j<column; j++){
				funds[j] = fundList.get(j + i*column);
			}
			mFundItems.add(funds);
		}
		
		if(remaining != 0){
			FundObject[] funds = new FundObject[remaining];
			for(int i=0; i<remaining; i++){//TODO:[Next Release]sequence is wrong, omit this error temporarily.
				funds[i] = fundList.get(fundList.size() - i - 1);
			}
			mFundItems.add(funds);
		}
		
		showFundList();
	}
	
	@UiThread
	protected void showFundList() {
		mLoadingView.setVisibility(View.GONE);
		if(mFundAdapter == null){
			mFundAdapter = new FundListAdapter(this, mFundItems);
		}
		
		mHFListView.setAdapter(mFundAdapter);
	}
	
	//for tablet
	@Subscribe
    public void onChangeFund(ChangeFundEvent event) {
		this.mFund = event.getFund();
    }
	
	@Click(R.id.filter_action_btn)
	protected void showFilterDialog(){
		if(mFund != null){
			DialogFragment newFragment = FilterDialogFragment_.builder().tabIndex(Constants.ACTIONBAR_LIST_HOLDINGS).fund(mFund).build();
		    newFragment.show(getSupportFragmentManager(), "filter_dialog");
		}
	}
	//

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);

		if (getLPreviewUtils().shouldChangeActionBarForDrawer()
				&& isNavDrawerOpen()) {
			// nothing to show if nav drawer is open or animating
			return true;
		}

		getMenuInflater().inflate(R.menu.menu_main, menu);
		
		setupShareProvider(menu, IntentUtil.getShareIntent(getString(R.string.share_app_label, Constants.APP_LINK)));
		
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		int id = item.getItemId();
		switch (id) {
		case R.id.menu_item_search:
			SearchActivity_.intent(this).start();
			overridePendingTransition(R.anim.push_left_in,R.anim.no_anim);
			return true;
		
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	protected int getSelfNavDrawerItem() {
		return R.id.navdrawer_item_hf_list;
	}
	
	
	@Override
    public void onDestroy() {
            super.onDestroy();
            mBus.unregister(this);
    }
	
	
}
