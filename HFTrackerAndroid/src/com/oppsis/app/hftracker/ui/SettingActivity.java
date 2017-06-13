package com.oppsis.app.hftracker.ui;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import br.com.dina.ui.model.ViewItem;
import br.com.dina.ui.widget.UITableView;
import br.com.dina.ui.widget.UITableView.ClickListener;

import com.oppsis.app.hftracker.R;
import com.oppsis.app.hftracker.ui.base.BaseSwipeAndActionBarActivity;
import com.oppsis.app.hftracker.util.Constants;
import com.oppsis.app.util.CacheUtils;
import com.oppsis.app.util.SharedPrefsUtils;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UmengUpdateListener;
import com.umeng.update.UpdateResponse;
import com.umeng.update.UpdateStatus;

@EActivity(R.layout.activity_setting)
public class SettingActivity extends BaseSwipeAndActionBarActivity{

	@ViewById(R.id.setting_table_view)
	protected UITableView tableView;
	private TextView mVersion,mCache;
	private Switch mMessage;
	
	private CacheUtils mCacheManager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mCacheManager = new CacheUtils(this);
		
		UmengUpdateAgent.setUpdateAutoPopup(false);
		UmengUpdateAgent.setUpdateListener(new UmengUpdateListener() {
		    @Override
		    public void onUpdateReturned(int updateStatus,UpdateResponse updateInfo) {
		        switch (updateStatus) {
		        case UpdateStatus.Yes:
		            UmengUpdateAgent.showUpdateDialog(SettingActivity.this, updateInfo);
		            break;
		        case UpdateStatus.No: 
		            Toast.makeText(SettingActivity.this, getString(R.string.setting_update_no_update), Toast.LENGTH_SHORT).show();
		            break;
		        case UpdateStatus.NoneWifi: 
		            Toast.makeText(SettingActivity.this, getString(R.string.setting_update_no_wifi), Toast.LENGTH_SHORT).show();
		            break;
		        case UpdateStatus.Timeout:
		            Toast.makeText(SettingActivity.this, getString(R.string.setting_update_timeout), Toast.LENGTH_SHORT).show();
		            break;
		        }
		    }
		});
	}

	@AfterViews
	public void initUI(){
		super.initUI(getString(R.string.setting_label));

		initTableView();
		
		try {
			String versionName = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
			mVersion.setText(Constants.VERSION_PREFIX + versionName);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		
		mMessage.setChecked(SharedPrefsUtils.getBoolean(this, Constants.PUSH_MESSAGE_ENABLED));
		computeCacheSize();
		
	}
	
	private void initTableView(){
		tableView.setClickListener(new ItemClickListener());
		LayoutInflater mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		RelativeLayout view = (RelativeLayout) mInflater.inflate(R.layout.setting_item_message, null);
		mMessage = (Switch)view.findViewById(R.id.setting_push_message);
		mMessage.setOnCheckedChangeListener(new OnCheckedChangeListener() {			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				SharedPrefsUtils.save(SettingActivity.this, Constants.PUSH_MESSAGE_ENABLED, isChecked);
			}
		});
		ViewItem viewItem = new ViewItem(view);
		viewItem.setClickable(false);
		tableView.addViewItem(viewItem);
		
		view = (RelativeLayout) mInflater.inflate(R.layout.setting_item_version, null);
		mVersion = (TextView)view.findViewById(R.id.setting_check_version);
		viewItem = new ViewItem(view);
		tableView.addViewItem(viewItem);
		
		view = (RelativeLayout) mInflater.inflate(R.layout.setting_item_cache, null);
		mCache = (TextView)view.findViewById(R.id.setting_clear_cache);
		viewItem = new ViewItem(view);
		tableView.addViewItem(viewItem);
		
		tableView.commit();
	}
	
	private class ItemClickListener implements ClickListener {

		@Override
		public void onClick(int index) {
			if (index == 0) {
				
			} else if (index == 1) {
				UmengUpdateAgent.forceUpdate(SettingActivity.this);
			} else if (index == 2) {
				clearCache();
			}

		}

	}
	
	@Background
	protected void computeCacheSize(){
		
		String size = mCacheManager.getCacheSize();
		showCacheSize(size,false);
	}
	
	@Background
	protected void clearCache(){
		mCacheManager.clearCache();
		showCacheSize(CacheUtils.CACHE_SIZE_ZERO,true);
		
	}
	
	@UiThread
	protected void showCacheSize(String size,boolean isClear){
		mCache.setText(size);
		if(isClear){
			Toast.makeText(this, R.string.setting_cache_ok, Toast.LENGTH_SHORT).show();
		}
	}
}
