package com.oppsis.app.hftracker.pojo;

import java.io.Serializable;

import android.os.Parcel;
import android.os.Parcelable;

public class FundObject implements Parcelable,Serializable{

	private static final long serialVersionUID = 6501608313790670345L;

	public final static String HERO = "true";
	public final static String PROPERTY_FUND_ID = "fundId";
	public final static String PROPERTY_FUND_NAME = "fundName";
	public final static String PROPERTY_MANAGER_NAME = "managerName";
	public final static String PROPERTY_MANAGER_ICON_PIC_H = "managerIconPicH";
	public final static String PROPERTY_MANAGER_ICON_PIC_XH = "managerIconPicXH";
	public final static String PROPERTY_MANAGER_ICON_PIC_XXH = "managerIconPicXXH";
	
	private String fundName; 
	private String managerName;
	private String managerPic;

	private String fundUrl;
	private String fundId;
	private String hero;
	private String fundName_zh; 
	private String managerName_zh; 
	private String fundName_tw;
	private String managerName_tw;
	private String color;
	
	private String managerCoverPicXXH;
	private String managerCoverPicXH;
	private String managerCoverPicH;
	private String managerIconPicXXH;
	private String managerIconPicXH;
	private String managerIconPicH;
	
	
	public String getManagerCoverPicXXH() {
		return managerCoverPicXXH;
	}
	public void setManagerCoverPicXXH(String managerCoverPicXXH) {
		this.managerCoverPicXXH = managerCoverPicXXH;
	}
	public String getManagerCoverPicXH() {
		return managerCoverPicXH;
	}
	public void setManagerCoverPicXH(String managerCoverPicXH) {
		this.managerCoverPicXH = managerCoverPicXH;
	}
	public String getManagerCoverPicH() {
		return managerCoverPicH;
	}
	public void setManagerCoverPicH(String managerCoverPicH) {
		this.managerCoverPicH = managerCoverPicH;
	}
	public String getManagerIconPicXXH() {
		return managerIconPicXXH;
	}
	public void setManagerIconPicXXH(String managerIconPicXXH) {
		this.managerIconPicXXH = managerIconPicXXH;
	}
	public String getManagerIconPicXH() {
		return managerIconPicXH;
	}
	public void setManagerIconPicXH(String managerIconPicXH) {
		this.managerIconPicXH = managerIconPicXH;
	}
	public String getManagerIconPicH() {
		return managerIconPicH;
	}
	public void setManagerIconPicH(String managerIconPicH) {
		this.managerIconPicH = managerIconPicH;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public String getFundName() {
		return fundName;
	}
	public void setFundName(String fundName) {
		this.fundName = fundName;
	}
	public String getManagerName() {
		return managerName;
	}
	public void setManagerName(String managerName) {
		this.managerName = managerName;
	}
	public String getManagerPic() {
		return managerPic;
	}
	public void setManagerPic(String managerPic) {
		this.managerPic = managerPic;
	}
	public String getFundUrl() {
		return fundUrl;
	}
	public void setFundUrl(String fundUrl) {
		this.fundUrl = fundUrl;
	}
	public String getFundId() {
		return fundId;
	}
	public void setFundId(String fundId) {
		this.fundId = fundId;
	}
	public String getFundName_zh() {
		return fundName_zh;
	}
	public void setFundName_zh(String fundName_zh) {
		this.fundName_zh = fundName_zh;
	}
	public String getManagerName_zh() {
		return managerName_zh;
	}
	public void setManagerName_zh(String managerName_zh) {
		this.managerName_zh = managerName_zh;
	}
	public String getFundName_tw() {
		return fundName_tw;
	}
	public void setFundName_tw(String fundName_tw) {
		this.fundName_tw = fundName_tw;
	}
	public String getManagerName_tw() {
		return managerName_tw;
	}
	public void setManagerName_tw(String managerName_tw) {
		this.managerName_tw = managerName_tw;
	} 
	public String getHero() {
		return hero;
	}
	public void setHero(String hero) {
		this.hero = hero;
	}
	
	public String getName(){
		return this.managerName + "$" + this.fundName;
	}
	
	public static String getManagerName(String name){
		return name.split("\\$")[0];
	}
	
	public static String getFundName(String name){
		return name.split("\\$")[1];
	}
	
	public String getManagerInfo(){
		return this.getName() + "|" + this.managerIconPicH;
	}
	
	public static String getManagerNameInfo(String info){
		return info.split("\\|")[0];
	}
	
	public static String getManagerPicInfo(String info){
		return info.split("\\|")[1];
	}
	
	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(fundId);
		dest.writeString(fundName);
		dest.writeString(managerName);
		
	}

	public static final Parcelable.Creator<FundObject> CREATOR = new Parcelable.Creator<FundObject>() {
		public FundObject createFromParcel(Parcel in) {
			return new FundObject(in);
		}

		public FundObject[] newArray(int size) {
			return new FundObject[size];
		}
	};

	private FundObject(Parcel in) {
		fundId = in.readString();
		fundName = in.readString();
		managerName = in.readString();
		
	}
}
