var mysql = require('mysql'),config = require("../config/config");

var pool = mysql.createPool({
    host: config.db.host,
    user: config.db.username,
    password: config.db.password,
    database: config.db.database,
    port: config.db.port,
	connectionLimit : 1,
	dateStrings: true,
    multipleStatements: true
});

var MysqlUtil = {
	
	query:function(sql,callback){
		
		pool.getConnection(function (err, conn) {
			conn.query(sql,function(err,results){//query if this record is exist
				
				if (err) console.error(err);
				conn.release();
				
				if(callback)callback(results);
			});
		});
	},
	
	close : function(){
		pool.end(function (err) {
			if (err) console.error(err);
		});
	}
}

module.exports = MysqlUtil;
