package com.oppsis.app.hftracker.model;

import org.apache.commons.lang.builder.ToStringBuilder;

public class FundAnalysis {

    private int shares,count,port;
	private String security, ticker, fund_names, securityType;
	
    public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}
	
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

    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
