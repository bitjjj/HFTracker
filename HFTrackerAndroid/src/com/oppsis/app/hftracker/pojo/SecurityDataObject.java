package com.oppsis.app.hftracker.pojo;

import android.os.Parcel;
import android.os.Parcelable;

public class SecurityDataObject implements Parcelable{

	public final static int ICON_SHOW = 1;
	public final static int ICON_HIDE = 0;

	private String ticker,security,fund_name;
	private int icon;
	
	public SecurityDataObject(String ticker,String security,String fund_name){
		this.ticker = ticker;
		this.security = security;
		this.fund_name = fund_name;
		icon = ICON_SHOW;
	}
	
	public SecurityDataObject(String ticker,String security,String fund_name,int icon){
		this.ticker = ticker;
		this.security = security;
		this.fund_name = fund_name;
		this.icon = icon;
	}
	
	public String getTicker() {
		return ticker;
	}


	public void setTicker(String ticker) {
		this.ticker = ticker;
	}


	public String getSecurity() {
		return security;
	}


	public void setSecurity(String security) {
		this.security = security;
	}


	public String getFund_name() {
		return fund_name;
	}


	public void setFund_name(String fund_name) {
		this.fund_name = fund_name;
	}


	public int getIcon() {
		return icon;
	}


	public void setIcon(int icon) {
		this.icon = icon;
	}


	private SecurityDataObject(Parcel in) {
		security = in.readString();
		ticker = in.readString();
		fund_name = in.readString();
		icon = in.readInt();
	}
	
	
	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(security);
		dest.writeString(ticker);
		dest.writeString(fund_name);
		dest.writeInt(icon);
	}
	
	public static final Parcelable.Creator<SecurityDataObject> CREATOR = new Parcelable.Creator<SecurityDataObject>() {
		public SecurityDataObject createFromParcel(Parcel in) {
			return new SecurityDataObject(in);
		}

		public SecurityDataObject[] newArray(int size) {
			return new SecurityDataObject[size];
		}
	};

}
