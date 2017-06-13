var MysqlUtil = require("../util/MysqlUtil"),
	util = require("util");


var SQL = {
	insert : "INSERT INTO fund_info(nameEN,nameZH,nameTW,managerEN,managerZH,managerTW,managerPic,summary,uniqueName,refId)" + 
				" SELECT '%s','%s','%s','%s','%s','%s','%s','%s','%s',%d FROM DUAL WHERE NOT EXISTS( SELECT id FROM fund_info WHERE uniqueName = '%s') LIMIT 1"
};

function Model(model){
	this.fundName = model.fundName;
	this.managerName = model.managerName;
	this.fundNameZH = model.fundName_zh;
	this.managerNameZH = model.managerName_zh;
	this.fundNameTW = model.fundName_tw;
	this.managerNameTW = model.managerName_tw;
	this.managerPic = model.managerPic;
	this.summary = model.summary;
	this.fundId = model.fundId;
}

Model.prototype = {
	add : function(success,failure){
		var sql = util.format(SQL.insert,
			this.fundName,this.fundNameZH,this.fundNameTW,this.managerName,this.managerNameZH,this.managerNameTW,
			this.managerPic,this.summary,this.managerName + "$" + this.fundName,this.fundId,this.managerName + "$" + this.fundName);

		MysqlUtil.query(sql,success || function(){},failure || function(){});
	}
}


module.exports = Model;