package com.oppsis.app.hftracker.ui.base;

import me.imid.swipebacklayout.lib.SwipeBackLayout;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ShareActionProvider;
import android.widget.ShareActionProvider.OnShareTargetSelectedListener;
import cn.jpush.android.api.JPushInterface;

import com.oppsis.app.hftracker.api.APIFacade;
import com.oppsis.app.lib.R;
import com.umeng.analytics.MobclickAgent;

public class BaseSwipeAndActionBarActivity extends SwipeBackActivity{

	protected Handler mMainHandler;
	protected APIFacade mAPIFacade;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mMainHandler = new Handler();
		mAPIFacade = new APIFacade(this.getApplicationContext());
	}
	
	public void initUI(){
		getActionBar().setTitle("");
		initUI(null);
	}
	
	public void initUI(String title){
		if(title != null && !title.equals("")){
			getActionBar().setTitle(title);
		}
		getActionBar().setHomeButtonEnabled(true);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getSwipeBackLayout().setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);
	}
	
	public void initUI(String title,int iconResId){
		initUI(title);
		getActionBar().setIcon(iconResId);
	}
	
	protected void setupShareProvider(Menu menu,Intent shareIntent){
    	MenuItem item = menu.findItem(com.oppsis.app.hftracker.R.id.menu_item_share);
	 	if(item != null){
	 		ShareActionProvider provider = (ShareActionProvider) item.getActionProvider();
	 		provider.setShareIntent(shareIntent);
			OnShareTargetSelectedListener listener = new OnShareTargetSelectedListener() {
				public boolean onShareTargetSelected(ShareActionProvider source,
						Intent intent) {
					startActivity(Intent.createChooser(intent,getResources().getText(com.oppsis.app.hftracker.R.string.share_label)));
					return true;
				}
			};
			provider.setOnShareTargetSelectedListener(listener);
	 	}
    }
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			
			
			// Create an intent for the parent Activity
			Intent upIntent = NavUtils.getParentActivityIntent(this);
			// Check if we need to create the entire stack
			if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
				// This stack doesn't exist yet, so it must be synthesized
				TaskStackBuilder.create(this).addParentStack(this)
						.startActivities();
			} else {
				// Stack exists, so just navigate up
				//NavUtils.navigateUpFromSameTask(this);
				finish();
				overridePendingTransition(R.anim.no_anim,R.anim.push_left_out);
			}
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
		JPushInterface.onResume(this);
	}

	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
		JPushInterface.onPause(this);
	}
	
}
