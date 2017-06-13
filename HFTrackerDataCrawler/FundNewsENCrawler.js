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
	runByNode = argv.join("").indexOf("FundNewsENCrawler") != -1;

function Crawler(indexList){
	this.currentFund = null;
	this.fundList = null;
	fundIndex = indexList;
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
	this.num = 0;
	this.latest = true;
}

_CrawlerThread.prototype = {
	run : function(){
		this.num = 0;
		this.beginRun();
	},
	
	beginRun : function(){
		var newsUrl = this.fund.fundUrl + Config.hf.newsPage.replace("{num}",this.num),self = this;
		console.log(newsUrl);
		HttpUtil.get(newsUrl,function(html){
			self.parseContent(html);
		});
	},
	
	parseContent : function(html){
		var $ = cheerio.load(html),newsListNodes = $(".headlines.related-news .post"),self = this;
		//try{
			if(newsListNodes.length <= 0 || !this.latest){
				//console.warn(self.fund.fundName + ": there is no more news!");
				return;
			}
			newsListNodes.each(function(index,ele){
				var imgSrc = $("a[rel='bookmark'] img",this).attr("data-original"),
					postTitleNode = $(".post-title a",this),postTitle = postTitleNode.html(),
					postLink = postTitleNode.attr("href"),postSummary = $(".entry",this).text(),
					postMetadata = $(".postmetadata",this).text(),postDate,postId,postTimeMark;
					
				postId = postLink.replace(Config.hf.newsPageDomain,"").replace(/\//g,"");
				postDate = postMetadata.split("-")[0].trim();
				postTimeMark = self.parseDate(postMetadata);
				//console.log(postId,postDate,postTitle);
				self.fund.title = postTitle;
				self.fund.dateStr = postDate;
				self.fund.summary = escape(postSummary);
				self.fund.picUrl = imgSrc;
				self.fund.url = postLink;
				self.fund.refId = postId;
				self.fund.newsType = Constants.NEWS_EN;
				self.fund.timeMark = new Date(postTimeMark).getTime();
				
				FundModelsFacade.addFundNews(self.fund,function(results){
					//console.log(results);
					if(results.affectedRows <= 0){
						//console.warn(self.fund.fundName + ": there is no latest news!");
						self.latest = false;
					}
				});
			});
		/*}
		catch(e){
			console.log("==========================");
			console.error(this.fund.fundName,e);
			return;
		}*/
		
		this.num += 10;
		this.beginRun();
		
	},
	
	parseDate : function(metadata){
		var postDate = metadata.split("-")[0].trim(),postDateArr = postDate.split(","),index;
		for(index = postDateArr[0].length-1; index>=0; index--){
			if(postDateArr[0].charAt(index) <= '9' &&  postDateArr[0].charAt(index) >= '0')break;
		}
		postDate = postDateArr[0].substring(0,index+1) + "," + postDateArr[1].trim();
		return postDate;
		
	}
}

runByNode && (new Crawler()).run();

module.exports = Crawler;