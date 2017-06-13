package com.oppsis.app.hftracker.api;

public class APIUrls {

	static{
		//System.loadLibrary("jin10");
	}

	private final static String API_BASE_URL = "/HFTrackerRESTAPI/rest/v1";
	
	private final static String API_DATE_LIST_ALL = "/datelist";
	private final static String API_DATE_LIST = "/datelist/fund";
	private final static String API_DATE_LISTS = "/datelist/funds";
	
	private final static String API_INDUSTRY_LIST_DEFAULT = "/industry/fund/default";
	private final static String API_INDUSTRY_LIST = "/industry/fund";
	private final static String API_INDUSTRY_LISTS = "/industry/funds";
	
	private final static String API_HOLDING_LIST_DEFAULT = "/holdings/fund/default";
	private final static String API_HOLDING_LIST = "/holdings/fund";
	private final static String API_HOLDING_MUTUAL = "/holdings/mutual";
	private final static String API_HOLDING_HISTORY = "/holdings/history";
	private final static String API_HOLDING_COMPARATOR = "/holdings/comparator";
	private final static String API_HOLDING_SEARCH = "/holdings/search";
	
	private final static String API_NEWS_LIST = "/news/fund/";
	private final static String API_NEWS = "/news/";
	
	private final static String API_ANALYSIS_MUTUAL = "/analysis/mutual";
	private final static String API_ANALYSIS_RANK = "/analysis/rank";
	
	private final static String API_SPX_PE = "/spx/pe";
	private final static String API_SPX_SHILLER_PE = "/spx/shillerpe";
	private final static String API_SPX_EPS = "/spx/eps";
	private final static String API_SPX_DPS = "/spx/dps";
	
	private static String getAPIHost(){
		return "http://api.app.oppsis.com";
		//return "http://192.168.11.112:8080";
	}
	
	public static String getDateListAllUrl(){
		return getAPIHost() + API_BASE_URL + API_DATE_LIST_ALL;
	}
	
	public static String getDateListUrl(){
		return getAPIHost() + API_BASE_URL + API_DATE_LIST;
	}
	
	public static String getDateListsUrl(){
		return getAPIHost() + API_BASE_URL + API_DATE_LISTS;
	}
	
	public static String getIndustryListDefaultUrl(){
		return getAPIHost() + API_BASE_URL + API_INDUSTRY_LIST_DEFAULT;
	}
	
	public static String getIndustryListUrl(){
		return getAPIHost() + API_BASE_URL + API_INDUSTRY_LIST;
	}
	
	public static String getIndustryListsUrl(){
		return getAPIHost() + API_BASE_URL + API_INDUSTRY_LISTS;
	}
	
	public static String getHoldingListDefaultUrl(){
		return getAPIHost() + API_BASE_URL + API_HOLDING_LIST_DEFAULT;
	}
	
	public static String getHoldingListUrl(){
		return getAPIHost() + API_BASE_URL + API_HOLDING_LIST;
	}
	
	public static String getHoldingMutualUrl(){
		return getAPIHost() + API_BASE_URL + API_HOLDING_MUTUAL;
	}
	
	public static String getHoldingHistoryUrl(){
		return getAPIHost() + API_BASE_URL + API_HOLDING_HISTORY;
	}
	
	public static String getHoldingComparatorUrl(){
		return getAPIHost() + API_BASE_URL + API_HOLDING_COMPARATOR;
	}
	
	public static String getHoldingSearchUrl(){
		return getAPIHost() + API_BASE_URL + API_HOLDING_SEARCH;
	}
	
	public static String getNewsListUrl(){
		return getAPIHost() + API_BASE_URL + API_NEWS_LIST;
	}
	
	public static String getNewsContentUrl(){
		return getAPIHost() + API_BASE_URL + API_NEWS;
	}
	
	public static String getAnalysisMutualUrl(){
		return getAPIHost() + API_BASE_URL + API_ANALYSIS_MUTUAL;
	}
	
	public static String getRankUrl(){
		return getAPIHost() + API_BASE_URL + API_ANALYSIS_RANK;
	}
	
	public static String getSPXPEUrl(){
		return getAPIHost() + API_BASE_URL + API_SPX_PE;
	}
	
	public static String getSPXShillerPEUrl(){
		return getAPIHost() + API_BASE_URL + API_SPX_SHILLER_PE;
	}
	
	public static String getSPXEPSUrl(){
		return getAPIHost() + API_BASE_URL + API_SPX_EPS;
	}
	
	public static String getSPXDPSUrl(){
		return getAPIHost() + API_BASE_URL + API_SPX_DPS;
	}
}
