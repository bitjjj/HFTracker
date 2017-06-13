package com.oppsis.app.hftracker.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import android.app.ActionBar;
import android.app.ActionBar.OnNavigationListener;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.DrawerLayout.DrawerListener;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SimpleAdapter;

import com.oppsis.app.hftracker.R;
import com.oppsis.app.hftracker.adapter.FundDetailPagerAdapter;
import com.oppsis.app.hftracker.event.ChangeFundFilterEvent;
import com.oppsis.app.hftracker.event.OttoBus;
import com.oppsis.app.hftracker.pojo.FundObject;
import com.oppsis.app.hftracker.pojo.FundObjectFetcher;
import com.oppsis.app.hftracker.ui.base.BaseSwipeAndActionBarActivity;
import com.oppsis.app.hftracker.ui.widget.FilterPanelView;
import com.oppsis.app.hftracker.ui.widget.FilterPanelView.FundFilter;
import com.oppsis.app.hftracker.ui.widget.FilterPanelView.FundFilterCallback;
import com.oppsis.app.hftracker.util.Constants;
import com.oppsis.app.hftracker.util.IntentUtil;
import com.oppsis.app.util.SharedPrefsUtils;
import com.umeng.analytics.MobclickAgent;

@SuppressWarnings("deprecation")
@EActivity(R.layout.activity_fund_detail)
public class FundDetailActivity extends BaseSwipeAndActionBarActivity implements FundFilterCallback,FundObjectFetcher{
	
	@ViewById(R.id.pager)protected ViewPager mViewPager;
	@ViewById(R.id.drawer_layout)protected DrawerLayout mDrawerLayout;
	@ViewById(R.id.navdrawer)protected FilterPanelView mFilterPanelView;

	@Extra("fund")protected FundObject fund;
	
	@Bean protected OttoBus bus;
	
	private List<Map<String, Object>> mFragmentData;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MobclickAgent.openActivityDurationTrack(false);
	}
	
	@AfterViews
	public void initUI(){
		super.initUI();
		
		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow_end, Gravity.RIGHT);
		mDrawerLayout.setDrawerListener(new DrawerListener() {
			
			@Override
			public void onDrawerStateChanged(int arg0) {
				invalidateOptionsMenu();
			}
			
			@Override
			public void onDrawerSlide(View arg0, float arg1) {
				invalidateOptionsMenu();
			}
			
			
			@Override
			public void onDrawerOpened(View arg0) {
				mFilterPanelView.initUI(fund,getActionBar().getSelectedNavigationIndex());
				invalidateOptionsMenu();
			}
			
			@Override
			public void onDrawerClosed(View arg0) {
				invalidateOptionsMenu();
			}
		});
		
		mViewPager = (ViewPager)findViewById(R.id.pager);
		
		ActionBar ab = getActionBar();
		ab.setIcon(Constants.FUND_MANAGER_ICONS.get(fund.getFundId()));
		ab.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
	
		mFragmentData = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < Constants.FUND_DETAIL_FRAGMENT_NAMES.length; i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("title", getResources().getString(Constants.FUND_DETAIL_FRAGMENT_TITLE_IDS[i]).toUpperCase());
			mFragmentData.add(map);
		}
	
		SimpleAdapter adapter = new SimpleAdapter(this, mFragmentData,
					R.layout.nav_list_textview, new String[] { "title" },
					new int[] { android.R.id.text1 });
		// adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	
		ab.setListNavigationCallbacks(adapter, navigationListener);
		
		
		FundDetailPagerAdapter pagerAdapter = new FundDetailPagerAdapter(
				getSupportFragmentManager(), this);
		mViewPager.setAdapter(pagerAdapter);
		mViewPager.setOffscreenPageLimit(3);// keep fragment instance both side
		//mViewPager.setPageTransformer(true, new DepthPageTransformer());
		
		//ab.setSelectedNavigationItem(SharedPrefsUtils.getInt(this, Constants.PrefKeys.ACTIONBAR_LIST_INDEX + fund.getFundId(), 0));

		mFilterPanelView.setFundFilterCallback(this);
	}
	
	private OnNavigationListener navigationListener = new OnNavigationListener() {
		@Override
		public boolean onNavigationItemSelected(int itemPosition,long itemId) {

			mViewPager.setCurrentItem(itemPosition,false);
			
			mFilterPanelView.adjustUISection(getActionBar().getSelectedNavigationIndex());
			invalidateOptionsMenu();
			SharedPrefsUtils.save(FundDetailActivity.this, Constants.PrefKeys.ACTIONBAR_LIST_INDEX_FUND_DETAIL + fund.getFundId(), getActionBar().getSelectedNavigationIndex());
			
			return true;
		}
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);

		getMenuInflater().inflate(R.menu.menu_fund_detail, menu);
		
		MenuItem filterMenu = menu.findItem(R.id.menu_item_filter);
		MenuItem shareMenu = menu.findItem(R.id.menu_item_share);
		
		if(Constants.ACTIONBAR_LIST_NEWS == getActionBar().getSelectedNavigationIndex()){
			mDrawerLayout.closeDrawer(Gravity.END);
			filterMenu.setVisible(false);
			shareMenu.setVisible(true);
		}
		else{
			filterMenu.setVisible(true);
			
			if(mDrawerLayout.isDrawerOpen(Gravity.END)){
				shareMenu.setVisible(false);
			}
			else{
				shareMenu.setVisible(true);
			}
		}
		
		setupShareProvider(menu, IntentUtil.getShareIntent(getString(R.string.share_app_fund_detail_label, Constants.APP_LINK)));

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		switch (id) {
		case R.id.menu_item_filter:
			if(mDrawerLayout.isDrawerOpen(Gravity.END)){
				mDrawerLayout.closeDrawer(Gravity.END);
			}
			else{
				mDrawerLayout.openDrawer(Gravity.END);
			}
			return true;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void filter(FundFilter setting) {
		mDrawerLayout.closeDrawer(Gravity.END);
		bus.post(new ChangeFundFilterEvent(setting));
	}

	@Override
	public FundObject fetchFundObject() {
		return fund;
	}
	
}
