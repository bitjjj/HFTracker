var HttpUtil = require("./util/HttpUtil"),
	Constants = require("./util/Constants"),
	Config = require("./config/config"),
	FundModelsFacade = require("./model/FundModelsFacade"),
	cheerio = require("cheerio"),
	util = require("util"),
	querystring = require("querystring"),
	url = require("url"),
	events = require("events");
	
var argv = process.argv,fundIndex = argv.length>=3 ? argv[2] : null,
	runByNode = argv.join("").indexOf("FundDetailsCrawler") != -1;

function Crawler(){
	this.currentFund = null;
	this.fundList = null;
	events.EventEmitter.call(this);
}
util.inherits(Crawler, events.EventEmitter);

Crawler.prototype.run = function(){
	var fundList = this._loadFundList(),list = fundList.list;

	if(fundIndex != null){
		var fundIndexArr = fundIndex.split(",");
		for(var i=0; i<fundIndexArr.length; i++){
			(new _CrawlerThread(list[fundIndexArr[i]])).run();
		}
	}
	else{
		for(var i=0;i<list.length;i++){
			(new _CrawlerThread(list[i])).run();
		}
	}
	
}

Crawler.prototype._loadFundList = function(){
	return this.fundList ? this.fundList : (this.fundList = require(Config.hf.listFile));
}

function _CrawlerThread(fund){
	this.fund = fund;
}

_CrawlerThread.prototype = {
	run : function(){
		var self = this;
		HttpUtil.get(this.fund.fundUrl,function(html){
			self.parseContent(html);
		});
	},
	
	parseContent : function(html){
		var $ = cheerio.load(html),
		fundSummaryNode = $(".bio .content"),
		summary = fundSummaryNode.find(".less").length > 0 ? (fundSummaryNode.find(".less").html() + fundSummaryNode.find(".more").html()) : fundSummaryNode.find("div").html(),
		dateListNodes = $("#select-pfp option"),
		targetDate,self = this;
		//console.log(fundSummaryNode.find(".less").html(),fundSummaryNode.find(".more").html(),summary);
		this.fund.summary = escape(summary);
		FundModelsFacade.addFundInfo(this.fund,function(){
			dateListNodes.each(function(index,ele){
				targetDate = $(this).text();
				self.loadProfilePage(targetDate);
			});
		})
		
	},
	
	loadProfilePage : function(targetDate){
		var self = this;
		HttpUtil.get(Config.hf.profilePage.replace("{id}",this.fund.fundId).replace("{date}",targetDate),function(html){
			var $ = cheerio.load("<div id='fund-profile-data'>" + html + "</div>"),profileNode = $("#fund-profile-data");
			
			if(profileNode.html().indexOf(Constants.FATAL_ERROR) != -1 || profileNode.html().indexOf(Constants.UNKNOWN_ERROR) != -1)return;
			
			var	industryUrl = profileNode.find(".chart img").attr("src"),
				profileItems = profileNode.find(".info tr"),
				portValue,portQTR,industryPort = [];
				
			//console.log(industryUrl);
			
			profileItems.each(function(index,ele){
				var key = $(this).children().first().text(),value = $(this).children().last().text();
				(key == Constants.PORT_VALUE_KEY) && (portValue = value);
				(key == Constants.QTR_KEY) && (portQTR = value);	
			});
			
			var urlInfo = url.parse(industryUrl),params = querystring.parse(urlInfo.query),
				chd = params.chd.split(":")[1].split(","),chl = params.chl.split("|");
			for(var i=0; i<chd.length; i++){
				industryPort.push({key:chl[i],value:chd[i]});
			}
			//console.log(targetDate,portValue,portQTR,industryPort);
			self.fund.dateStr = targetDate;
			self.fund.portfolioValue = portValue;
			self.fund.qtr = portQTR;
			self.fund.industryPort = industryPort;
			FundModelsFacade.addFundDateList(self.fund);
			FundModelsFacade.addFundIndustryPercentage(self.fund);
		});
	}
}

runByNode && (new Crawler()).run();

module.exports = Crawler;