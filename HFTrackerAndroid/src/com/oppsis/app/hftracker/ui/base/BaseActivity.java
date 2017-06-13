package com.oppsis.app.hftracker.ui.base;

import android.app.ActionBar;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ShareActionProvider;
import android.widget.ShareActionProvider.OnShareTargetSelectedListener;
import android.widget.TextView;
import cn.jpush.android.api.JPushInterface;

import com.oppsis.app.hftracker.R;
import com.oppsis.app.hftracker.api.APIFacade;
import com.oppsis.app.hftracker.ui.AnalysisActivity_;
import com.oppsis.app.hftracker.ui.ContactActivity_;
import com.oppsis.app.hftracker.ui.MainActivity_;
import com.oppsis.app.hftracker.ui.RankActivity_;
import com.oppsis.app.hftracker.ui.SPXDPSActivity_;
import com.oppsis.app.hftracker.ui.SPXEPSActivity_;
import com.oppsis.app.hftracker.ui.SPXPEActivity_;
import com.oppsis.app.hftracker.ui.SPXShillerPEActivity_;
import com.oppsis.app.hftracker.ui.SettingActivity_;
import com.oppsis.app.hftracker.ui.StatementActivity_;
import com.oppsis.app.hftracker.util.LPreviewUtils;
import com.oppsis.app.hftracker.util.LPreviewUtilsBase;
import com.umeng.analytics.MobclickAgent;

public abstract class BaseActivity extends FragmentActivity {

	private DrawerLayout mDrawerLayout;
	private LPreviewUtilsBase.ActionBarDrawerToggleWrapper mDrawerToggle;
	private LPreviewUtilsBase mLPreviewUtils;

	protected static final int NAVDRAWER_LAUNCH_DELAY = 250;
	protected static final int NAVDRAWER_ITEM_INVALID = -1;
	protected static final int[] NAVDRAWER_ITEMS = new int[] {
			R.id.navdrawer_item_hf_list, R.id.navdrawer_item_hf_stock_rank,R.id.navdrawer_item_hf_analysis, 
			R.id.navdrawer_item_spx_pe,R.id.navdrawer_item_spx_shiller_pe,R.id.navdrawer_item_spx_eps,R.id.navdrawer_item_spx_dps };
	private static final String TAG_ICON = "icon";
	private static final String TAG_TITLE = "title";
	protected Handler mMainHandler;
	protected APIFacade mAPIFacade;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mLPreviewUtils = LPreviewUtils.getInstance(this);

		mMainHandler = new Handler();
		mAPIFacade = new APIFacade(this.getApplicationContext());
		
		ActionBar ab = getActionBar();
		if (ab != null) {
			ab.setDisplayHomeAsUpEnabled(true);
		}

	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		setupNavDrawer();
		
