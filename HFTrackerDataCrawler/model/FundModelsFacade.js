var FundInfoModel = require("./FundInfoModel"),
	FundDateListModel = require("./FundDateListModel"),
	FundIndustryPercentageModel = require("./FundIndustryPercentageModel"),
	FundNewsModel = require("./FundNewsModel"),
	FundHoldingsModel = require("./FundHoldingsModel");

module.exports = {
	addFundInfo : function(fundInfo,success,failure){
		//console.log(fundInfo);
		new FundInfoModel(fundInfo).add(success,failure);
	},
	
	addFundDateList : function(fundInfo,success,failure){
		//console.log(fundInfo);
		new FundDateListModel(fundInfo).add(success,failure);
	},
	
	addFundIndustryPercentage : function(fundInfo,success,failure){
		
		this._addFundIndustryPercentage(fundInfo,0);
	},
	
	_addFundIndustryPercentage : function(fundInfo,index){
		if(index >= fundInfo.industryPort.length)return;
		
		var self = this;
		fundInfo.num = fundInfo.industryPort[index].value;
		fundInfo.name = fundInfo.industryPort[index].key;
		
		new FundIndustryPercentageModel(fundInfo).add(function(){
			//fundInfo.dateStr.indexOf("2017")!=-1 && console.log("add....");
			//self._addFundIndustryPercentage(fundInfo,++index);
		});
	},
	
	addFundNews : function(fundInfo,success,failure){
		new FundNewsModel(fundInfo).add(success,failure);
	},
	
	addFundHoldings : function(fundInfo,success,failure){
		new FundHoldingsModel(fundInfo).add(success,failure);
	},
	
	getFundDateList : function(fundInfo,success,failure){
		new FundDateListModel({}).getByFundName(fundInfo.managerName + "$" + fundInfo.fundName,success,failure);
	},
	
	getHoldingsByDate : function(fundInfo,success,failure){
		new FundHoldingsModel({}).getByDate(fundInfo.managerName + "$" + fundInfo.fundName,fundInfo.dateStr,success,failure);
	}

};