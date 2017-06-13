package com.oppsis.app.hftracker.ui;

import android.os.Bundle;
import android.view.Menu;
import android.widget.Toast;

import com.oppsis.app.hftracker.R;
import com.oppsis.app.hftracker.api.util.DefaultObjectCallback;
import com.oppsis.app.hftracker.model.FundNews;
import com.oppsis.app.hftracker.pojo.FundLocalInfoManager;
import com.oppsis.app.hftracker.pojo.FundObject;
import com.oppsis.app.hftracker.pojo.NewsContentObject;
import com.oppsis.app.hftracker.ui.base.BaseWebViewActivity;
import com.oppsis.app.hftracker.util.Constants;
import com.oppsis.app.hftracker.util.IntentUtil;

public class NewsDetailActivity extends BaseWebViewActivity{

	private FundNews mFundNews;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    
	    mFundNews = getIntent().getParcelableExtra("data");

	    super.initUI(getActivityTitle(),Constants.FUND_MANAGER_ICONS.get(FundLocalInfoManager.getFundProperty(this, mFundNews.getFund_name(),FundObject.PROPERTY_FUND_ID)));
	    
	    loadUrl();
	}
	
	@Override
	public String getActivityTitle() {
		return getString(R.string.app_name);
	}

	public void loadUrl() {
		mAPIFacade.getNewsContent(mFundNews, new DefaultObjectCallback<NewsContentObject>(this) {
			@Override
			public void onSuccess(NewsContentObject result) {
				if(result != null){
					webView.loadUrl(result.getUrl());
				}
				else{
					Toast.makeText(NewsDetailActivity.this, getString(R.string.unknown_error_tip), Toast.LENGTH_SHORT).show();
				}
			}
		});
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		
		setupShareProvider(menu, IntentUtil.getShareIntent(getString(R.string.share_app_news_detail_label, mFundNews.getTitle(),Constants.APP_LINK)));
		
		return true;
	}
	
}
