var	HttpUtil = require("./util/HttpUtil"),
	Config = require("./config/config"),
	FundlistExtra = require("./config/fundlistExtra"),
	cheerio = require("cheerio"),
	util = require("util"),
	url = require("url"),
	events = require("events"),
	fs = require("fs");
	
var argv = process.argv,
	runByNode = argv.join("").indexOf("FundListCrawler") != -1;

function Crawler(){
	events.EventEmitter.call(this);
}

util.inherits(Crawler, events.EventEmitter);

Crawler.prototype.run = function(){
	var indexUrl = Config.hf.indexPage,self = this;
	HttpUtil.get(indexUrl,function(html){
		var content = self._parseContent(html);
		self._writeToJsonFile(content);
	});
}
	
Crawler.prototype._parseContent = function(html){
	var $ = cheerio.load(html),
		fundList = $(".section.funds-list-home > div"),
		results = {list:[]},
		self = this;
	
	fundList.each(function(index,ele){
		var funds = [];
		
		funds.push($(".sidebar-funds:first-child",this));
		funds.push($(".sidebar-funds:last-child",this));
		//console.log("funds:",funds.length);
		for(var i=0; i<funds.length; i++){
			var fund,fundExtra,fundUrl = funds[i].find(".sidebar-fund a"),
				fundPic = funds[i].find(".manager-thumb-crop img"),
				fundInfo = funds[i].find(".fund-info"),
				fundId,
				linkStr = fundUrl.attr("href"),
				paths = url.parse(linkStr).path.split("/");
				
			fundId = paths[paths.length - 2];
			fund = {
				fundName:fundInfo.find(".fund-name").html(),
				managerName:fundInfo.find(".manager-name").html(),
				managerPic:fundPic.attr("src") || fundPic.attr("data-original"),
				fundUrl:fundUrl.attr("href"),
				fundId:fundId
			};
			fundExtra = FundlistExtra[fundId];
			if(fundExtra){//mixin chinese info
				for(var key in fundExtra){
					(key != "fundId") && (fund[key] = fundExtra[key]);
				}
			}
			results.list.push(fund);
		}
		
	});
	
	results.list.sort(function(fund1,fund2){
		return fund1.fundId - fund2.fundId > 0 ? 1 : -1;
	});
	return results;
}
	
Crawler.prototype._writeToJsonFile = function(content){
	var self = this;
	if(fs.existsSync(Config.hf.listFile)){
		fs.unlinkSync(Config.hf.listFile);
	}
	fs.writeFile(Config.hf.listFile, JSON.stringify(content), function (err) {
		if (err) throw err;
		self.emit("complete");
	});
}

runByNode && (new Crawler()).run();

module.exports = Crawler;