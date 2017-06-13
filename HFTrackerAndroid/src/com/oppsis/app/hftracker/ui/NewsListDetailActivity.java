package com.oppsis.app.hftracker.ui;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.oppsis.app.hftracker.R;
import com.oppsis.app.hftracker.pojo.FundObject;
import com.oppsis.app.hftracker.pojo.FundObjectFetcher;
import com.oppsis.app.hftracker.ui.base.BaseSwipeAndActionBarActivity;
import com.oppsis.app.hftracker.ui.fragment.FundDetailNewsFragment;
import com.oppsis.app.hftracker.ui.fragment.FundDetailNewsFragment_;
import com.oppsis.app.hftracker.ui.fragment.NewsDetailFragment;
import com.oppsis.app.hftracker.ui.fragment.NewsDetailFragment_;
import com.oppsis.app.hftracker.util.Constants;

@EActivity(R.layout.activity_news_list_detial)
public class NewsListDetailActivity extends BaseSwipeAndActionBarActivity implements FundObjectFetcher{
	
	@Extra protected FundObject fund;
	
	
	@AfterViews
	public void initUI(){
		super.initUI(getString(R.string.fund_detail_menu_news));
		
		FundDetailNewsFragment newsListfragment = 
				FundDetailNewsFragment_.builder().build();
		
		NewsDetailFragment newsDetailfragment = 
				NewsDetailFragment_.builder().build();
		
		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		
		if(fragmentManager.findFragmentByTag(Constants.FUND_NEWS_LIST_FRAGMENT_TAG) != null){
			fragmentTransaction.replace(R.id.news_list_fragment, newsListfragment);
		}
		else{
			fragmentTransaction.add(R.id.news_list_fragment, newsListfragment, Constants.FUND_NEWS_LIST_FRAGMENT_TAG);
		}
		
		if(fragmentManager.findFragmentByTag(Constants.FUND_NEWS_DETAIL_FRAGMENT_TAG) != null){
			fragmentTransaction.replace(R.id.news_detail_fragment, newsDetailfragment);
		}
		else{
			fragmentTransaction.add(R.id.news_detail_fragment, newsDetailfragment, Constants.FUND_NEWS_DETAIL_FRAGMENT_TAG);
		}
		
		
		fragmentTransaction.commit();
	}


	@Override
	public FundObject fetchFundObject() {
		return fund;
	}
}
