package com.oppsis.app.hftracker.ui.fragment;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Toast;

import com.oppsis.app.hftracker.R;
import com.oppsis.app.hftracker.api.util.DefaultObjectCallback;
import com.oppsis.app.hftracker.event.ChangeFundNewsEvent;
import com.oppsis.app.hftracker.event.OttoBus;
import com.oppsis.app.hftracker.model.FundNews;
import com.oppsis.app.hftracker.pojo.NewsContentObject;
import com.oppsis.app.hftracker.ui.base.BaseFragment;
import com.squareup.otto.Subscribe;

@EFragment(R.layout.fragment_news_detail)
public class NewsDetailFragment extends BaseFragment{

	@ViewById(R.id.news_content) protected WebView mWebView;
	@ViewById(R.id.loading_view) protected View mLoadingView;
	@Bean protected OttoBus mBus;
	
	private FundNews mFundNews;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            mBus.register(this);  
    }
	
	@Subscribe
	public void onChangeFundNews(ChangeFundNewsEvent event) {
		mFundNews = event.getFundNews();
		mWebView.loadUrl("about:blank");
		mLoadingView.setVisibility(View.VISIBLE);
		loadData();
    }
	
	@Background
	protected void loadData(){
		mAPIFacade.getNewsContent(mFundNews, new DefaultObjectCallback<NewsContentObject>(getActivity()) {
			@Override
			public void onSuccess(final NewsContentObject result) {
				mMainHandler.post(new Runnable() {
					@Override
					public void run() {
						mLoadingView.setVisibility(View.GONE);
						if(result != null){
							mWebView.loadUrl(result.getUrl());
						}
						else{
							Toast.makeText(getActivity(), getString(R.string.unknown_error_tip), Toast.LENGTH_SHORT).show();
						}
						
					}
				});
			}
		});
	}
	
	@Override
    public void onDestroy() {
            super.onDestroy();
            mBus.unregister(this);
    }
	
}
