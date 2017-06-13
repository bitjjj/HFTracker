package com.oppsis.app.hftracker.model;

import android.os.Parcel;
import android.os.Parcelable;


public class FundHolding implements Parcelable,FundChartParcelable{

	public final static FundHolding EMPTY = new FundHolding(); 
	
	public final static int ACTION_UP = 1;
	public final static int ACTION_DOWN = -1;
	public final static int ACTION_UNCHANGED = 0;
	public final static int ACTION_CLEAR = -2;
	public final static int ACTION_ADD = 2;

    private int id, action,shares, value;
    private float activity, port;
    private String security, ticker, dateStr, fund_name, securityType, addBy;
    
    public FundHolding(){
    	
    }
    
    public int getShares() {
        return shares;
    }

    public void setShares(int shares) {
        this.shares = shares;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public float getActivity() {
        return activity;
    }

    public void setActivity(float activity) {
        this.activity = activity;
    }

    public float getPort() {
        return port;
    }

    public void setPort(float port) {
        this.port = port;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
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

    public String getDateStr() {
        return dateStr;
    }

    public void setDateStr(String dateStr) {
        this.dateStr = dateStr;
    }

    public String getFund_name() {
        return fund_name;
    }

    public void setFund_name(String fund_name) {
        this.fund_name = fund_name;
    }

    public String getSecurityType() {
        return securityType;
    }

    public void setSecurityType(String securityType) {
        this.securityType = securityType;
    }

    public String getAddBy() {
        return addBy;
    }

    public void setAddBy(String addBy) {
        this.addBy = addBy;
    }

	private FundHolding(Parcel in) {
		security = in.readString();
		ticker = in.readString();
		securityType = in.readString();
		fund_name = in.readString();
	}
    
    public static final Parcelable.Creator<FundHolding> CREATOR = new Parcelable.Creator<FundHolding>() {
		public FundHolding createFromParcel(Parcel in) {
			return new FundHolding(in);
		}

		public FundHolding[] newArray(int size) {
			return new FundHolding[size];
		}
	};

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(security);
		dest.writeString(ticker);
		dest.writeString(securityType);
		dest.writeString(fund_name);
	}
	
	@Override
	public String getComparatorField() {
		return dateStr;
	}
	@Override
	public void setComparatorField(String field) {
		dateStr = field;
	}
	@Override
	public String getIdentifier() {
		return fund_name;
	}
	@Override
	public void setIdentifier(String identifier) {
		this.fund_name = identifier;
		
	}

	@Override
	public void initValue() {
		
	}
	
}
