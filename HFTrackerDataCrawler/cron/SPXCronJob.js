var cronJob = require("cron").CronJob,
	SPXInfo = require("../config/spx"),
	SPXDataUpdater = require("../SPXDataUpdater");  
 
new cronJob('00 00 21 * * *', function () {  

	var list = SPXInfo.list;
	for(var i=0; i<list.length; i++){
		console.log("[" + new Date() + "]" + i + ". Begin to run for " + list[i].name);
		(new SPXDataUpdater(list[i])).run();
	}
}, null, true);

