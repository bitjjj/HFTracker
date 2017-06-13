var MysqlUtil = require("../util/MysqlUtil"),
	util = require("util");


var SQL = {
	insert : "INSERT INTO spx_shiller_pe_year(dateStr,timeMark,`value`)" + 
				" SELECT '%s',%d,%d FROM DUAL WHERE NOT EXISTS( SELECT id FROM spx_shiller_pe_year WHERE dateStr = '%s') LIMIT 1"
};

function Model(model){
	this.dateStr = model.dateStr;
	this.timeMark = model.timeMark;
	this.value = model.value;
}

Model.prototype = {
	add : function(success,failure){
		var sql = util.format(SQL.insert,
			this.dateStr,this.timeMark,this.value,this.dateStr);
		//console.log(sql);
		MysqlUtil.query(sql,success || function(){},failure || function(){});
	}
}


module.exports = Model;