package com.oppsis.app.hftracker.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.oppsis.app.hftracker.R;
import com.oppsis.app.hftracker.ui.base.BaseActivity;
import com.oppsis.app.hftracker.util.Constants;
import com.oppsis.app.hftracker.util.IntentUtil;

public class SPXBaseActivity extends BaseActivity {
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		getActionBar().setTitle(R.string.app_name);
		
		mMainHandler.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				loadData();
			}
		}, NAVDRAWER_LAUNCH_DELAY);
		
	}
	
	protected int getActionBarTitleRes(){
		throw new UnsupportedOperationException("You must implement this method in subclass");
	}
	
	protected int getSPXInfoRes(){
		throw new UnsupportedOperationException("You must implement this method in subclass");
	}
	
	protected void loadData(){
		throw new UnsupportedOperationException("You must implement this method in subclass");
	}
	
	protected int getShareTitle(){
		throw new UnsupportedOperationException("You must implement this method in subclass");
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);

		if (getLPreviewUtils().shouldChangeActionBarForDrawer()
				&& isNavDrawerOpen()) {
			return true;
		}

		getMenuInflater().inflate(R.menu.menu_spx, menu);
		
		setupShareProvider(menu, IntentUtil.getShareIntent(getString(getShareTitle(), Constants.APP_LINK)));
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		int id = item.getItemId();
		switch (id) {
		case R.id.menu_item_spx_info:
			showSPXInfoDlg();
			return true;

		default:
			break;
		}

		return super.onOptionsItemSelected(item);
	}
	
	private void showSPXInfoDlg(){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(getString(getActionBarTitleRes()));
		builder.setMessage(getString(getSPXInfoRes()));
		builder.setCancelable(true);
		builder.setNeutralButton(android.R.string.ok,
		        new DialogInterface.OnClickListener() {
		    public void onClick(DialogInterface dialog, int id) {
		        dialog.cancel();
		    }
		});

		AlertDialog alertDlg = builder.create();
		alertDlg.show();
	}

}
