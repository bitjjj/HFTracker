package com.oppsis.app.hftracker.ui;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.WindowFeature;

import android.annotation.SuppressLint;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.oppsis.app.hftracker.R;
import com.oppsis.app.hftracker.model.FundHolding;
import com.oppsis.app.hftracker.ui.base.BaseSwipeAndActionBarActivity;

@Deprecated
@WindowFeature({ Window.FEATURE_PROGRESS })
@EActivity(R.layout.activity_webview)
public class WebViewActivity extends BaseSwipeAndActionBarActivity {
	
	@SuppressWarnings("unused")
	private final String TAG = "WebViewActivity";

	@ViewById(R.id.webView)protected WebView webView;

	@Extra("fundHolding")protected FundHolding fundHolding;

	@SuppressLint("SetJavaScriptEnabled")
	@AfterViews
	public void initUI() {
		super.initUI(fundHolding.getSecurity());

		webView.getSettings().setJavaScriptEnabled(true); 
		
		webView.getSettings().setSupportZoom(true);  

		webView.setWebViewClient(new MyWebViewClient());
		webView.setWebChromeClient(new MyChromeWebClient());
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_refresh, menu);

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

			WebViewActivity.this.setTitle(getString(R.string.loading_label));
			WebViewActivity.this.setProgress(progress * 100);

			if (progress == 100){
				WebViewActivity.this.setTitle(fundHolding.getSecurity());
			}
		}
	}


}
