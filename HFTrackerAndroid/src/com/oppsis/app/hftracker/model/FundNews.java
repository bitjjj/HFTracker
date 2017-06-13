package com.oppsis.app.hftracker.model;

import android.os.Parcel;
import android.os.Parcelable;

public class FundNews implements Parcelable{

	public final static String SOURCE_INSIDERMONKEY = "insidermonkey";
	
	private int id,newsType;
	private String title,dateStr,summary,picUrl,refId,fund_name,timeMark,url;
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getNewsType() {
		return newsType;
	}
	public void setNewsType(int newsType) {
		this.newsType = newsType;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDateStr() {
		return dateStr;
	}
	public void setDateStr(String dateStr) {
		this.dateStr = dateStr;
	}
	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}
	public String getPicUrl() {
		return picUrl;
	}
	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}
	public String getRefId() {
		return refId;
	}
	public void setRefId(String refId) {
		this.refId = refId;
	}
	public String getFund_name() {
		return fund_name;
	}
	public void setFund_name(String fund_name) {
		this.fund_name = fund_name;
	}
	public String getTimeMark() {
		return timeMark;
	}
	public void setTimeMark(String timeMark) {
		this.timeMark = timeMark;
	}
	
	
	private FundNews(Parcel in) {
		id = in.readInt();
		url = in.readString();
		fund_name = in.readString();
		title = in.readString();
	}
    
    public static final Parcelable.Creator<FundNews> CREATOR = new Parcelable.Creator<FundNews>() {
		public FundNews createFromParcel(Parcel in) {
			return new FundNews(in);
		}

		public FundNews[] newArray(int size) {
			return new FundNews[size];
		}
	};
	
	@Override
	public int describeContents() {
		return 0;
	}
	
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(id);
		dest.writeString(url);
		dest.writeString(fund_name);
		dest.writeString(title);
		
	}
	
	
}
