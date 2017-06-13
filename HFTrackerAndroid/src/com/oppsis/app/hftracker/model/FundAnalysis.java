package com.oppsis.app.hftracker.model;

import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;

import com.oppsis.app.util.FuncUtils;


public class FundAnalysis {

    private int shares,count;
    private String security, ticker, fund_names, securityType;

    public int getShares() {
        return shares;
    }

    public void setShares(int shares) {
        this.shares = shares;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getSecurity() {
        return security;
    }

    public void setSecurity(String security) {
        this.security = security;
    }

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public String getFund_names() {
        return fund_names;
    }

    public void setFund_names(String fund_names) {
        this.fund_names = fund_names;
    }

    public String getSecurityType() {
        return securityType;
    }

    public void setSecurityType(String securityType) {
        this.securityType = securityType;
    }
    
    public String getId(){
    	return security + "|" + securityType + "|" + ticker;
    }
    
    public SortedSet<Map.Entry<String, Float>> getHoldingDetails(){
    	String[] holdings = fund_names.split("\\|");
    	Map<String, Float> holdingMap = new HashMap<String,Float>();
    	
    	for(String holding:holdings){
    		holding = holding.trim();
    		String[] holdingDetail = holding.split("!");
    		//Log.d(this.toString(), holding);
    		//Log.d(this.toString(), holdingDetail[0] + "," + holdingDetail[1]);
    		if(holdingMap.containsKey(holdingDetail[0])){
    			Float port = holdingMap.get(holdingDetail[0]);
    			port += Float.valueOf(holdingDetail[1]);
    		}
    		else{
    			holdingMap.put(holdingDetail[0], Float.valueOf(holdingDetail[1]));
    		}
    	}
    	
    	return FuncUtils.entriesSortedByValues(holdingMap,false);
    	
    }


}
