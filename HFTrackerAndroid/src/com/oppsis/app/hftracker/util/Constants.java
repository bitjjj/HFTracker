package com.oppsis.app.hftracker.util;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import com.oppsis.app.hftracker.R;
import com.oppsis.app.hftracker.ui.fragment.AnalysisIndustryFragment_;
import com.oppsis.app.hftracker.ui.fragment.AnalysisMutualSecurityFragment_;
import com.oppsis.app.hftracker.ui.fragment.AnalysisPortfolioFragment_;
import com.oppsis.app.hftracker.ui.fragment.FundDetailHoldingFragment_;
import com.oppsis.app.hftracker.ui.fragment.FundDetailIndustryFragment_;
import com.oppsis.app.hftracker.ui.fragment.FundDetailNewsFragment_;

@SuppressWarnings("serial")
public class Constants {

	public final static String[] FUND_DETAIL_FRAGMENT_TAGS = {"fund_holdings","fund_industry","fund_news"};
	public final static int[] FUND_DETAIL_FRAGMENT_TITLE_IDS = { R.string.fund_detail_menu_holdings,R.string.fund_detail_menu_industry,R.string.fund_detail_menu_news};
	public final static String[] FUND_DETAIL_FRAGMENT_NAMES = { FundDetailHoldingFragment_.class.getName(),FundDetailIndustryFragment_.class.getName(),FundDetailNewsFragment_.class.getName()};
	
	public final static String[] ANALYSIS_FRAGMENT_TAGS = {"analysis_portfolio","analysis_industry","analysis_news"};
	public final static int[] ANALYSIS_FRAGMENT_TITLE_IDS = { R.string.analysis_menu_portfolio,R.string.analysis_menu_industry,R.string.analysis_menu_mutual_security};
	public final static String[] ANALYSIS_FRAGMENT_NAMES = { AnalysisPortfolioFragment_.class.getName(),AnalysisIndustryFragment_.class.getName(),AnalysisMutualSecurityFragment_.class.getName()};
	
	public final static String FUND_HOLDINGS_CHART_FRAGMENT_TAG = "fragment_fund_holdings_chart";
	public final static String FUND_HOLDINGS_LIST_FRAGMENT_TAG = "fragment_fund_holdings_list";
	public final static String FUND_HOLDINGS_INDUSTRY_FRAGMENT_TAG = "fragment_fund_holdings_industry";
	public final static String FUND_NEWS_LIST_FRAGMENT_TAG = "fragment_fund_news_list";
	public final static String FUND_NEWS_DETAIL_FRAGMENT_TAG = "fragment_fund_news_detail";
	
	public final static int FUND_LIST_COLUMN_SIZE = 2;
	public final static int ACTIONBAR_LIST_NEWS = 2;
	public final static int ACTIONBAR_LIST_INDUSTRY = 1;
	public final static int ACTIONBAR_LIST_HOLDINGS = 0;
	
	public final static String TRUE_STR = "true";
	public final static String FALSE_STR = "false";
	
	public final static int HALF_HOUR_SECONDS = 30 * 60;
	public final static int ANIM_MANAGER_BAR_DURATION = 3000;
	
	public final static String SECURITY_TYPE_PREFIX = "type";
	public final static String SECURITY_TYPE_ALL = "lla";//avoid 'call' type
	
	public final static Map<String, Integer> FUND_MANAGER_ICONS = new HashMap<String,Integer>(){{
		put("1", R.drawable.fund_buffett_icon);
		put("2", R.drawable.fund_soros_icon);
		put("4", R.drawable.fund_icahn_icon);
		put("5", R.drawable.fund_simons_icon);
		put("8", R.drawable.fund_cohen_icon);
		put("11", R.drawable.fund_fisher_icon);
		put("13", R.drawable.fund_ackman_icon);
		put("14", R.drawable.fund_lampert_icon);
		put("15", R.drawable.fund_einhorn_icon);
		put("16", R.drawable.fund_loeb_icon);
		put("18", R.drawable.fund_paulson_icon);
		put("23", R.drawable.fund_jones_icon);
		put("24", R.drawable.fund_tepper_icon);
		put("44", R.drawable.fund_griffin_icon);
		put("49", R.drawable.fund_cooperman_icon);
		put("50", R.drawable.fund_robertson_icon);
		put("67", R.drawable.fund_englander_icon);
		put("72", R.drawable.fund_dalio_icon);
		put("129", R.drawable.fund_price_icon);
		put("295", R.drawable.fund_druckenmiller_icon);
	}};
	
