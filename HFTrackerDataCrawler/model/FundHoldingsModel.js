var MysqlUtil = require("../util/MysqlUtil"),
	util = require("util");


var SQL = {
	/*insert : "INSERT INTO fund_holdings(security,ticker,shares,value,activity,action,port,dateStr,fund_name,securityType,addBy)" + 
				" SELECT '%s','%s',%d,%d,%d,%d,%d,'%s','%s','%s','%s' FROM DUAL WHERE NOT EXISTS( SELECT id FROM fund_holdings WHERE " +
				"security = '%s' AND dateStr = '%s' AND fund_name='%s' AND securityType='%s') LIMIT 1"*/
	selectByDate : "SELECT * FROM fund_holdings WHERE dateStr = '%s' AND fund_name='%s'",
	insert : "INSERT INTO fund_holdings(security,ticker,shares,value,activity,action,port,dateStr,fund_name,securityType,addBy) VALUES('%s','%s','%s','%s','%s',%d,'%s','%s','%s','%s','%s')"
};

function Model(model){
	this.fundName = model.fundName;
	this.managerName = model.managerName;
	this.security = model.security;
	this.dateStr = model.dateStr;
	this.ticker = model.ticker;
	this.shares = model.shares;
	this.value = model.value;
	this.activity = model.activity;
	this.action = model.action;
	this.port = model.port;
	this.securityType = model.securityType;
	this.addBy = model.addBy;
}

Model.prototype = {
	add : function(success,failure){
		var sql = util.format(SQL.insert,
			this.security.replace(/'/g,"\\'"),this.ticker,this.removeNonNumberChars(this.shares),
			this.removeNonNumberChars(this.value),this.removeNonNumberChars(this.activity),
			this.action,this.removeNonNumberChars(this.port),this.dateStr,
			this.managerName + "$" + this.fundName,this.securityType,this.addBy);
			//this.managerName + "$" + this.fundName,this.securityType,this.addBy,this.security,this.dateStr,this.managerName + "$" + this.fundName,this.securityType);
		//console.log(sql);
		MysqlUtil.query(sql,success || function(){},failure || function(){});
	},
	
	getByDate : function(fundName,dateStr,success,failure){
		var sql = util.format(SQL.selectByDate,dateStr,fundName);
		//console.log(sql);
		MysqlUtil.query(sql,success || function(){},failure || function(){});
	},
	
	removeNonNumberChars : function(s){
		return s.replace(/[$,%]/g,"");
	}
}


module.exports = Model;