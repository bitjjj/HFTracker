package com.oppsis.app.hftracker.ui;

import it.sephiroth.android.library.widget.HListView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.astuetz.PagerSlidingTabStrip;
import com.oppsis.app.hftracker.R;
import com.oppsis.app.hftracker.adapter.AnalysisManagerListAdapter;
import com.oppsis.app.hftracker.adapter.AnalysisPagerAdapter;
import com.oppsis.app.hftracker.api.util.DefaultListCallback;
import com.oppsis.app.hftracker.event.ChangeAnalysisDateEvent;
import com.oppsis.app.hftracker.event.ChangeAnalysisManagersEvent;
import com.oppsis.app.hftracker.event.OttoBus;
import com.oppsis.app.hftracker.model.FundDateList;
import com.oppsis.app.hftracker.ui.base.BaseActivity;
import com.oppsis.app.hftracker.ui.util.ManagerListBarCallback;
import com.oppsis.app.hftracker.util.Constants;
import com.oppsis.app.hftracker.util.FundFuncUtils;
import com.oppsis.app.hftracker.util.IntentUtil;
import com.umeng.analytics.MobclickAgent;

@EActivity(R.layout.activity_analysis)
public class AnalysisActivity extends BaseActivity implements ManagerListBarCallback{

	private final static int SELECT_MANAGERS_RESULT_CODE = 1;
	
	@ViewById(R.id.tabs) protected PagerSlidingTabStrip mTabs;
	@ViewById(R.id.pager) protected ViewPager mViewPager;
	@ViewById(R.id.analysis_fund_manager_list) protected HListView mManagerList;
	@ViewById(R.id.loading_view) protected LinearLayout mLoadingView;
	@ViewById(R.id.analysis_select_date_btn) protected ImageView mCalendarBtn;
	@ViewById(R.id.analysis_edit_manager_btn) protected ImageView mEditManagerBtn;
	@ViewById(R.id.analysis_manager_list_panel) protected LinearLayout mManagerListBar;
	
	@Bean protected OttoBus mBus;

	private DisplayMetrics mDM;
	private AnalysisManagerListAdapter mAdapter;
	private List<String> mResultList;
	private int mSelectedDateIndex = -1;
	private boolean mManagerListBarStatus = true;//true show,false hide;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	
		mDM = getResources().getDisplayMetrics();
		mResultList = new ArrayList<String>();
		
