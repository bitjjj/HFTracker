var	HttpUtil = require("./util/HttpUtil"),
	Constants = require("./util/Constants"),
	Config = require("./config/config"),
	SPXInfo = require("./config/spx"),
	cheerio = require("cheerio"),
	util = require("util"),
	url = require("url"),
	SPXModelsFacade = require("./model/SPXModelsFacade");
	
var argv = process.argv,
	runByNode = argv.join("").indexOf("SPXDataUpdater") != -1;

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
		//console.log(index);
		if(index == 1){//fetch first line item
			var dateStr = dateStrDom.html().trim(),date = new Date(dateStr),timeMark = new Date(dateStr).getTime(),value = parseFloat(valueDom.html().trim());
			SPXModelsFacade.updateInfo({"dateStr":dateStr,"timeMark":timeMark,"value":value,"name":self.info.name});
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