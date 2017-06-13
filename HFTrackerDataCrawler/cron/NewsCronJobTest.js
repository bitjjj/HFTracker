var cronJob = require("cron").CronJob,
	FundNewsENCrawler = require("../FundNewsENCrawler");  
 
//new cronJob('* * 20 * * *', function () {  
	new FundNewsENCrawler("0").run();
	new FundNewsENCrawler("1").run();


//}, null, true);