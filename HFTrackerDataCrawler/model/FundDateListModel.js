var MysqlUtil = require("../util/MysqlUtil"),
	util = require("util");


var SQL = {
	select : "SELECT fund_name,dateStr FROM fund_date_list WHERE fund_name='%s' ORDER BY dateStr DESC",
	insert : "INSERT INTO fund_date_list(dateStr,portfolioValue,qtr,fund_name)" + 
				" SELECT '%s','%s','%s','%s' FROM DUAL WHERE NOT EXISTS( SELECT id FROM fund_date_list WHERE fund_name = '%s' AND dateStr = '%s') LIMIT 1"
};

function Model(model){
	this.fundName = model.fundName;
	this.managerName = model.managerName;
	this.dateStr = model.dateStr;
	this.portfolioValue = model.portfolioValue || "0";
	this.qtr = model.qtr || "0";
}

Model.prototype = {
	add : function(success,failure){
		var sql = util.format(SQL.insert,
			this.dateStr,this.portfolioValue,this.qtr,this.managerName + "$" + this.fundName,this.managerName + "$" + this.fundName,this.dateStr);

		MysqlUtil.query(sql,success || function(){},failure || function(){});
	},
	
	getByFundName : function(fundName,success,failure){
		var sql = util.format(SQL.select,fundName);
		//console.log(sql);
		MysqlUtil.query(sql,success || function(){},failure || function(){});
	}
}

module.exports = Model;