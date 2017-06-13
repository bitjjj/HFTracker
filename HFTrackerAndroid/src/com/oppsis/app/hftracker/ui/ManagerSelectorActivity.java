package com.oppsis.app.hftracker.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ListView;
import cn.jpush.android.api.JPushInterface;

import com.oppsis.app.hftracker.R;
import com.oppsis.app.hftracker.adapter.ManagerListAdapter;
import com.oppsis.app.hftracker.pojo.FundLocalInfoManager;
import com.oppsis.app.hftracker.pojo.FundObject;
import com.oppsis.app.hftracker.util.Constants;
import com.oppsis.app.ui.DoneDiscardActivity;
import com.umeng.analytics.MobclickAgent;

@EActivity(R.layout.activity_manager_selector)
public class ManagerSelectorActivity extends DoneDiscardActivity{

	@ViewById(R.id.manager_list)ListView mManagerList;
	@ViewById(R.id.loading_view) protected View mLoadingView;
	
	@Extra
	protected Set<String> selectedManagers;
	
	private ManagerListAdapter mAdapter; 
	private Handler mMainHandler = new Handler();
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@AfterViews
	protected void initUI(){
		mManagerList.setEmptyView(mLoadingView);
		
		loadData();
	}
	
	@Background
	protected void loadData(){
		final List<FundObject> list = FundLocalInfoManager.getFundList(this);
		mMainHandler.post(new Runnable() {
			
			@Override
			public void run() {
				mAdapter = new ManagerListAdapter(ManagerSelectorActivity.this, list,selectedManagers);
				mManagerList.setAdapter(mAdapter);
				
			}
		});
	}
	
	@Override
	public void done(){
		Intent i = new Intent();
		i.putStringArrayListExtra(Constants.MANAGER_SELECT_RESULT, new ArrayList<String>(mAdapter.getSelectedManagers()));
		setResult(RESULT_OK, i);
	}
	
	@Override
	public void discard(){
		setResult(RESULT_CANCELED);
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
