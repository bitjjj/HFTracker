var MysqlUtil = require("../util/MysqlUtil"),
	util = require("util");


var SQL = {
	update : "UPDATE spx_info_year SET dateStr='%s',timeMark=%d,value=%d WHERE spx_index_name='%s'"
};

function Model(model){
	this.dateStr = model.dateStr;
	this.timeMark = model.timeMark;
	this.value = model.value;
	this.name = model.name;
}

Model.prototype = {
	update : function(success,failure){
		var sql = util.format(SQL.update,
			this.dateStr,this.timeMark,this.value,this.name);
		//console.log(sql);
		MysqlUtil.query(sql,success || function(){},failure || function(){});
	}
}


module.exports = Model;