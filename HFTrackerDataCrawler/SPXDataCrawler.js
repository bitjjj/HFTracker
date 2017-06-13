var	HttpUtil = require("./util/HttpUtil"),
	Constants = require("./util/Constants"),
	Config = require("./config/config"),
	SPXInfo = require("./config/spx"),
	cheerio = require("cheerio"),
	util = require("util"),
	url = require("url"),
	SPXModelsFacade = require("./model/SPXModelsFacade");
	
var argv = process.argv,
	runByNode = argv.join("").indexOf("SPXDataCrawler") != -1;

function Crawler(info){
	this.info = info;
}

Crawler.prototype.run = function(){
	var self = this;
	HttpUtil.get(this.info.url,function(html){
		self._parseContent(html);
	});
}
	
Crawler.prototype._parseContent = function(html){
	var $ = cheerio.load(html),dataItems = $("#datatable tr"),self = this;
	dataItems.each(function(index,ele){
		var className = $(this).attr("class"),dateStrDom = $(".left",this),valueDom = $(".right",this);
		if(className != null && className != ""){
			var dateStr = dateStrDom.html().trim(),date = new Date(dateStr),timeMark = new Date(dateStr).getTime(),value = 1 * valueDom.html().trim();
			if(((date.getMonth() == 0 && date.getDate() == 1) || (date.getMonth() == 11 && date.getDate() == 31)) && !isNaN(value)){//must first month,remove latest estimated value
				switch(self.info.name){
					case Constants.TABLE_PE:
						SPXModelsFacade.addPE({"dateStr":dateStr,"timeMark":timeMark,"value":value});
						break;
					case Constants.TABLE_SHILLER_PE:
						SPXModelsFacade.addShillerPE({"dateStr":dateStr,"timeMark":timeMark,"value":value});
						break;
					case Constants.TABLE_EPS:
						SPXModelsFacade.addEPS({"dateStr":dateStr,"timeMark":timeMark,"value":value});
						break;
					case Constants.TABLE_DPS:
						SPXModelsFacade.addDPS({"dateStr":dateStr,"timeMark":timeMark,"value":value});
						break;
				}
				
			}
			
		}
	});

}


if(runByNode){
	var list = SPXInfo.list;
	for(var i=0; i<list.length; i++){
		console.log(i + ". Begin to run for " + list[i].name);
		(new Crawler(list[i])).run();
	}
}

module.exports = Crawler;