		for (int i = 0; i < NAVDRAWER_ITEMS.length; i++) {
			final int itemId = NAVDRAWER_ITEMS[i];
			View view = mDrawerLayout.findViewById(itemId);
			view.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					onNavDrawerItemClicked(itemId);
				}
			});
		}
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		if (mDrawerToggle != null) {
			mDrawerToggle.onConfigurationChanged(newConfig);
		}
	}
	
	 @Override
	    public boolean onCreateOptionsMenu(Menu menu) {
		 	
	        return super.onCreateOptionsMenu(menu);
	    }

	    @Override
	    public boolean onOptionsItemSelected(MenuItem item) {
	        int id = item.getItemId();
	        if (mDrawerToggle != null && mDrawerToggle.onOptionsItemSelected(item)) {
	            return true;
	        }
			switch(id){
				case R.id.menu_item_contactus:
					ContactActivity_.intent(this).start();
					overridePendingTransition(R.anim.push_left_in,R.anim.no_anim);
					return true;
				case R.id.menu_item_notice:
					StatementActivity_.intent(this).start();
					overridePendingTransition(R.anim.push_left_in,R.anim.no_anim);
					return true;
				case R.id.menu_item_settings:
					SettingActivity_.intent(this).start();
					overridePendingTransition(R.anim.push_left_in,R.anim.no_anim);
					return true;
			}
	        return super.onOptionsItemSelected(item);
	    }
	    
	    protected void setupShareProvider(Menu menu,Intent shareIntent){
	    	MenuItem item = menu.findItem(R.id.menu_item_share);
		 	if(item != null){
		 		ShareActionProvider provider = (ShareActionProvider) item.getActionProvider();
		 		provider.setShareIntent(shareIntent);
				OnShareTargetSelectedListener listener = new OnShareTargetSelectedListener() {
					public boolean onShareTargetSelected(ShareActionProvider source,
							Intent intent) {
						startActivity(Intent.createChooser(intent,getResources().getText(R.string.share_label)));
						return true;
					}
				};
				provider.setOnShareTargetSelectedListener(listener);
		 	}
	    }

	private void setupNavDrawer() {
		// What nav drawer item should be selected?
		int selfItem = getSelfNavDrawerItem();

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		if (mDrawerLayout == null) {
			return;
		}
		if (selfItem == NAVDRAWER_ITEM_INVALID) {
			// do not show a nav drawer
			View navDrawer = mDrawerLayout.findViewById(R.id.navdrawer);
			if (navDrawer != null) {
				((ViewGroup) navDrawer.getParent()).removeView(navDrawer);
			}
			mDrawerLayout = null;
			return;
		}

		mDrawerToggle = mLPreviewUtils.setupDrawerToggle(mDrawerLayout,
				new DrawerLayout.DrawerListener() {
					@Override
					public void onDrawerClosed(View drawerView) {
						invalidateOptionsMenu();
					}

					@Override
					public void onDrawerOpened(View drawerView) {
						invalidateOptionsMenu();
					}

					@Override
					public void onDrawerStateChanged(int newState) {
						invalidateOptionsMenu();
					}

					@Override
					public void onDrawerSlide(View drawerView, float slideOffset) {
						onNavDrawerSlide(slideOffset);
					}
				});
		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, Gravity.START);

		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);

		// populate the nav drawer with the correct items
		resetNavDrawerItem(getSelfNavDrawerItem());

		mDrawerToggle.syncState();

	}

	private void resetNavDrawerItem(int selectedItemId) {

		for (int i = 0; i < NAVDRAWER_ITEMS.length; i++) {
			View view = mDrawerLayout.findViewById(NAVDRAWER_ITEMS[i]);
			formatNavDrawerItem(view, false);
		}

		formatNavDrawerItem(findViewById(selectedItemId), true);
	}

	private void formatNavDrawerItem(View view, boolean selected) {
		ImageView iconView = (ImageView) view.findViewWithTag(TAG_ICON);
		TextView titleView = (TextView) view.findViewWithTag(TAG_TITLE);

		titleView.setTextColor(selected ? getResources().getColor(
				R.color.navdrawer_text_color_selected) : getResources()
				.getColor(R.color.navdrawer_text_color));
		iconView.setColorFilter(selected ? getResources().getColor(
				R.color.navdrawer_icon_tint_selected) : getResources()
				.getColor(R.color.navdrawer_icon_tint));
	}

	protected boolean isNavDrawerOpen() {
		return mDrawerLayout != null
				&& mDrawerLayout.isDrawerOpen(Gravity.START);
	}

	protected void onNavDrawerSlide(float offset) {
	}

	protected int getSelfNavDrawerItem() {
		return NAVDRAWER_ITEM_INVALID;
	}

	public LPreviewUtilsBase getLPreviewUtils() {
		return mLPreviewUtils;
	}

	private void onNavDrawerItemClicked(final int itemId) {
		if (itemId == getSelfNavDrawerItem()) {
			mDrawerLayout.closeDrawer(Gravity.START);
			return;
		}

		mMainHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				goToNavDrawerItem(itemId);
			}
		}, NAVDRAWER_LAUNCH_DELAY);

		resetNavDrawerItem(itemId);

		mDrawerLayout.closeDrawer(Gravity.START);
	}

	private void goToNavDrawerItem(int item) {
		switch (item) {
		case R.id.navdrawer_item_hf_list:
			MainActivity_.intent(this).start();
			finish();
			break;
		case R.id.navdrawer_item_hf_stock_rank:
			RankActivity_.intent(this).start();
			finish();
			break;
		case R.id.navdrawer_item_hf_analysis:
			AnalysisActivity_.intent(this).start();
			finish();
			break;
		case R.id.navdrawer_item_spx_pe:
			SPXPEActivity_.intent(this).start();
			finish();
			break;
		case R.id.navdrawer_item_spx_shiller_pe:
			SPXShillerPEActivity_.intent(this).start();
			finish();
			break;
		case R.id.navdrawer_item_spx_eps:
			SPXEPSActivity_.intent(this).start();
			finish();
			break;
		case R.id.navdrawer_item_spx_dps:
			SPXDPSActivity_.intent(this).start();
			finish();
			break;
		}
	}

	public void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
		JPushInterface.onResume(this);
	}
	
	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
		JPushInterface.onPause(this);
	}
	
}
