var cronJob = require("cron").CronJob,
	FundNewsENCrawler = require("../FundNewsENCrawler");  
 
new cronJob('00 00 20 * * *', function () { 
	console.log(new Date());
    new FundNewsENCrawler().run();
}, null, true);