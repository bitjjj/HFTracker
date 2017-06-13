var MysqlUtil = require("../util/MysqlUtil"),
	util = require("util");


var SQL = {
	insert : "INSERT INTO fund_industry_percentage(dateStr,num,name,fund_name)" + 
				" SELECT '%s',%d,'%s','%s' FROM DUAL WHERE NOT EXISTS( SELECT id FROM fund_industry_percentage WHERE fund_name = '%s' AND name='%s' AND dateStr = '%s') LIMIT 1;"
};

function Model(model){
	this.fundName = model.fundName;
	this.managerName = model.managerName;
	this.dateStr = model.dateStr;
	this.num = model.num;
	this.name = model.name;
	this.industryPort =model.industryPort;
}

Model.prototype = {
	add : function(success,failure){
		//var sql = util.format(SQL.insert,
		//	this.dateStr,this.num,this.name,this.managerName + "$" + this.fundName,this.managerName + "$" + this.fundName,this.name,this.dateStr);
		//if(this.dateStr.indexOf("2017")!=-1)console.log(sql);
		var sqls = "";
		for(var i=0;i<this.industryPort.length;i++){
			var sql = util.format(SQL.insert,
				this.dateStr,this.industryPort[i].value,this.industryPort[i].key,this.managerName + "$" + this.fundName,this.managerName + "$" + this.fundName,this.industryPort[i].key,this.dateStr);
			sqls+=sql; 
		}
		
		MysqlUtil.query(sqls,success || function(){},failure || function(){});
	}
}

module.exports = Model;