		MobclickAgent.openActivityDurationTrack(false);
	}
	
	@AfterViews
	protected void initUI(){
		getActionBar().setTitle(R.string.menu_hf_analysis);
		
		mViewPager = (ViewPager)findViewById(R.id.pager);

		AnalysisPagerAdapter pagerAdapter = new AnalysisPagerAdapter(getSupportFragmentManager(), this);
		mViewPager.setAdapter(pagerAdapter);
		mViewPager.setOffscreenPageLimit(3);// keep fragment instance both side
		//mViewPager.setPageTransformer(true, new DepthPageTransformer());
		
		mViewPager.setPageMargin(0);

		mTabs.setViewPager(mViewPager);
		mTabs.setOnPageChangeListener(mPageChangeListener);
		setTabsStyle();
		
		mManagerList.setEmptyView(mLoadingView);
		
		mCalendarBtn.setEnabled(false);
	}
	
	OnPageChangeListener mPageChangeListener = new OnPageChangeListener() {

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageSelected(int position) {
			if(position == 0){//portfolio value don't need date
				mCalendarBtn.setEnabled(false);
			}
			else{
				mCalendarBtn.setEnabled(true);
			}
		}

	};
	
	private void setTabsStyle() {
		// tabs.setShouldExpand(true);
		mTabs.setDividerColor(Color.TRANSPARENT);
		mTabs.setUnderlineHeight((int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, 1, mDM));
		mTabs.setIndicatorHeight((int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, 4, mDM));
		if(getResources().getBoolean(R.bool.tablet_land)){
			mTabs.setTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 16, mDM));
		}
		mTabs.setIndicatorColor(getResources().getColor(R.color.tab_selected_strip));
		mTabs.setBackgroundColor(getResources().getColor(R.color.tab_background));

		mTabs.setTextColor(getResources().getColor(R.color.list_dropdown_divider_color));
		mTabs.setSelectedTextColor(Color.WHITE);

		mTabs.setTabBackground(0);
	}
	
	@Click(R.id.analysis_edit_manager_btn)
	protected void selectManagers(){
		Set<String> managers = new HashSet<String>();
		if(mAdapter != null){
			managers.addAll(mAdapter.getSelectedManagers());
		}
		ManagerSelectorActivity_.intent(this).selectedManagers(managers).startForResult(SELECT_MANAGERS_RESULT_CODE);
	}
	
	@Click(R.id.analysis_select_date_btn)
	protected void selectPeriod(){	
		loadData();
	}
	
	@Background
	protected void loadData(){
		
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
						showDateListDlg(choice);
					}
				});
			}
		});
	}
	
	private void showDateListDlg(final CharSequence[] choice){
		
		final String title = getActionBar().getTitle().toString();
		
		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		alert.setTitle(getString(R.string.select_date_title));
		alert.setSingleChoiceItems(choice, mSelectedDateIndex, new DialogInterface.OnClickListener() {
		    @Override
		    public void onClick(DialogInterface dialog, int which) {
		        mSelectedDateIndex = which;
		    }
		});
		alert.setPositiveButton(getString(R.string.sure_label), new DialogInterface.OnClickListener() {
		    @Override
		    public void onClick(DialogInterface dialog, int which) {
		        if(mSelectedDateIndex != -1 && !choice[mSelectedDateIndex].equals(title)){
		        	getActionBar().setTitle(choice[mSelectedDateIndex]);
		        	mBus.post(new ChangeAnalysisDateEvent(choice[mSelectedDateIndex].toString()));
		        }
		    }
		});
		alert.show();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

	    if (requestCode == SELECT_MANAGERS_RESULT_CODE) {
	        if (resultCode == RESULT_OK) {
	        	List<String> selectedManagers = data.getStringArrayListExtra(Constants.MANAGER_SELECT_RESULT);
	        	
	        	if(mAdapter == null){
	        		mResultList.addAll(selectedManagers);
	        		mAdapter = new AnalysisManagerListAdapter(this,R.layout.analysis_manager_list_item,mResultList);
	        		mManagerList.setAdapter(mAdapter);
	        		
	        		mBus.post(new ChangeAnalysisManagersEvent(selectedManagers));
	        	}
	        	else{

		        	if(!FundFuncUtils.isSameManagerList(selectedManagers,mAdapter.getSelectedManagers())){
		        		mResultList.clear();
		        		mResultList.addAll(selectedManagers);
		        		mAdapter.notifyDataSetChanged();
		        		
		        		mBus.post(new ChangeAnalysisManagersEvent(selectedManagers));
		        	}
	        	}
	        }
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
		setupShareProvider(menu, IntentUtil.getShareIntent(getString(R.string.share_app_analysis_label, Constants.APP_LINK)));
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return super.onOptionsItemSelected(item);
	}
	
	public void toggleManagerListBar(){
		if(mManagerListBarStatus){
			animateStatusBar(false);
		}
		else{
			animateStatusBar(true);
		}
		mManagerListBarStatus = !mManagerListBarStatus;
	}
	
	public void hideManagerListBar(){
		if(mManagerListBarStatus)animateStatusBar(false);
		mManagerListBarStatus = false;
	}
	
	public void showManagerListBar(){
		if(!mManagerListBarStatus)animateStatusBar(true);
		mManagerListBarStatus = true;
	}
	
	private void animateStatusBar(final boolean isShow){
		ObjectAnimator anim;
		if(isShow){
			anim = ObjectAnimator.ofFloat(mManagerListBar, View.ALPHA, 0f, 1f);
		}
		else{
			anim = ObjectAnimator.ofFloat(mManagerListBar, View.ALPHA, 1f, 0f);
		}
		anim.addListener(new AnimatorListenerAdapter(){		
			@Override
			public void onAnimationEnd(Animator animation) {
				mCalendarBtn.setClickable(isShow);
				mEditManagerBtn.setClickable(isShow);
			}
		});
		anim.setDuration(Constants.ANIM_MANAGER_BAR_DURATION).start();
	}
	
	protected int getSelfNavDrawerItem() {
		return R.id.navdrawer_item_hf_analysis;
	}


}
