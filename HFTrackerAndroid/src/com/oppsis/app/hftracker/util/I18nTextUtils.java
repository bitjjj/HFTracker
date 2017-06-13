package com.oppsis.app.hftracker.util;

import com.oppsis.app.hftracker.R;
import com.oppsis.app.hftracker.pojo.FundObject;
import com.oppsis.app.hftracker.pojo.IndustryObject;

import android.content.Context;

public class I18nTextUtils {

	public static String getFundName(Context context, FundObject fund) {
		String language = context.getString(R.string.language),fundName="";
		if(language.equals("CN")){
			fundName = fund.getFundName_zh();
		}
		else if(language.equals("HK") || language.equals("TW")){
			fundName = fund.getFundName_tw();
		}
		else if(language.equals("GB") || language.equals("US")){
			fundName = fund.getFundName();
		}
		
		return fundName;
	}

	public static String getManagerName(Context context, FundObject fund) {
		String language = context.getString(R.string.language),fundManagerName="";
		if(language.equals("CN")){
			fundManagerName = fund.getManagerName_zh();
		}
		else if(language.equals("HK") || language.equals("TW")){
			fundManagerName = fund.getManagerName_tw();
		}
		else if(language.equals("GB") || language.equals("US")){
			fundManagerName = fund.getManagerName();
		}
		
		return fundManagerName;
	}

	public static String getIndustryName(Context context,IndustryObject industry) {
		String language = context.getString(R.string.language),industryName="";
		if(language.equals("CN")){
			industryName = industry.getName_zh();
		}
		else if(language.equals("HK") || language.equals("TW")){
			industryName = industry.getName_tw();
		}
		else if(language.equals("GB") || language.equals("US")){
			industryName = industry.getName();
		}
		
		return industryName;
	}
	
	public static String getIndustryAbbrName(Context context,IndustryObject industry) {
		String language = context.getString(R.string.language),industryName="";
		if(language.equals("CN")){
			industryName = industry.getName_abbr_zh();
		}
		else if(language.equals("HK") || language.equals("TW")){
			industryName = industry.getName_abbr_tw();
		}
		else if(language.equals("GB") || language.equals("US")){
			industryName = industry.getName_abbr();
		}
		
		return industryName;
	}
	
	public static String getStockUrl(Context context) {
		String language = context.getString(R.string.language),url="";
		if(language.equals("CN")){
			url = Constants.STOCK_SINA_URL_CN;
		}
		else if(language.equals("HK") || language.equals("TW")){
			url = Constants.STOCK_YAHOO_URL_EN;
		}
		else if(language.equals("GB") || language.equals("US")){
			url = Constants.STOCK_YAHOO_URL_EN;
		}
		
		return url;
	}
	
	

}