	public final static class PrefKeys{
		public final static String ACTIONBAR_LIST_INDEX_FUND_DETAIL = "actionbar_mode_list_index_fund_detail";
		public final static String ACTIONBAR_LIST_INDEX_ANALYSIS = "actionbar_mode_list_index_analysis";
		public final static String ACTIONBAR_LIST_DATE_FUND_DETAIL = "actionbar_mode_list_index_fund_detail";
		public final static String ACTIONBAR_LIST_DATE_ANALYSIS = "actionbar_mode_list_index_analysis";
	}
	
	public final static String PACKAGE_NAME = "com.oppsis.app.hftracker";
	
	public final static Map<Integer, String> SORT_FIELDS = new HashMap<Integer, String>(){{
		put(5, "security");
		put(4, "ticker");
		put(3, "shares");
		put(2, "value");
		put(1, "activity");
		put(0, "port");
	}};
	
	public final static Map<Integer, String> SORT_DIRECTIONS = new HashMap<Integer, String>(){{
		put(0, "desc");
		put(1, "");//stands for asc
	}};
	
	public final static String SECURITY_TYPES_ALL = "STOCK,BOND,PUT,CALL";
	public final static String SECURITY_TYPE_STOCK = "STOCK";
	public final static String SECURITY_TYPE_CALL = "CALL";
	public final static String SECURITY_TYPE_PUT = "PUT";
	public final static String SECURITY_TYPE_BOND = "BOND";
	public final static Map<String, String> SECURITY_TYPES_MAP = new HashMap<String, String>(){{
		put("type|stock", SECURITY_TYPE_STOCK);
		put("type|bond", SECURITY_TYPE_BOND);
		put("type|put", SECURITY_TYPE_PUT);
		put("type|call", SECURITY_TYPE_CALL);
	}};
	public final static String[] SECURITY_TYPES_ARR = {SECURITY_TYPES_ALL,SECURITY_TYPE_STOCK,SECURITY_TYPE_CALL,SECURITY_TYPE_PUT,SECURITY_TYPE_BOND};
	
	public final static String REGEX_SPECIAL_CHARS = "[$|,]"; 
	
	public final static DecimalFormat US_DECIMAL_FORMAT = new DecimalFormat("#,###,##0");
	
	public final static int PAGE_ZERO = 0;
	public final static int PAGE_FIRST = 1;
	public final static int PAGE_LOADING_BEFORE_COUNT = 10;
	public final static int PAGE_COUNT = 20;
	
	public final static String PERCENTAGE_MARK = "%";
	public final static String FUND_DETAIL_STOCK_UP = "<font color='green'><b>%s%%</b>↑</font>";
	public final static String FUND_DETAIL_STOCK_DOWN = "<font color='red'><b>%s%%</b>↓</font>";
	public final static String FUND_DETAIL_STOCK_ADD = "<font color='blue'>✚</font>";
	public final static String FUND_DETAIL_STOCK_CLEAR = "<font color='red'>✖</font>";
	
	public final static String STOCK_YAHOO_API = "http://finance.yahoo.com/d/quotes.csv?s=%s&f=sl1c1p2vd1t1ohg&e=.csv";
	public final static String STOCK_YAHOO_URL_EN = "http://finance.yahoo.com/q?s=%s";
	public final static String STOCK_YAHOO_URL_HK = "http://hk.finance.yahoo.com/q?s=%s";
	public final static String STOCK_SINA_URL_CN = "http://stock1.sina.cn/prog/wapsite/stock/v2/nasdaq_single.php?code=%s&vt=4";
	
	public final static String URL_KEY_CACHE = "cache";
	public final static String URL_KEY_EXPIRY = "expiry";
	public final static String URL_KEY_REFRESH = "refresh";
	
	public final static int WEBVIEW_TYPE_YAHOO = 1;
	public final static int WEBVIEW_TYPE_NEWS = 2;
	
	public final static String MANAGER_SELECT_RESULT = "MANAGER_SELECT_RESULT";
	
	public final static String SORTER_LIST = "sort_name";
	public final static String IDENTIFIER_LIST = "identifier_list";
	public final static String RESULT_LIST = "result_list";
	
	public final static String VERSION_PREFIX = "V";
	public final static String PUSH_MESSAGE_ENABLED = "notice_enabled";
	
	public final static String APP_LINK = "https://play.google.com/store/apps/details?id=" + PACKAGE_NAME;
	
	public final static String JPUSH_TAG_STATUS = "jpush_tag_status";
	
}
