package com.oppsis.app.hftracker.api;

import java.io.IOException;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import org.stringtemplate.v4.ST;

import android.content.Context;

import com.oppsis.app.hftracker.api.APIManager.ListCallback;
import com.oppsis.app.hftracker.api.APIManager.ObjectCallback;
import com.oppsis.app.hftracker.model.FundDateList;
import com.oppsis.app.hftracker.model.FundHolding;
import com.oppsis.app.hftracker.model.FundAnalysis;
import com.oppsis.app.hftracker.model.FundIndustry;
import com.oppsis.app.hftracker.model.FundNews;
import com.oppsis.app.hftracker.pojo.FundObject;
import com.oppsis.app.hftracker.pojo.NewsContentObject;
import com.oppsis.app.hftracker.pojo.SPXData;
import com.oppsis.app.hftracker.ui.widget.FilterPanelView.FundFilter;
import com.oppsis.app.hftracker.util.Constants;
import com.oppsis.app.util.FuncUtils;
import com.oppsis.app.util.UrlUtils;

public class APIFacade {

	private APIManager apiManager;
	private static JSONObject urls;
	
	static{
		try {
			urls = new JSONObject(FuncUtils.convertStreamToString(APIFacade.class.getResourceAsStream("/com/oppsis/app/hftracker/api/api.json")));
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @param must application context
	 */
	public APIFacade(Context context){
		
		apiManager = APIManager.getInstance(context);
	}
	
	//TODO:[Next Release]replace async to sync. attention:caller need in background thread
	public void getDateList(FundObject fund,ListCallback<FundDateList> callback){
		ST url = new ST(urls.optString("dateList"));
		url.add("url", APIUrls.getDateListUrl());
		url.add("name", UrlUtils.encode(fund.getName()));
		apiManager.listAsync(url.render(), FundDateList.class,callback);
	}
	
	public void getDateLists(List<String> nameList,ListCallback<FundDateList> callback){
		String[] names = new String[nameList.size()];
		for (int i = 0; i < nameList.size(); i++) {
			names[i] = UrlUtils.encode(nameList.get(i));
		}
		ST url = new ST(urls.optString("dateLists"));
		url.add("url", APIUrls.getDateListsUrl());
		url.add("names", names);
		apiManager.listSync(url.render(), FundDateList.class,callback);
	}
	
	public void getDateListAll(ListCallback<FundDateList> callback){
		ST url = new ST(urls.optString("dateList"));
		url.add("url", APIUrls.getDateListAllUrl());
		apiManager.listSync(url.render(), FundDateList.class,callback);
	}
	
	public void getIndustryList(FundObject fund,FundFilter filter,ListCallback<FundIndustry> callback){
		ST url;

		if(filter == null){
			url = new ST(urls.optString("industryList"));
			url.add("url", APIUrls.getIndustryListDefaultUrl());
		}
		else{
			url = new ST(urls.optString("industryListWithFilter"));
			url.add("url",  APIUrls.getIndustryListUrl());
			url.add("date", filter.getDateStr());
		}
		url.add("name", UrlUtils.encode(fund.getName()));
		apiManager.listSync(url.render(), FundIndustry.class,callback);
	}
	
	public void getIndustryLists(List<String> nameList,String date,ListCallback<FundIndustry> callback){
		String[] names = new String[nameList.size()];
		for (int i = 0; i < nameList.size(); i++) {
			names[i] = UrlUtils.encode(nameList.get(i));
		}
		ST url = new ST(urls.optString("industryLists"));
		url.add("url",  APIUrls.getIndustryListsUrl());
		url.add("date", date);
		url.add("names", names);
		
		
		apiManager.listSync(url.render(), FundIndustry.class,callback);
	}
	
	public void getHoldingList(FundObject fund,FundFilter filter,int page,ListCallback<FundHolding> callback){
		ST url;
		if(filter == null){
			url = new ST(urls.optString("holdingList"));
			url.add("url", APIUrls.getHoldingListDefaultUrl());
		}
		else{
			url = new ST(urls.optString("holdingListWithFilter"));
			url.add("url", APIUrls.getHoldingListUrl());
			url.add("date", filter.getDateStr());
			url.add("sort", filter.getSort());
			url.add("desc", filter.getDirection());
			url.add("securityType", UrlUtils.encode(filter.getSecurityTypes()));
		}
		url.add("name", UrlUtils.encode(fund.getName()));
		url.add("page", page);
		
		apiManager.listSync(url.render(), FundHolding.class,callback);
	}
	
	public void getHoldingMutualList(FundHolding holding,String date,ListCallback<FundHolding> callback){
		ST url = new ST(urls.optString("holdingMutualList"));
		url.add("url", APIUrls.getHoldingMutualUrl());
		url.add("security", UrlUtils.encode(holding.getSecurity()));
		url.add("securityType",  holding.getSecurityType());
		url.add("ticker", holding.getTicker());
		url.add("date", date);
		
		apiManager.listSync(url.render(), FundHolding.class, callback);
	}
	
	public void getHoldingHistoryList(FundHolding holding,ListCallback<FundHolding> callback){
		ST url = new ST(urls.optString("holdingHistoryList"));
		url.add("url", APIUrls.getHoldingHistoryUrl());
		url.add("security", UrlUtils.encode(holding.getSecurity()));
		url.add("securityType",  holding.getSecurityType());
		url.add("ticker", holding.getTicker());
		url.add("name", UrlUtils.encode(holding.getFund_name()));
		apiManager.listSync(url.render(), FundHolding.class, callback);
	}
	
	public void getHoldingComparatorList(FundHolding holding,String[] names,ListCallback<FundHolding> callback){
		for (int i = 0; i < names.length; i++) {
			names[i] = UrlUtils.encode(names[i]);
		}
		
		ST url = new ST(urls.optString("holdingComparatorList"));
		url.add("url", APIUrls.getHoldingComparatorUrl());
		url.add("security", UrlUtils.encode(holding.getSecurity()));
		url.add("securityType",  holding.getSecurityType());
		url.add("ticker", holding.getTicker());
		url.add("names", names);
		
		apiManager.listSync(url.render(), FundHolding.class, callback);
	}
	
	public void getHoldingSearchList(String ticker,int page,ListCallback<FundHolding> callback){
		ST url = new ST(urls.optString("holdingSearchList"));
		url.add("url", APIUrls.getHoldingSearchUrl());
		url.add("page", page);
		url.add("ticker", ticker);
		
		apiManager.listSync(url.render(), FundHolding.class, callback);
	}
	
	public void getNewsList(FundObject fund,int page,ListCallback<FundNews> callback){
		ST url;	
		if(page == Constants.PAGE_ZERO){
			url = new ST(urls.optString("newsListFirstPage"));
			url.add("expiry", Constants.HALF_HOUR_SECONDS);
		}
		else{
			url = new ST(urls.optString("newsList"));
		}
		url.add("url", APIUrls.getNewsListUrl());
		url.add("name", UrlUtils.encode(fund.getName()));
		url.add("page", page);
		
		
		apiManager.listSync(url.render(), FundNews.class,callback);
	}
	
	public void refreshNewsList(FundObject fund,ListCallback<FundNews> callback){
		ST url = new ST(urls.optString("refreshNewsList"));
		url.add("url", APIUrls.getNewsListUrl());
		url.add("expiry",  Constants.HALF_HOUR_SECONDS);

		url.add("name", UrlUtils.encode(fund.getName()));
		apiManager.listSync(url.render(), FundNews.class,callback);
	}
	
	
	public void getNewsContent(FundNews news,ObjectCallback<NewsContentObject> callback){
		
		ST url = new ST(urls.optString("newsContent"));
		url.add("url", APIUrls.getNewsContentUrl());
		url.add("id", news.getId());
		url.add("name", UrlUtils.encode(news.getFund_name()));
		url.add("newsUrl", UrlUtils.encode(news.getUrl()));
		url.add("source", FundNews.SOURCE_INSIDERMONKEY);
		
		apiManager.objectAsync(url.render(), NewsContentObject.class,callback);
	}
	
	public void getAnalysisMutualList(List<String> nameList,String date,int page,ListCallback<FundAnalysis> callback){
		String[] names = new String[nameList.size()];
		for (int i = 0; i < nameList.size(); i++) {
			names[i] = UrlUtils.encode(nameList.get(i));
		}
		ST url = new ST(urls.optString("analysisMutual"));
		url.add("url",  APIUrls.getAnalysisMutualUrl());
		url.add("date", date);
		url.add("page", page);
		url.add("names", names);
		
		
		apiManager.listSync(url.render(), FundAnalysis.class,callback);
	}
	
	public void getRankList(String date,String securityType,int page,ListCallback<FundAnalysis> callback){

		ST url = new ST(urls.optString("analysisRank"));
		url.add("url",  APIUrls.getRankUrl());
		url.add("date", date);
		url.add("page", page);
		url.add("securityType", securityType);
		
		
		apiManager.listSync(url.render(), FundAnalysis.class,callback);
	}
	
	public void getSPXPE(ListCallback<SPXData> callback){
		apiManager.listSync(APIUrls.getSPXPEUrl(), SPXData.class,callback);
	}
	
	public void getSPXShillerPE(ListCallback<SPXData> callback){
		apiManager.listSync(APIUrls.getSPXShillerPEUrl(), SPXData.class,callback);
	}
	
	public void getSPXEPS(ListCallback<SPXData> callback){
		apiManager.listSync(APIUrls.getSPXEPSUrl(), SPXData.class,callback);
	}
	
	public void getSPXDPS(ListCallback<SPXData> callback){
		apiManager.listSync(APIUrls.getSPXDPSUrl(), SPXData.class,callback);
	}
	
}
