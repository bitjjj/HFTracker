package com.oppsis.app.hftracker.pojo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.google.gson.Gson;
import com.oppsis.app.hftracker.util.I18nTextUtils;
import com.oppsis.app.util.FileUtils;

public class FundLocalInfoManager {
	private final static String FUND_LIST_NAME = "fundlist.json";
	private final static String INDUSTRY_LIST_NAME = "industrylist.json";
	
	private static List<FundObject> fundList = null;
	private static List<IndustryObject> industryList = null;
	private static Map<String,IndustryObject> industryMap = null;
	
	public static List<FundObject> getFundList(Context context){
		if(fundList == null){
			fundList = new ArrayList<FundObject>();
			String listStr = FileUtils.readFromAssetsfile(FUND_LIST_NAME, context);
			
			try {
				JSONObject listJson = new JSONObject(listStr);
				JSONArray listJsonArr = listJson.optJSONArray("list");
				
				for(int i=0;i<listJsonArr.length();i++){
					JSONObject fundInfo = listJsonArr.getJSONObject(i);
					FundObject fundObject = new Gson().fromJson(fundInfo.toString(), FundObject.class);
					fundList.add(fundObject);
				}
				
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		
		return fundList;
		
	}
	
	/**
	 * @param context
	 * @param fundName
	 * @param property
	 * @return
	 */
	public static String getFundProperty(Context context,String fundName,String property){
		if(fundList == null){
			getFundList(context);
		}
		String result = "";
		for(FundObject obj:fundList){
			if(obj.getName().equals(fundName)){
				if(property.equals(FundObject.PROPERTY_FUND_ID)){
					result =  obj.getFundId();
				}
				else if(property.equals(FundObject.PROPERTY_FUND_NAME)){
					result =  I18nTextUtils.getFundName(context, obj);
				}
				else if(property.equals(FundObject.PROPERTY_MANAGER_NAME)){
					result =  I18nTextUtils.getManagerName(context, obj);
				}
				else if(property.equals(FundObject.PROPERTY_MANAGER_ICON_PIC_H)){
					result =  obj.getManagerIconPicH();
				}
				else if(property.equals(FundObject.PROPERTY_MANAGER_ICON_PIC_XH)){
					result =  obj.getManagerIconPicXH();
				}
				else if(property.equals(FundObject.PROPERTY_MANAGER_ICON_PIC_XXH)){
					result =  obj.getManagerIconPicXXH();
				}
			}
		}
		
		return result;
	}
	
	public static String getI18nFundProperty(Context context,String fundName,String property){
		return getFundProperty(context,fundName,property);
	}
	
	public static List<IndustryObject> getIndustryList(Context context){
		if(industryList == null){
			industryList = new ArrayList<IndustryObject>();
			String listStr = FileUtils.readFromAssetsfile(INDUSTRY_LIST_NAME, context);
			
			try {
				JSONObject listJson = new JSONObject(listStr);
				JSONArray listJsonArr = listJson.optJSONArray("list");
				
				for(int i=0;i<listJsonArr.length();i++){
					JSONObject fundInfo = listJsonArr.getJSONObject(i);
					IndustryObject fundObject = new Gson().fromJson(fundInfo.toString(), IndustryObject.class);
					industryList.add(fundObject);
				}
				
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		
		return industryList;
		
	}
	
	public static IndustryObject getIndustry(Context context,String name){
		if(industryMap == null){
			industryMap = new HashMap<String, IndustryObject>();
			String listStr = FileUtils.readFromAssetsfile(INDUSTRY_LIST_NAME, context);
			
			try {
				JSONObject listJson = new JSONObject(listStr);
				JSONArray listJsonArr = listJson.optJSONArray("list");
				
				for(int i=0;i<listJsonArr.length();i++){
					JSONObject fundInfo = listJsonArr.getJSONObject(i);
					IndustryObject fundObject = new Gson().fromJson(fundInfo.toString(), IndustryObject.class);
					industryMap.put(fundObject.getName(), fundObject);
				}
				
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		
		return industryMap.get(name);
		
	}
	
	public static String getI18nIndustryName(Context context,String name){
		IndustryObject industryObject = getIndustry(context, name);
		return I18nTextUtils.getIndustryName(context, industryObject);
	}
	
	public static String getI18nIndustryAbbrName(Context context,String name){
		IndustryObject industryObject = getIndustry(context, name);
		return (name.equals("") || name == null) ? "" : I18nTextUtils.getIndustryAbbrName(context, industryObject);
	}
	
}
