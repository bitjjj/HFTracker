var MysqlUtil = require("../util/MysqlUtil"),
	util = require("util");


var SQL = {
	insert : "INSERT INTO fund_news(title,dateStr,summary,picUrl,url,refId,newsType,fund_name,timeMark)" + 
				" SELECT '%s','%s','%s','%s','%s','%s',%d,'%s',%d FROM DUAL WHERE NOT EXISTS( SELECT id FROM fund_news WHERE refId = '%s' AND fund_name='%s') LIMIT 1"
};

function Model(model){
	this.fundName = model.fundName;
	this.managerName = model.managerName;
	this.title = model.title;
	this.dateStr = model.dateStr;
	this.summary = model.summary;
	this.picUrl = model.picUrl;
	this.url = model.url;
	this.refId = model.refId;
	this.newsType = model.newsType;
	this.timeMark = model.timeMark;
}

Model.prototype = {
	add : function(success,failure){
		var sql = util.format(SQL.insert,
			this.title,this.dateStr,this.summary,this.picUrl,this.url,this.refId,this.newsType,this.managerName + "$" + this.fundName,this.timeMark,this.refId,this.managerName + "$" + this.fundName);
		//console.log(sql);
		MysqlUtil.query(sql,success || function(){},failure || function(){});
	}
}


module.exports = Model;