var HttpUtil = require("./util/HttpUtil"),
	Config = require("./config/config"),
	Constants = require("./util/Constants"),
	cheerio = require("cheerio"),
	util = require("util"),
	querystring = require("querystring"),
	url = require("url"),
	events = require("events"),
	FundModelsFacade = require("./model/FundModelsFacade");
	
var argv = process.argv,fundIndex = argv.length>=3 ? argv[2] : null,
	runByNode = argv.join("").indexOf("FundHoldingsCrawler") != -1;

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
			var crawler = new _CrawlerThread(list[fundIndexArr[i]]);
			crawler.run();
			crawler.on("next",function(){
				crawler.beginRun();
			});
		}
	}
	else{
		for(var i=0;i<list.length;i++){
			var crawler = new _CrawlerThread(list[i]);
			crawler.run();
			crawler.on("next",function(){
				crawler.beginRun();
			});
		}
	}
	
}

Crawler.prototype._loadFundList = function(){
	return this.fundList ? this.fundList : (this.fundList = require(Config.hf.listFile));
}

function _CrawlerThread(fund){
	this.fund = fund;
	this.dateList = null;
	this.dateIndex = -1;
	events.EventEmitter.call(this);
}
util.inherits(_CrawlerThread, events.EventEmitter);

_CrawlerThread.prototype.run = function(){
	var self = this;
	FundModelsFacade.getFundDateList(this.fund,function(results){
		self.dateList = results;
		self.beginRun();
	});
}

_CrawlerThread.prototype.beginRun = function(){
	var self = this;
	this.dateIndex++;
	if(this.dateList == null || this.dateIndex >= this.dateList.length)return;
	this.fund.dateStr = this.dateList[this.dateIndex].dateStr;
	console.log("crawlering:",this.fund.fundName,this.fund.dateStr);
	FundModelsFacade.getHoldingsByDate(this.fund,function(results){
		if(results.length <= 0){
			(new _CrawlerThreadByDate(self)).run();
		}
		else{
			console.warn(self.fund.fundName,self.fund.dateStr," has existing holdings");
			self.beginRun();
		}
	});
	
	
}

function _CrawlerThreadByDate(crawler){
	this.crawler = crawler;
	this.num = 0;
}

_CrawlerThreadByDate.prototype = {
	run : function(){
		this.num = 0;
		this.latest = true;
		this.beginRun();
	},
	
	beginRun : function(){
		var holdingsUrl = Config.hf.holdingPage.replace("{date}",this.crawler.fund.dateStr).replace("{num}",this.num).replace("{id}",this.crawler.fund.fundId),
			self = this,headers = Config.hf.holdingHeaders;
		//console.log(holdingsUrl);
		HttpUtil.getWithHeaders(holdingsUrl,headers,function(html){
			self.parseContent(html);
		});
	},
	
	parseContent : function(html){
		var $ = cheerio.load(html),holdingsListNodes = $(".holdings-container tbody tr"),
			loginNode = $(".subsection-header-2.bottom"),self = this;
		
			if(holdingsListNodes.length <= 0 || !this.latest){
				//console.warn(self.fund.fundName + ": there is no more news!");
				self.crawler.emit("next");
				return;
			}
			if(loginNode.length > 0){
				console.error(self.crawler.fund.fundName + ": you should config login cookie!");
				return;
			}
			holdingsListNodes.each(function(){
				var name,ticker,shares,value,activity,port,actionType,securityType,spriteClass;
				$("td",this).each(function(idx,ele){
					switch(idx){
						case 0://sequence
						break;
						case 1://name
							name = $(this).find(".name :first-child").text();
							securityType = $(this).find(".name :last-child[class*='label']").text();
							!securityType && (securityType = Constants.STOCK_TYPE);
							//console.log(name,securityType);
						break;
						case 2://ticker
							ticker = $(this).text();
						break;
						case 3://shares
							shares = $(this).text();
						break;
						case 4://value x$1000
							value = $(this).text();
						break;
						case 5://activity
							activity = $(this).text();
							spriteClass = $(this).find(".inline-icon").attr("class");
							for(var key in Constants.SECURITY_ACTION){
								if(spriteClass.indexOf(key) != -1){
									actionType = Constants.SECURITY_ACTION[key];
								}
							}
						break;
						case 6://port
							port = $(this).text();
						break;
					}
				});
				//console.log(name,ticker,shares,value,activity,port,actionType,securityType);
				self.crawler.fund.security = name;
				self.crawler.fund.ticker = ticker;
				self.crawler.fund.shares = shares;
				self.crawler.fund.value = value;
				self.crawler.fund.activity = activity;
				self.crawler.fund.action = actionType;
				self.crawler.fund.port = port;
				self.crawler.fund.securityType = securityType;
				self.crawler.fund.addBy = Constants.ADD_BY;
				
				
				FundModelsFacade.addFundHoldings(self.crawler.fund);
					
				
			});
		
		
		this.num += 20;
		this.beginRun();
		
	}
}

runByNode && (new Crawler()).run();

module.exports = Crawler;