package com.oppsis.app.hftracker.util;

public class Constants {

	public final static int PAGE_SIZE_20 = 20;
	public final static int PAGE_SIZE_10 = 10;
	
	public final static String NEWS_GENERATOR_PREFIX = "new.generator.";
	public final static String NEWS_CONTENT_DIR = "news.content.dir";
	public final static String NEWS_BASE_URL = "news.url";
	
	public final static class DBTables{
		public static String FUND_HOLDINGS = "fund_holdings";
	}
	
	public final static class DBSQLs{
		public static String QUERY_HOLDINGS_BY_DEFAULT = "queryHoldingsByDefault";
		public static String QUERY_HOLDINGS_BY_NAME = "queryHoldingsByName";
		public static String QUERY_NEWS_BY_NAME = "queryNewsByName";
		public static String QUERY_DATE_LIST_BY_NAME = "queryDateListByName";
		public static String QUERY_DATE_LIST_BY_NAMES = "queryDateListByNames";
		public static String QUERY_WHOLE_DATE_LIST = "queryWholeDateList";
		public static String GROUP_HOLDINGS = "groupHoldings";
		public static String QUERY_HOLDINGS_BY_TICKER_NAME = "queryHoldingsByTickerAndName";
		public static String QUERY_HOLDINGS_BY_TICKER_NAMES = "queryHoldingsByTickerAndNames";
		public static String QUERY_HOLDINGS_BY_TICKER = "queryHoldingsByTicker";
		public static String QUERY_INDUSTRY_BY_DEFAULT = "queryIndustryByDefault";
		public static String QUERY_INDUSTRY_BY_NAME = "queryIndustryByName";
		public static String QUERY_INDUSTRY_BY_NAMES = "queryIndustryByNames";
		public static String QUERY_MUTUAL_HOLDINGS = "queryMutualHoldings";
		public static String QUERY_MUTUAL_HOLDINGS_RANK = "queryMutualHoldingsRank";
		public static String QUERY_SPX_DATA = "querySPX";
		
	}
	
	public final static class CacheNames{
		public static String CACHE_HOLDINGS = "holdings";
		public static String CACHE_NEWS_LIST = "news-list";
		public static String CACHE_NEWS_CONTENT = "news-content";
		public static String CACHE_DATE_LIST = "date-list";
		public static String CACHE_INDUSTRY_LIST = "industry-list";
		public static String CACHE_ANALYSIS = "analysis";
		public static String CACHE_SPX = "spx";
	}
	
	public final static class SecurityTypes{
		public static String STOCK = "STOCK";
		public static String PUT = "PUT";
		public static String CALL = "CALL";
		public static String BOND = "BOND";
	}
	
	public final static class SPXTables{
		public static String PE = "spx_pe_year";
		public static String SHILLER_PE = "spx_shiller_pe_year";
		public static String EPS = "spx_eps_year";
		public static String DPS = "spx_dps_year";
	}
	
	public final static String HTML_FILE = ".html";
	
}
