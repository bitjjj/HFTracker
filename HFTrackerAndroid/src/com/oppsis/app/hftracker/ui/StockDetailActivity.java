package com.oppsis.app.hftracker.ui;

import android.os.Bundle;
import android.view.Menu;

import com.oppsis.app.hftracker.R;
import com.oppsis.app.hftracker.pojo.FundLocalInfoManager;
import com.oppsis.app.hftracker.pojo.FundObject;
import com.oppsis.app.hftracker.pojo.SecurityDataObject;
import com.oppsis.app.hftracker.ui.base.BaseWebViewActivity;
import com.oppsis.app.hftracker.util.Constants;
import com.oppsis.app.hftracker.util.I18nTextUtils;
import com.oppsis.app.hftracker.util.IntentUtil;

public class StockDetailActivity extends BaseWebViewActivity{

	private SecurityDataObject mSecurityData;

	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    
	    mSecurityData = getIntent().getParcelableExtra("data");
	    
	    if(mSecurityData.getIcon() == SecurityDataObject.ICON_HIDE){
	    	super.initUI(getActivityTitle());
	    }
	    else{
	    	super.initUI(getActivityTitle(),Constants.FUND_MANAGER_ICONS.get(FundLocalInfoManager.getFundProperty(this, mSecurityData.getFund_name(),FundObject.PROPERTY_FUND_ID)));
	    }
	    
	    loadUrl();
	}
	
	@Override
	public String getActivityTitle() {
		return mSecurityData.getSecurity();
	}

	public void loadUrl() {
		webView.loadUrl(String.format(I18nTextUtils.getStockUrl(this), mSecurityData.getTicker()));
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		
		setupShareProvider(menu, IntentUtil.getShareIntent(getString(R.string.share_app_stock_detail_label, mSecurityData.getSecurity(),Constants.APP_LINK)));
		
		return true;
	}

}
