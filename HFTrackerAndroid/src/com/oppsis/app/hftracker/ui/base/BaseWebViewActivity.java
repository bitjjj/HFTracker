package com.oppsis.app.hftracker.ui.base;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.oppsis.app.hftracker.R;

public class BaseWebViewActivity extends BaseSwipeAndActionBarActivity{
	
	protected WebView webView;
	
	@SuppressLint("SetJavaScriptEnabled")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_PROGRESS);
	    super.onCreate(savedInstanceState);

	    setContentView(R.layout.activity_webview);

	    webView = (WebView) findViewById(R.id.webView);
	    
	    webView.getSettings().setJavaScriptEnabled(true); 
		
		webView.getSettings().setSupportZoom(true);  

		webView.setWebViewClient(new MyWebViewClient());
		webView.setWebChromeClient(new MyChromeWebClient());
	}
	
	protected String getActivityTitle(){
		return getString(R.string.app_name);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_fund_news, menu);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {

			case R.id.menu_item_refresh:
				webView.reload();
				return true;

		}

		return super.onOptionsItemSelected(item);
	}

	class MyWebViewClient extends WebViewClient {
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url);
			return true;
		}
	}

	class MyChromeWebClient extends WebChromeClient {
		public void onProgressChanged(WebView view, int progress) {

			BaseWebViewActivity.this.setTitle(getString(R.string.loading_label));
			BaseWebViewActivity.this.setProgress(progress * 100);

			if (progress == 100){
				BaseWebViewActivity.this.setTitle(getActivityTitle());
			}
		}
	}
}
